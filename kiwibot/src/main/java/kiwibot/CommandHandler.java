package kiwibot;

import kiwibot.Commands.MasterCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Array;
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
        Member author = _e.getMember();
        System.out.println("wow message");
        if(_e.getMessage().getContentRaw().isEmpty()) return;
        if(author.getUser().isBot()) return;

        Boolean isCringe = false;
        List<Role> userRoles = author.getRoles();
        for (Role role : userRoles){
            if(role.getName().toLowerCase().contains("cringe")){
                isCringe = true;
                break;
            }
        }
        if (isCringe){
            System.out.println("cringe");
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
            System.out.println("Deleting Message");
            _e.getMessage().delete().queue();
        }
        if( !_e.getMessage().getContentDisplay().startsWith(prefix)){
            return;
        }
        if(ignoredUsers.contains(_e.getAuthor().getId()) && !Objects.requireNonNull(_e.getMember()).hasPermission(Permission.ADMINISTRATOR)){
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

    public void addRoleToBlacklist(String _userID){
        ignoredUsers.add(_userID);
    }

    public void removeRoleFromBlacklist(String _userID){
        ignoredUsers.remove(_userID);
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
