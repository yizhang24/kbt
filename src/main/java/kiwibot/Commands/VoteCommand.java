package kiwibot.Commands;
import kiwibot.CommandHandler;
import kiwibot.StatusHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteCommand extends MasterCommand {
    private final String[] reactions = new String[] {"U+20E3",":one:",":two:",":three:",":four:",":five:",":six:",":seven:",":eight:",":nine:",":ten:"};
    private StatusHelper status = new StatusHelper();

    public VoteCommand() {
        this.name = "vote";
        commands.add("vote");
    }
    public void HandleCommand(MessageReceivedEvent _e, List<String> _args){

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < _args.size(); i++) {
            sb.append(_args.get(i));
            sb.append(" ");
        }
        String query = sb.toString();
        switch (_args.get(0)){
            case "vote":
                createVote(query,_e.getChannel());
                break;
            default:
                System.out.println("Vote Command: No command recognized");
                return;
        }
    }


    public List<String> getSubCommands() {
        return commands;
    }

    public boolean acceptingDMs() {
        return false;
    }
    void createVote(String _query, MessageChannel _channel){
        String[] splitString = _query.split("[?]+");
        StringBuilder question = new StringBuilder(splitString[0]);
        String[] options = splitString[1].substring(1).split(" ");
        if(options.length > 10){
            _channel.sendMessage("Can't have more than 10 options.  Why would you want to anyway?").queue();
            return;
        }
        question.setCharAt(0,Character.toUpperCase(question.charAt(0)));
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(question+"?");
        for(int i = 0; i < options.length; i++){
            eb.addField("Option " + (i+1) + ":",options[i],true);
        }
        Message embed = _channel.sendMessage(eb.build()).complete();
        for(int i = 0; i < options.length; i++){
            String reactionUnicode = "U+003"+(i+1) + " U+FE0F U+20E3";
            embed.addReaction(reactionUnicode).queue();
        }
    }
}

