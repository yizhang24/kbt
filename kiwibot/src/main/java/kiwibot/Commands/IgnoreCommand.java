package kiwibot.Commands;

import kiwibot.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCommand extends Command{

    public static List<String> commands = new ArrayList<>();

    public IgnoreCommand(String _prefix) {
        super(_prefix);
        this.SetPrefix(_prefix);
        commands.add("ignore");
        commands.add("unignore");
        commands.add("ignorelist");
    }
    public void HandleCommand(MessageReceivedEvent e, List<String> args){
        List<Member> userids = e.getMessage().getMentionedMembers();
        switch (args.get(0)){
            case "ignore":
                for (Member member : userids) {
                    AddUserToBlacklist(member.getId());
                    SendMessage(e.getChannel(),"Ignored <@!" + member.getId() + ">");
                }
                break;
            case "unignore":
                for (Member member : userids){
                    if(ignoredUsers.contains(member.getId())) ignoredUsers.remove(member.getId());
                    SendMessage(e.getChannel(),"Unignored <@!" + member.getId() + ">");
                }
                break;
            case "ignorelist":
                for(String userID : GetBlacklist()){
                    SendMessage(e.getChannel(),"<@!" + userID + ">");
                }
            default:
                System.out.println("No commnand recognized");
        }
    }

    @Override
    public List<String> getSubCommands() {
        return commands;
    }

    @Override
    public boolean acceptingDMs() {
        return false;
    }
}
