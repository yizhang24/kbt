package kiwibot;

import kiwibot.Commands.MasterCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class CommandHandler extends ListenerAdapter {

    static String prefix;
    private static final List<String> canceledUsers = new ArrayList<>();
    private static final List<String> ignoredUsers = new ArrayList<>();

    public CommandHandler(String _prefix){
        if(_prefix.trim().contains(" ") || _prefix.startsWith(" ")){
            System.out.println("CommandHandler: Cannot use prefix with non-trailing spaces. Prefix has been set to \"!\" ");
            setPrefix("!");
            return;
        }
        setPrefix(_prefix);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent _e){
        System.out.println("wow message");
        if(_e.getMessage().getContentRaw().isEmpty()) return;
        if(_e.getMessage().getAuthor().isBot()) return;
        if(canceledUsers.contains(_e.getAuthor().getId())  && !_e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Deleting Message");
            _e.getMessage().delete().queue();
        }
        if( !_e.getMessage().getContentDisplay().startsWith(prefix)){
            return;
        }
        if(ignoredUsers.contains(_e.getAuthor().getId()) && !_e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Command.java: Ignored User");
            directMessage(_e.getAuthor(),"You've been ignored. xdlmao");
            return;
        }

        TreeMap<String, MasterCommand> subcommands = CommandHelper.getSubcommandList();


        for (MasterCommand subcommand:subcommands.values()) {
            System.out.println(subcommand.commands);
            System.out.println(GetArgs(_e.getMessage().getContentRaw()).get(0));
            if (subcommand.commands.contains(GetArgs(_e.getMessage().getContentRaw()).get(0))){
                System.out.println(subcommand.name);
                subcommand.HandleCommand(_e,GetArgs(_e.getMessage().getContentRaw()));
                break;
            }
        }
        System.out.println("exit");
    }

    public void sendMessage(MessageChannel channel, String contents){
        channel.sendMessage(contents).queue();
    }

    public void sendMessage(MessageChannel channel, Message contents){
        channel.sendMessage(contents).queue();
    }

    public void setPrefix(String _prefix){
        prefix = _prefix;
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

    public List<String> getCancellist(){return canceledUsers;}

    protected List<String> GetArgs(String msg){
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
