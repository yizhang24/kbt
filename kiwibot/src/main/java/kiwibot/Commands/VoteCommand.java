package kiwibot.Commands;
import kiwibot.StatusHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kiwibot.Main.prefix;

public class VoteCommand extends Command{
    private final String[] reactions = new String[] {"U+20E3",":one:",":two:",":three:",":four:",":five:",":six:",":seven:",":eight:",":nine:",":ten:"};
    public static List<String> commands = new ArrayList<>();
    private StatusHelper status = new StatusHelper();

    public VoteCommand(String _prefix) {
        super(_prefix);
        this.SetPrefix(_prefix);
        commands.add("vote");
    }
    public void HandleCommand(MessageReceivedEvent e, List<String> args){

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.size(); i++) {
            sb.append(args.get(i));
            sb.append(" ");
        }
        String query = sb.toString();
        switch (args.get(0)){
            case "vote":
                createVote(query,e.getChannel());
                break;
            default:
                System.out.println(args.get(0));
                System.out.println("VOTE COMMAND: No command recognized");
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
    void createVote(String query, MessageChannel channel){
        String[] splitString = query.split("[?]+");
        StringBuilder question = new StringBuilder(splitString[0]);
        System.out.println("Question is: "+question);
        String[] options = splitString[1].substring(1).split(" ");
        if(options.length > 10){
            channel.sendMessage("You fool! 10 options is way to many for a mortal mind to comprehend.").queue();
            return;
        }
        System.out.println("Options: "+ Arrays.toString(options));
        question.setCharAt(0,Character.toUpperCase(question.charAt(0)));
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(question+"?");
        for(int i = 0; i < options.length; i++){
            eb.addField("Option " + (i+1) + ":",options[i],true);
        }
        Message embed = channel.sendMessage(eb.build()).complete();
        for(int i = 0; i < options.length; i++){
            String reactionUnicode = "U+003"+(i+1) + " U+FE0F U+20E3";
            System.out.println(reactionUnicode);
            embed.addReaction(reactionUnicode).queue();
        }
    }
}

