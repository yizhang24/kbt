package kiwibot.Commands;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class MafiaCommand extends Command{

    public static List<String> commands = new ArrayList<>();

    public MafiaCommand(String _prefix) {
        super(_prefix);
        this.SetPrefix(_prefix);
        commands.add("mafiacreate");
        commands.add("mafiajoin");
        commands.add("mafialeave");
        commands.add("mafiadelete");
        commands.add("mafiaend");
    }


    public void HandleCommand(MessageReceivedEvent e, List<String> args){


    }


    @Override
    public List<String> getSubCommands() {
        return commands;
    }

    @Override
    public boolean acceptingDMs() {
        return false;
    }
}
