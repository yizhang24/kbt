package kbt.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.youtube.model.SearchResult;

import kbt.Constants;
import kbt.music.MusicManager;
import kbt.music.YoutubeSearcher;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Music extends Command {

    private final MusicManager musicManager;
    private final YoutubeSearcher youtube;

    public Music () {
        super("music", new ArrayList<String>(List.of("play", "stop", "skip")));

        musicManager = MusicManager.getInstance();
        youtube = new YoutubeSearcher();
    }

    @Override
    public void HandleCommand(MessageReceivedEvent e, String command, List<String> args) {
        switch (command) {
            case "play":
                String query = String.join(" ", args);
                String address;
                if (YoutubeSearcher.validateURL(query)) {
                    address = query;
                } else {
                    SearchResult result = youtube.search(query);
                    if (result == null) {
                        e.getChannel().sendMessage("No results found.").queue();
                        return;
                    } else {
                        address = result.getId().getVideoId();
                    }
                }
                musicManager.loadTrack(e.getChannel().asTextChannel(), address, e.getMember().getVoiceState().getChannel().asVoiceChannel());
                break;
            case "stop":
                musicManager.stop(e.getGuild());
                break;
            case "skip":
                musicManager.skip(e.getGuild());
                break;
        }
        e.getMessage().addReaction(Emoji.fromUnicode(Constants.GREEN_CHECK)).queue();
    }

}
