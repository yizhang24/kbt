package kbt.commands;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    
    public final ArrayList<String> aliases;
    
    public final String name;

    public Command(String name, ArrayList<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void HandleCommand(MessageReceivedEvent e, List<String> args);

    public ArrayList<String> getAliases() {
        return aliases;
    }
}
