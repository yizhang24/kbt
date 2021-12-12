package kbt.music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicManager {

    private static MusicManager mInstance;

    public static MusicManager getInstance() {
        if (mInstance == null) {
            mInstance = new MusicManager();
        }
        return mInstance;
    }

    private final Map<Long, GuildMusicManager> musicManagerList;
    private final AudioPlayerManager playerManager;

    private MusicManager() {
        musicManagerList = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(playerManager);
    }
    
    private synchronized GuildMusicManager getGuildManager(Guild guild) {
        long guildID = Long.parseLong(guild.getId());
        GuildMusicManager localManager = musicManagerList.get(guildID);
        if (localManager == null) {
            localManager = new GuildMusicManager(playerManager, guild.getAudioManager());
            musicManagerList.put(guildID, localManager);
        }
        guild.getAudioManager().setSendingHandler(localManager.getSendHandler());

        return localManager;
    }
    
    public void loadTrack(Guild guild, String address, VoiceChannel channel) {

        GuildMusicManager localMusicManager = getGuildManager(guild);
        AudioManager localAudioManager = guild.getAudioManager();

        System.out.println(address);

        playerManager.loadItem(address, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if (!localAudioManager.isConnected()
                        || !localAudioManager.getConnectedChannel().getId().equals(channel.getId())) {
                    localAudioManager.closeAudioConnection();
                    localAudioManager.openAudioConnection(channel);
                }
                localMusicManager.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // TODO Auto-generated method stub

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();

            }

        });
    }
    
    public void stop(Guild guild) {
        getGuildManager(guild).scheduler.stop();
    }

    public void skip(Guild guild) {
        getGuildManager(guild).scheduler.nextTrack();
    }
}
