package kiwibot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends ListenerAdapter {
    public abstract void HandleCommand(MessageReceivedEvent e, List<String> args);
    public abstract List<String> getSubCommands();
    public abstract boolean acceptingDMs();
    public String name;
    static String prefix;
    public static List<String> ignoredUsers = new ArrayList<>();

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
        if(!ContainsCommand(e.getMessage()) || !e.getMessage().getContentDisplay().startsWith(prefix)){
            return;
        }
        if(ignoredUsers.contains(e.getAuthor().getId()) && !e.getMember().hasPermission(Permission.ADMINISTRATOR)){
            return;
        }
        if(!e.isFromGuild() && !acceptingDMs()) {
            e.getChannel().sendMessage("Sorry, but this command cannot be used inside a direct message.");
            return;
        }
        HandleCommand(e,GetArgs(e.getMessage()));
    }

    protected boolean ContainsCommand(Message msg){
        return getSubCommands().contains(GetArgs(msg).get(0));
    }

    protected List<String> GetArgs(Message msg){
        return GetArgs(msg.getContentRaw());
    }

    protected List<String> GetArgs(String msg){
        List<String> temp = Arrays.asList(msg.split(" ").clone());
        if(prefix.contains(" ")) {
            temp.remove(0);
        }else{
            temp.set(0,temp.get(0).substring(prefix.length()));
        }
        System.out.println(temp);
        return temp;
    }

    protected void SendMessage(MessageChannel _channel, String e){
        _channel.sendMessage(e);
    }

    protected void DirectMessage(PrivateChannel _channel, String e){
        _channel.sendMessage(e);
    }

    protected void SetPrefix(String _prefix){
        prefix = _prefix;
    }

    protected void SetName(String _name){
        name = _name;
    }
}
