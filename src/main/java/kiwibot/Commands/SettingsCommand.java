package kiwibot.Commands;

import kiwibot.Main;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class SettingsCommand extends MasterCommand{
    public SettingsCommand() {
        this.name = "settings";
        commands.add("setdjrole");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){

        if(!_e.getMember().hasPermission(Permission.MANAGE_SERVER)){
            _e.getChannel().sendMessage("Ya don't have perms to use this, ya walnut").queue();
        }

        switch (_args.get(0)){
            case "setdjrole":
                String targetRoleID = _e.getMessage().getMentionedRoles().get(0).getId();
                Main.configuration.setDJRole(_e.getGuild().getId(), targetRoleID);
                _e.getChannel().sendMessage("Set <@&" + targetRoleID + "> as the DJ role.  Members must now have this role to use music functions.").queue();
                break;
            default:
                System.out.println("No command recognized");
        }

        Main.configLoader.updateConfig(Main.configuration);
    }
}
