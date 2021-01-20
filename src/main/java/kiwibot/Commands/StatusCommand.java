package kiwibot.Commands;

import kiwibot.CommandHandler;
import kiwibot.StatusHelper;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;


public class StatusCommand extends MasterCommand{

    private final StatusHelper status = new StatusHelper();

    public StatusCommand() {
        this.name = "status";
        commands.add("online");
        commands.add("idle");
        commands.add("dnd");
        commands.add("donotdisturb");
        commands.add("invis");
        commands.add("invisible");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){
        switch (_args.get(0)){
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
            System.out.println("No command recognized");
            return;
        }
    }
}
