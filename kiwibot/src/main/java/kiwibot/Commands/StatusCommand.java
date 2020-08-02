package kiwibot.Commands;

import kiwibot.StatusHelper;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;


public class StatusCommand extends Command{

    public static List<String> commands = new ArrayList<>();
    private StatusHelper status = new StatusHelper();

    public StatusCommand(String _prefix) {
        super(_prefix);
        this.SetPrefix(_prefix);
        commands.add("online");
        commands.add("idle");
        commands.add("dnd");
        commands.add("donotdisturb");
        commands.add("invis");
        commands.add("invisible");
    }
    public void HandleCommand(MessageReceivedEvent e, List<String> args){
        switch (args.get(0)){
            case "online":
                status.ChangeStatus(OnlineStatus.ONLINE);
                break;
            case "idle":
                status.ChangeStatus(OnlineStatus.IDLE);
                break;
            case "dnd":
            case "donotdisturb":
                status.ChangeStatus(OnlineStatus.DO_NOT_DISTURB);
                break;
            case "invis":
            case "invisible":
                status.ChangeStatus(OnlineStatus.INVISIBLE);
                break;
            default:
                System.out.println("No commnand recognized");
                return;
        }
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
