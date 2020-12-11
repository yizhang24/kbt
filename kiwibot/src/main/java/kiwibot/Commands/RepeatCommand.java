package kiwibot.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class RepeatCommand extends MasterCommand {

    private boolean isSpamming = false;
    private Thread thread;

    public RepeatCommand() {
        this.name = "repeat";
        commands.add("startspam");
        commands.add("stopspam");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){
        if(!Objects.requireNonNull(_e.getMember()).hasPermission(Permission.ADMINISTRATOR)){
            return;
        }
        switch (_args.get(0)){
            case "startspam":
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < _args.size(); i++) {
                    sb.append(_args.get(i));
                    sb.append(" ");
                }
                String query = sb.toString();
                isSpamming = true;
                thread = new Thread(() -> {
                    while (isSpamming) {
                        spam(query, _e.getChannel());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                break;
            case "stopspam":
                System.out.println("stopping spam");
                isSpamming = false;
                break;
            default:
                System.out.println("No command recognized");
        }
    }
    private void spam(String message, MessageChannel channel){
            System.out.println(isSpamming);
            channel.sendMessage(message).queue();
    }
}
