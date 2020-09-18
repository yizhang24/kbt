package kiwibot.Commands;

import kiwibot.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CancelCommand extends Command{

    public static List<String> commands = new ArrayList<>();

    public CancelCommand(String _prefix) {
        super(_prefix);
        this.SetPrefix(_prefix);
        commands.add("cancel");
        commands.add("decancel");
        commands.add("cancellist");
    }
    public void HandleCommand(MessageReceivedEvent e, List<String> args){
        List<Member> userids = e.getMessage().getMentionedMembers();
        switch (args.get(0)){
            case "cancel":
                if(!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)){
                    e.getChannel().sendMessage("I can't do this, as I don't have the manage message permission.").queue();
                }
                for (Member member : userids) {
                    AddUserToCancellistt(member.getId());
                    SendMessage(e.getChannel(),"Canceled <@!" + member.getId() + ">");
                }
                break;
            case "decancel":
                for (Member member : userids){
                    if(ignoredUsers.contains(member.getId())) RemoveUserFromCancellist(member.getId());
                    SendMessage(e.getChannel(),"DeCanceled <@!" + member.getId() + ">");
                }
                break;
            case "cancellist":
                for(String userID : GetCancelist()){
                    SendMessage(e.getChannel(),"<@!" + userID + ">");
                }
            default:
                System.out.println("No command recognized");
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
