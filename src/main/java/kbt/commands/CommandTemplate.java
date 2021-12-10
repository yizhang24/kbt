package kbt.commands;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandTemplate extends Command {

    public CommandTemplate() {
        super("template command, edit me!",
                new ArrayList<String>(List.of()));
    }

    @Override
    public void HandleCommand(MessageReceivedEvent e, String command, List<String> args) {
        switch (command) {
            
        }
    }

}
