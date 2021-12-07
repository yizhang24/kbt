package kbt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kbt.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Events extends ListenerAdapter{

    private static HashMap<ArrayList<String>, Command> aliases = new HashMap<ArrayList<String>, Command>();

    public static Command registerCommand(Command command) {
        aliases.put(command.getAliases(), command);
        return command;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String prefix = Main.config.getString(event.getGuild().getId() + ".prefix");
        
        if(!event.getMessage().getContentDisplay().startsWith(prefix)) return;

        String messageContent = event.getMessage().getContentDisplay().substring(prefix.length()).trim();
        List<String> messageArgs = Arrays.asList(messageContent.split(" "));
        String command = messageArgs.get(0);
        messageArgs.remove(0);

        for(ArrayList<String> alias : aliases.keySet()) {
            if(alias.contains(command)) {
                aliases.get(alias).HandleCommand(event, messageArgs);
            }
        }
    }
}