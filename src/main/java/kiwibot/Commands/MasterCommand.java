package kiwibot.Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class MasterCommand {
    public List<String> commands = new ArrayList<>();
    public String name;

    public abstract void HandleCommand(MessageReceivedEvent e, List<String> args);
}
