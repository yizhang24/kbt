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

import org.apache.http.impl.auth.NTLMEngineException;

public class CommandHandler extends ListenerAdapter {


    private static final List<String> canceledUsers = new ArrayList<>();
    private static final List<String> ignoredUsers = new ArrayList<>();


    @Override
    public void onMessageReceived(MessageReceivedEvent _e){
        if(_e.getMessage().getContentRaw().isEmpty()) return;

        if(canceledUsers.contains(_e.getAuthor().getId())  && !Objects.requireNonNull(_e.getMember()).hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Command Handler: Deleting Censored Message");
            _e.getMessage().delete().queue();
        }

        GuildInfo guildInfo = ConfigLoader.finalConfig.getGuildData(_e.getGuild().getId());

        String localPrefix;
        try {
            localPrefix = guildInfo.prefix;

            if(localPrefix == null){
                throw new NullPointerException();
            }
            System.out.println("CommandHandler.java: local prefix is " + localPrefix);
        } catch (NullPointerException e) {
            System.out.println("CommandHandler.java: No prefix found for guildid " + _e.getGuild().getId() + ", setting it to the default of \"!\"");
            Main.settingsCommand.SetPrefix("!", _e.getGuild());
            localPrefix = "!";
        }

        if(!_e.getMessage().getContentDisplay().startsWith(localPrefix)){
            return;
        }

        System.out.println(_e.getMessage().getContentDisplay());
        
        if(ignoredUsers.contains(_e.getAuthor().getId()) && !Objects.requireNonNull(_e.getMember()).hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Command.java: Ignored User Requested Command");
            directMessage(_e.getAuthor(),"You've been ignored. xdlmao");
            return;
        }

        TreeMap<String, MasterCommand> subcommands = CommandHelper.getSubcommandList();

        List<String> commandArgs = GetArgs(_e.getMessage().getContentRaw(), localPrefix);
        for (MasterCommand subcommand:subcommands.values()) {
            if (subcommand.commands.contains(commandArgs.get(0))){
                subcommand.HandleCommand(_e, commandArgs);
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
        String command = msg.substring(_prefix.length()).trim();
        List<String> temp = new LinkedList<>(Arrays.asList(command.split(" ").clone()));
        System.out.println(command);
        return temp;
    }

}
