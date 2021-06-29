package kiwibot.Commands;

import kiwibot.Main;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class SettingsCommand extends MasterCommand{
    public SettingsCommand() {
        this.name = "settings";
        commands.add("setdjrole");
        commands.add("setprefix");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){

        if(!_e.getMember().hasPermission(Permission.MANAGE_SERVER)){
            _e.getChannel().sendMessage("Ya don't have perms to use this, you walnut").queue();
        }

        switch (_args.get(0)){
            case "setdjrole":
                String targetRoleID = _e.getMessage().getMentionedRoles().get(0).getId();
                Main.configuration.setDJRole(_e.getGuild().getId(), targetRoleID);
                _e.getChannel().sendMessage("Set <@&" + targetRoleID + "> as the DJ role.  Members must now have this role to use music functions.").queue();
                break;
            case "setprefix":
                int currentPrefixLength;
                try{
                    currentPrefixLength = Main.configLoader.finalConfig.getGuildData(_e.getGuild().getId()).prefix.length();
                } catch(NullPointerException e){
                    currentPrefixLength = 1;
                }
                System.out.println("Prefix length " + currentPrefixLength);
                System.out.println("Text message |" + _e.getMessage().getContentRaw()+ "|");
                String query;
                try {
                    query = _e.getMessage().getContentRaw().substring(currentPrefixLength+10);
                } catch (StringIndexOutOfBoundsException e) {
                    _e.getChannel().sendMessage("Please actually specify a valid prefix ;-;").queue();
                    return;
                }
                System.out.println(query);
                _e.getChannel().sendMessage("Prefix set to \"" + query + "\" for this server.").queue();
                SetPrefix(query, _e.getGuild());
                break;
            default:
                System.out.println("Settings Command: No command recognized");
        }

        Main.configLoader.updateConfig(Main.configuration);
    }

    public void SetPrefix(String newPrefix, Guild guild){
        Main.configLoader.finalConfig.setPrefix(guild.getId(),newPrefix);
        Main.configLoader.updateConfig();
    }
}
