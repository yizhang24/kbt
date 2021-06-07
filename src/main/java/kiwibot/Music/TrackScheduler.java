package kiwibot.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    public BlockingQueue<AudioTrack> queue;
    private final AudioManager manager;
    public boolean loop;
    public AudioTrack lastTrack;
    public TrackScheduler(AudioPlayer player, AudioManager manager){
        this.player = player;
        this.manager = manager;
        queue = new LinkedBlockingDeque<>();
    }

    public void Queue(AudioTrack track, boolean _loop){
        loop = _loop;
        if(queue.size() > 0){
            queue.offer(track);
        }
        player.startTrack(track,true);
        PrintQueue();
    }

    public void ForcePlay(AudioTrack track, boolean _loop){
        loop = _loop;
        queue.clear();
        player.startTrack(track, false);
    }

    public boolean NextTrack(){
        if(queue.size() > 0) {
            //queue.poll();
            player.startTrack(queue.poll(), false);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean Seek(Long millis){
        if(player.getPlayingTrack() == null) return false;
        AudioTrack tracc = player.getPlayingTrack();
        tracc.setPosition(millis);
        return true;
    }

    public void ClearQueue(){
        if(!queue.isEmpty()) queue.clear();
    }

    public boolean Cut(int index){
        BlockingQueue<AudioTrack> temp = new LinkedBlockingDeque<>();
        Object[] holder = queue.toArray();
        for(int i = 0; i < queue.size(); i++){
            if(i != index-1) temp.add((AudioTrack) holder[i]);
        }
        queue = temp;
        return true;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason){
        System.out.println("Track Scheduler: End reason: " + endReason);
        PrintQueue();
        this.lastTrack = track;
        if(endReason == AudioTrackEndReason.REPLACED){
            return;
        }
        if(endReason.mayStartNext && queue.size() > 0) {
            NextTrack();
            return;
        }else if(loop){
            player.startTrack(lastTrack.makeClone(),false);
        }else {
            ClearQueue();
            manager.closeAudioConnection();
        }

    }
    public void SetLoop(boolean _loop){
        loop = _loop;
    }
    public BlockingQueue<AudioTrack> getQueue(){return queue;}
    public void PrintQueue(){
        for(AudioTrack track : queue){
            System.out.println(track.getInfo());
        }
    }
}

