package kbt.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.managers.AudioManager;

public class GuildMusicManager {

    public final AudioPlayer player;

    public final TrackScheduler scheduler;

    public GuildMusicManager(AudioPlayerManager playerManager, AudioManager audioMananger){
    player = playerManager.createPlayer();
        scheduler = new TrackScheduler(player, audioMananger);
        player.addListener(scheduler);
    }
    public AudioPlayerSendHandler getSendHandler(){
        return new AudioPlayerSendHandler(player);
    }

    }
