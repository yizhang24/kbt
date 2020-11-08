package kiwibot.Commands;


import kiwibot.CommandHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class MafiaCommand extends MasterCommand{

    public static List<String> commandList = new ArrayList<>();
    public MafiaCommand(String _prefix) {
        this.name = "mafia";
        commands.add("mafiacreate");
        commands.add("mafiajoin");
        commands.add("mafialeave");
        commands.add("mafiadelete");
        commands.add("mafiaend");
        this.commands = commandList;
    }


    public void HandleCommand(MessageReceivedEvent e, List<String> args){


    }
}
