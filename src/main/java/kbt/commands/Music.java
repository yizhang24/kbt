package kbt.commands;

import java.util.ArrayList;
import java.util.List;

import kbt.Constants;
import kbt.music.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Music extends Command {

    private final MusicManager musicManager;

    public Music () {
        super("music", new ArrayList<String>(List.of("play", "stop")));

        musicManager = MusicManager.getInstance();
    }

    @Override
    public void HandleCommand(MessageReceivedEvent e, String command, List<String> args) {
        switch (command) {
            case "play":
                String query = String.join(" ", args);
                musicManager.loadTrack(e.getGuild(), query, e.getMember().getVoiceState().getChannel());
                break;
            case "stop":
                musicManager.stop(e.getGuild());
                break;
            case "skip":
                musicManager.skip(e.getGuild());
                break;
        }
        e.getMessage().addReaction(Constants.GREEN_CHECK).queue();
    }

}
