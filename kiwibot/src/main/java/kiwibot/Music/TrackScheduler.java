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
    public int queueLength;
    public boolean loop;
    public AudioTrack lastTrack;
    public TrackScheduler(AudioPlayer player, AudioManager manager){
        this.player = player;
        this.manager = manager;
        queue = new LinkedBlockingDeque<>();
        queueLength = queue.size();
    }

    public void Queue(AudioTrack track, boolean _loop){
        loop = _loop;
        queue.offer(track);
        player.startTrack(track,true);
        if(!queue.isEmpty()) System.out.println("music queue = " + queue.toString());
        queueLength = queue.size();
        PrintQueue();
    }

    public void ForcePlay(AudioTrack track, boolean _loop){
        loop = _loop;
        player.startTrack(track, false);
        queue.clear();
        queueLength = 0;
    }

    public boolean NextTrack(){
        //System.out.println("queue size = " +queue.size());
        System.out.println(queue);
        //System.out.println(queue.peek());
        if(queue.size() > 0) {
            queue.poll();
            player.startTrack(queue.poll(), false);
            System.out.println("Shoullddd be working");
            return true;
        }
        else{
            System.out.println("track is nonexistent");
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
        queueLength = queue.size();
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
        System.out.println(endReason);
        PrintQueue();
        this.lastTrack = track;
        if(endReason == AudioTrackEndReason.REPLACED){
            return;
        }
        if(endReason.mayStartNext && queue.size() > 0) {
            System.out.println("Next");
            NextTrack();
            return;
        }else if(loop){
            System.out.println("looped!");
            player.startTrack(lastTrack.makeClone(),false);
        }else {
            ClearQueue();
            System.out.println("Stop");
            manager.closeAudioConnection();
        }

    }
    public void SetLoop(boolean _loop){
        loop = _loop;
    }
    public int GetQueueLength(){
        return queueLength;
    }
    public BlockingQueue<AudioTrack> getQueue(){return queue;}
    public void PrintQueue(){
        for(AudioTrack track : queue){
            System.out.println(track.getInfo());
        }
    }
}

