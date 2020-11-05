package kiwibot.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.managers.AudioManager;

public class GuildMusicManager {

    public final AudioPlayer player;

    public final TrackScheduler scheduler;

    public GuildMusicManager(AudioPlayerManager _playerManager, AudioManager _manager){
        player = _playerManager.createPlayer();
        scheduler = new TrackScheduler(player,_manager);
        player.addListener(scheduler);
    }
    public AudioPlayerSendHandler getSendHandler(){
        return new AudioPlayerSendHandler(player);
    }

}
