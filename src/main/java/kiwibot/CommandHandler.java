package kiwibot;

import kiwibot.Commands.MasterCommand;
import kiwibot.Config.ConfigInfo;
import kiwibot.Config.ConfigLoader;
import kiwibot.Config.GuildInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class CommandHandler extends ListenerAdapter {


    private static final List<String> canceledUsers = new ArrayList<>();
    private static final List<String> ignoredUsers = new ArrayList<>();


    @Override
    public void onMessageReceived(MessageReceivedEvent _e){
        Member author = _e.getMember();
        if(_e.getMessage().getContentRaw().isEmpty()) return;
        //if(author.getUser().isBot()) return;

        Boolean isCringe = false;
        List<Role> userRoles = author.getRoles();
        for (Role role : userRoles){
            if(role.getName().toLowerCase().contains("cringe")){
                isCringe = true;
                break;
            }
        }
        if (isCringe){
            System.out.println("Command Handler: Adding Cringe Reaction");
            Random random = new Random();
            int cringeorcrigen = random.nextInt(2);
            String[] cringe;
            if (cringeorcrigen < 1){
                cringe = new String []{"U+1F1E8","U+1F1F7","U+1F1EE","U+1F1EC","U+1F1EA","U+1F1F3"};
            }else{
                cringe = new String []{"U+1F1E8","U+1F1F7","U+1F1EE","U+1F1F3","U+1F1EC","U+1F1EA"};
            }
            for (String letter:cringe) {
                _e.getMessage().addReaction(letter).queue();
            }
        }


        if(canceledUsers.contains(_e.getAuthor().getId())  && !Objects.requireNonNull(_e.getMember()).hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Command Handler: Deleting Censored Message");
            _e.getMessage().delete().queue();
        }

        GuildInfo guildInfo = ConfigLoader.finalConfig.getGuildData(_e.getGuild().getId());

        String localPrefix = guildInfo.prefix;

        System.out.println("CommandHandler.java: local prefix is " + localPrefix);

        if(localPrefix == null){
            System.out.println("CommandHandler.java: No prefix found for guildid " + _e.getGuild().getId() + ", setting it to the default of \"!\"");

            Main.configLoader.updateConfig();
            localPrefix = "!";
        }

        if(!_e.getMessage().getContentDisplay().startsWith(localPrefix)){
            return;
        }
        if(ignoredUsers.contains(_e.getAuthor().getId()) && !Objects.requireNonNull(_e.getMember()).hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Command.java: Ignored User Requested Command");
            directMessage(_e.getAuthor(),"You've been ignored. xdlmao");
            return;
        }

        TreeMap<String, MasterCommand> subcommands = CommandHelper.getSubcommandList();


        for (MasterCommand subcommand:subcommands.values()) {
            if (subcommand.commands.contains(GetArgs(_e.getMessage().getContentRaw(), localPrefix).get(0))){
                subcommand.HandleCommand(_e,GetArgs(_e.getMessage().getContentRaw(), localPrefix));
                break;
            }
        }
    }
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getChannelLeft().equals(Main.musicHandler.getVC(event.getGuild())) && event.getChannelLeft().getMembers().size() == 1) {
            Main.musicHandler.Stop(event.getGuild());
        }
    }
    public void sendMessage(MessageChannel channel, String contents){
        channel.sendMessage(contents).queue();
    }

    public void sendMessage(MessageChannel channel, Message contents){
        channel.sendMessage(contents).queue();
    }


    public void directMessage(User _user, String _e){
        _user.openPrivateChannel().complete().sendMessage(_e).queue();
    }

    public void addUserToBlacklist(String _userID){
        ignoredUsers.add(_userID);
    }

    public void removeUserFromBlacklist(String _userID){
        ignoredUsers.remove(_userID);
    }

    public List<String> getBlacklist(){return ignoredUsers;}


    public void addUserToCancellist(String _userID){
        canceledUsers.add(_userID);
    }

    public void removeUserFromCancellist(String _userID){
        canceledUsers.remove(_userID);
    }

    public void addRoleToBlacklist(String _userID){
        ignoredUsers.add(_userID);
    }

    public void removeRoleFromBlacklist(String _userID){
        ignoredUsers.remove(_userID);
    }
    public List<String> getCancellist(){return canceledUsers;}

    protected List<String> GetArgs(String msg, String _prefix){
        String prefix = _prefix;
        List<String> temp = new LinkedList<>(Arrays.asList(msg.split(" ").clone()));
        if(prefix.contains(" ")) {
            temp.remove(0);
        }else{
            temp.set(0,temp.get(0).substring(prefix.length()));
        }
        //System.out.println(temp);
        return temp;
    }

}
