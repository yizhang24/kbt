package kbt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import kbt.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Events extends ListenerAdapter{

    private static HashMap<ArrayList<String>, Command> aliases = new HashMap<ArrayList<String>, Command>();

    public Command registerCommand(Command command) {
        aliases.put(command.getAliases(), command);
        return command;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String prefix = Main.config.getString(event.getGuild().getId() + ".prefix");

        if(event.getMessage().getContentDisplay().contains("fuck")) {
            event.getMessage().reply("https://i.imgur.com/i3UnHnd.png").queue();
        }

        if(!event.getMessage().getContentDisplay().startsWith(prefix)) return;

        String messageContent = event.getMessage().getContentDisplay().substring(prefix.length()).trim();
        LinkedList<String> messageArgs = new LinkedList<String>(Arrays.asList(messageContent.split(" ")));
        String command = messageArgs.get(0);
        messageArgs.remove(0);

        for(ArrayList<String> alias : aliases.keySet()) {
            if(alias.contains(command)) {
                aliases.get(alias).HandleCommand(event, command, messageArgs);
            }
        }
    }
}