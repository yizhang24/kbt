package kiwibot.Commands;

import kiwibot.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCommand extends MasterCommand {

    public IgnoreCommand() {
        this.name = "ignore";
        commands.add("ignore");
        commands.add("unignore");
        commands.add("ignorelist");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){
        List<Member> userids = _e.getMessage().getMentionedMembers();
        switch (_args.get(0)){
            case "ignore":
                for (Member member : userids) {
                    Main.commandHandler.addUserToBlacklist(member.getId());
                    Main.commandHandler.sendMessage(_e.getChannel(),"Ignored <@!" + member.getId() + ">");
                }
                break;
            case "unignore":
                for (Member member : userids){
                    if(Main.commandHandler.getBlacklist().contains(member.getId())) Main.commandHandler.getBlacklist().remove(member.getId());
                    Main.commandHandler.sendMessage(_e.getChannel(),"Unignored <@!" + member.getId() + ">");
                }
                break;
            case "ignorelist":
                for(String userID : Main.commandHandler.getBlacklist()){
                    Main.commandHandler.sendMessage(_e.getChannel(),"<@!" + userID + ">");
                }
            default:
                System.out.println("No commnand recognized");
        }
    }

}
