package kiwibot.Commands;


import kiwibot.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CensorCommand extends MasterCommand{


    public CensorCommand() {
        this.name = "cancel";
        commands.add("cancel");
        commands.add("decancel");
        commands.add("cancellist");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){
        List<Member> userids = _e.getMessage().getMentionedMembers();
        switch (_args.get(0)){
            case "cancel":
                if(!_e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)){
                    _e.getChannel().sendMessage("I can't do this, as I don't have the manage message permission.").queue();
                }
                for (Member member : userids) {
                    Main.commandHandler.addUserToCancellist(member.getId());
                    Main.commandHandler.sendMessage(_e.getChannel(),"Canceled <@!" + member.getId() + ">");
                }
                break;
            case "decancel":
                for (Member member : userids){
                    if(Main.commandHandler.getCancellist().contains(member.getId())) Main.commandHandler.removeUserFromCancellist(member.getId());
                    Main.commandHandler.sendMessage(_e.getChannel(),"DeCanceled <@!" + member.getId() + ">");
                }
                break;
            case "cancellist":
                for(String userID : Main.commandHandler.getCancellist()){
                    Main.commandHandler.sendMessage(_e.getChannel(),"<@!" + userID + ">");
                }
            default:
                System.out.println("No command recognized");
        }
    }

}
