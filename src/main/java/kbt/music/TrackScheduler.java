package kbt.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.managers.AudioManager;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final AudioManager audioManager;

    public TrackScheduler(AudioPlayer player, AudioManager audioManager) {
        this.player = player;
        this.audioManager = audioManager;
        this.queue = new LinkedBlockingQueue<>();

    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            System.out.println(queue.toString());
        }
    }

    public void nextTrack() {
        player.startTrack(queue.peek(), false);
    }

    public void stop() {
        player.stopTrack();
        queue.clear();
        audioManager.closeAudioConnection();
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext & queue.peek() != null) {
            nextTrack();
        } else if (endReason != AudioTrackEndReason.REPLACED) {
            audioManager.closeAudioConnection();
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext
        // = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not
        // finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you
        // can put a
        // clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be
        // received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start
        // a new track
    }
}
