package kbt.commands;

import java.util.ArrayList;
import java.util.List;

import kbt.Constants;
import kbt.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Status extends Command {

    public Status() {
        super(
            "status",
            new ArrayList<String>(List.of("status", "setstatus", "setonline", "setidle", "setdnd", "setinvis"))
        );
    }

    @Override
    public void HandleCommand(MessageReceivedEvent e, String command, List<String> args) {
        switch(command) {
            case "status":
            case "setstatus":
                if (args.isEmpty()) {
                    Activity activity = null;
                    Main.setStatus(activity);
                    return;
                }
                ActivityType activityType = ActivityType.PLAYING;
                String activity = String.join(" ", args.subList(1, args.size()));
                switch(args.get(0)){
                    case "listening":
                        activityType = ActivityType.LISTENING;
                        break;
                    case "streaming":
                        activityType = ActivityType.STREAMING;
                        break;
                    case "watching":
                        activityType = ActivityType.WATCHING;
                        break;
                    case "competing":
                        activityType = ActivityType.COMPETING;
                        break;
                    default:
                        activity = String.join(" ", args);
                }
                Main.setStatus(Activity.of(activityType, activity));
                e.getMessage().addReaction(Emoji.fromUnicode(Constants.GREEN_CHECK)).queue();
        }
    }
    
}
