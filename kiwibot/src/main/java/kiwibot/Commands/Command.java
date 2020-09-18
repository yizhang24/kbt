package kiwibot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Command extends ListenerAdapter {
    public abstract void HandleCommand(MessageReceivedEvent e, List<String> args);
    public abstract List<String> getSubCommands();
    public abstract boolean acceptingDMs();
    public String name;
    static String prefix;
    public static List<String> ignoredUsers = new ArrayList<>();
    public static List<String> canceledUsers = new ArrayList<>();

    public Command(String _prefix){
        SetName(this.getClass().getName());
        if(_prefix.trim().contains(" ") || _prefix.startsWith(" ")){
            System.out.println("MasterCommand: Cannot use prefix with non-trailing spaces. Set prefix to \"!\" ");
            SetPrefix("!");
            return;
        }
        SetPrefix(_prefix);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getMessage().getContentRaw().isEmpty()) return;
        if(e.getMessage().getAuthor().isBot()) return;
        if(canceledUsers.contains(e.getAuthor().getId())  && !e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Deleting Message");
            e.getMessage().delete().queue();
        }
        if( !e.getMessage().getContentDisplay().startsWith(prefix) || !ContainsCommand(e.getMessage())){
            return;
        }
        if(ignoredUsers.contains(e.getAuthor().getId()) && !e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            System.out.println("Command.java: Ignored User");
            DirectMessage(e.getAuthor(),"You've been ignored. Please feel free to leave your complaints in the nearest trash can.");
            return;
        }
        if(!e.isFromGuild() && !acceptingDMs()) {
            e.getChannel().sendMessage("Sorry, but this command cannot be used inside a direct message.");
            return;
        }
        HandleCommand(e,GetArgs(e.getMessage()));
    }

    protected boolean ContainsCommand(Message msg){
        if(getSubCommands() == null) return false;
        return getSubCommands().contains(GetArgs(msg).get(0));
    }

    protected List<String> GetArgs(Message msg){
        return GetArgs(msg.getContentRaw());
    }

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

    protected void SendMessage(MessageChannel _channel, String e){
        _channel.sendMessage(e).queue();
    }

    protected void DirectMessage(User user, String e){
        user.openPrivateChannel().complete().sendMessage(e).queue();
    }

    protected void SetPrefix(String _prefix){
        prefix = _prefix;
    }

    protected void SetName(String _name){
        name = _name;
    }

    protected void AddUserToBlacklist(String _userID){
        ignoredUsers.add(_userID);
    }

    protected void RemoveUserFromBlacklist(String _userID){
        ignoredUsers.remove(_userID);
    }

    protected List<String> GetBlacklist(){return ignoredUsers;}


    protected void AddUserToCancellistt(String _userID){
        canceledUsers.add(_userID);
    }

    protected void RemoveUserFromCancellist(String _userID){
        canceledUsers.remove(_userID);
    }

    protected List<String> GetCancelist(){return canceledUsers;}
}
