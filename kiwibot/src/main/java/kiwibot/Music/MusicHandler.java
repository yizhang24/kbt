package kiwibot.Music;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import kiwibot.Commands.MasterCommand;
import kiwibot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.RestAction;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class MusicHandler extends MasterCommand {
    private final String[] broughtToYouBy = new String[]{"Xander maining Tone", "haha epg go whoosh", "big chungus", "Ray's 1k voices", "the scp foundation","ur mom lamo", "the seventh seraph cqc 12"};
    private final AudioPlayerManager playerManager;
    private final Map<Long,GuildMusicManager> musicManagerList;
    private final YoutubeSearchHandler searchHandler = new YoutubeSearchHandler();

    public MusicHandler() throws IOException {
        this.name = "music";
        this.musicManagerList = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        this.commands.add("play");
        this.commands.add("loop");
        this.commands.add("np");
        this.commands.add("nowplaying");
        this.commands.add("skip");
        this.commands.add("replay");
        this.commands.add("rewind");
        this.commands.add("stop");
        this.commands.add("seek");
        this.commands.add("cut");
        AudioSourceManagers.registerRemoteSources(playerManager);
    }
    private synchronized GuildMusicManager getGuildManager(Guild guild){
        long guildID = Long.parseLong(guild.getId());
        GuildMusicManager guildManager = musicManagerList.get(guildID);
        if(guildManager == null){
            //Creates a guild manager if the current guild doesn't have one
            guildManager = new GuildMusicManager(playerManager,guild.getAudioManager());
            musicManagerList.put(guildID,guildManager);
        }
        guild.getAudioManager().setSendingHandler(guildManager.getSendHandler());

        return guildManager;
    }
    public void HandleCommand(MessageReceivedEvent e, List<String> _args){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < _args.size(); i++) {
            sb.append(_args.get(i));
            sb.append(" ");
        }
        String query = sb.toString();
        Message msg = e.getMessage();
        switch(_args.get(0)){
            case "play":
                HandleRequest(msg,query,false);
                break;
            case "loop":
                HandleRequest(msg,query,true);
                break;
            case "np":
            case "nowplaying":
                NowPlaying(msg);
                break;
            case "skip":
                Skip(msg.getTextChannel());
                break;
            case "replay":
            case "rewind":
                Replay(msg);
                break;
            case "stop":
                Stop(msg.getGuild());
                break;
            case "seek":
                query = query.trim();
                System.out.println("Query = "  + query);
                if(!query.matches("^[0-9:]+$")) {
                    msg.getChannel().sendMessage("Please input time the the format HH:MM:SS").queue();
                    //return;
                }
                long milliseconds = 0;
                if(!query.contains(":")){
                    milliseconds = Long.parseLong(query) * 1000L;
                }else{
                    long hours = 0;
                    long minutes;
                    long seconds;
                    String[] values = query.split(":");
                    System.out.println(Arrays.toString(values));
                    if(values.length == 2){
                        minutes = Long.parseLong(values[0]);
                        seconds = Long.parseLong(values[1]);
                    }else if (values.length == 3){
                        hours = Long.parseLong(values[0]);
                        minutes = Long.parseLong(values[1]);
                        seconds = Long.parseLong(values[2]);
                    }else{
                        msg.getChannel().sendMessage("da fuq is this time").queue();
                        return;
                    }
                    milliseconds = TimeUnit.SECONDS.toMillis(seconds);
                    milliseconds += TimeUnit.MINUTES.toMillis(minutes);
                    milliseconds += TimeUnit.HOURS.toMillis(hours);
                    Seek(milliseconds,msg.getTextChannel());
                    break;
                }
            case "cut":
                int i;
                System.out.println(query);
                try {
                    i = Integer.parseInt(query);
                }catch(Exception exception){
                    msg.getChannel().sendMessage("Whoops, I didn't get an index number").queue();
                    break;
                }
                Cut(msg,i);
                break;
            default:
        }

    }
    public void HandleRequest(final Message msg, final String query, final boolean loop){

        //Check if user is currently in a voice channel
        if(!msg.getMember().getVoiceState().inVoiceChannel()){
            msg.getChannel().sendMessage("You have to be in a channel").queue();
            return;
        }

        GuildMusicManager musicManager = getGuildManager(msg.getGuild()); //Music manager for the guild the request originated from.
        String address = null; //Variable holding the address of the desired youtube video.
        Message tempMsg = null; //The "loading" message
        String thumb = null; //Thumbnail of the desired youtube video.

        //Check if the user specified a specific youtube link
        if(YoutubeSearchHandler.ValidateYTUrl(query)){
            System.out.println("Direct");
            RestAction<Message> ra = msg.getChannel().sendMessage("Fetching audio from YouTube URL...");
            tempMsg = ra.complete();
            address = query.trim();
        }else{ //If it is just a keyword
            SearchResult res = YoutubeSearchHandler.Search(query);
            if(res == null){
                msg.getChannel().sendMessage("No results found.").queue();
                return;
            }
            address = res.getId().getVideoId();
            thumb = res.getSnippet().getThumbnails().getDefault().getUrl();
            System.out.println(thumb);
        }
        //Prints address to console
        System.out.println("Address = \"" + address + '"');
        //Deletes loading message
        if(tempMsg != null) tempMsg.delete().queue();
        String finalThumb = thumb;
        String finalAddress = address;
        playerManager.loadItem(address, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) { //If user specified a track
                AudioTrackInfo info = track.getInfo();
                String duration = ConvertTime(track.getDuration());
                EmbedBuilder eb = new EmbedBuilder()
                        .addField("Song author", info.author,true)
                        .addField("Song length", duration,true)
                        .addField("Requested By", msg.getAuthor().getName(),true)
                        .addField("Queue position", String.valueOf(musicManager.scheduler.queue.size()),false);
                int broughtToYouBuyIndex = (int) Math.round(Math.random()* broughtToYouBy.length-1);
                System.out.println(broughtToYouBuyIndex);
                try {
                    eb.setFooter("Brought to you by " + broughtToYouBy[broughtToYouBuyIndex]);
                }catch(ArrayIndexOutOfBoundsException e){
                    eb.setFooter("Nick is gay");
                }
                if(!loop){
                    eb.setTitle("Track added to mixtape: "+ info.title);
                }else{
                    eb.setTitle("Track looping: "+ info.title);
                }
                if(finalThumb != null) eb.setThumbnail(finalThumb);
                msg.getChannel().sendMessage(eb.build()).queue();
                Play(msg,musicManager,track, loop);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack track = playlist.getTracks().get(0);
                AudioTrackInfo info = track.getInfo();
                String duration = ConvertTime(track.getDuration());
                EmbedBuilder eb = new EmbedBuilder()
                        .addField("Song author", info.author,true)
                        .addField("Song length", duration,true)
                        .addField("Requested By", msg.getAuthor().getName(),true)
                        .addField("Queue position", String.valueOf(musicManager.scheduler.queue.size()),false);
                int broughtToYouBuyIndex = (int) Math.round(Math.random()* broughtToYouBy.length-1);
                System.out.println(broughtToYouBuyIndex);
                try {
                    eb.setFooter("Brought to you by " + broughtToYouBy[broughtToYouBuyIndex]);
                }catch(ArrayIndexOutOfBoundsException e){
                    eb.setFooter("Nick is gay");
                }
                if(!loop){
                    eb.setTitle("Track added to mixtape: "+ info.title);
                }else{
                    eb.setTitle("Track looping: "+ info.title);
                }
                if(finalThumb != null) eb.setThumbnail(finalThumb);
                msg.getChannel().sendMessage(eb.build()).queue();
                Play(msg,musicManager,track, loop);
            }
            @Override
            public void noMatches() { //If no matches were found
                    System.out.println(finalAddress);
                msg.getChannel().sendMessage("No results found. Did you misspell something? (You probably did)").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) { //Any other error
                msg.getChannel().sendMessage("Error code: "+exception.getMessage()).queue();
            }
        });
    }
    //Plays song or adds song to queue if song is already playing
    private void Play(Message msg, GuildMusicManager musicManager, AudioTrack track, boolean loop){
        ConnectToVC(msg.getGuild().getAudioManager(),msg); //Connects to voice channel
        musicManager.scheduler.Queue(track, loop); //Adds track to queue
    }
    //Plays a song and forces it to front of the queue
    private void ForcePlay(GuildMusicManager musicManager, AudioTrack track, boolean loop){
        musicManager.scheduler.ForcePlay(track,loop);
    }
    //Skips current song and plays the next one in the queue.
    public void Skip(TextChannel channel){
        GuildMusicManager guildManager = getGuildManager(channel.getGuild());
        if(guildManager.scheduler.NextTrack()){
            channel.sendMessage("Current track cast into the shadow realm.  Now playing next track.").queue();
        }else{ //If no songs are left in queue
            System.out.println("No songs left");
            Stop(channel.getGuild());
        }
    }
    //Replays the currently playing song, or plays the last playing song
    public void Replay(Message message){
        ConnectToVC(message.getGuild().getAudioManager(),message);
        TrackScheduler scheduler = getGuildManager(message.getGuild()).scheduler;
        GuildMusicManager guildManager = getGuildManager(message.getGuild());
        AudioTrack track = scheduler.player.getPlayingTrack();
        if(track == null) track = scheduler.lastTrack;
        System.out.println(track);
        if(track != null){
            ForcePlay(guildManager,track.makeClone(),false);
            message.getChannel().sendMessage("Repeating last track.  Now playing "+track.getInfo().title).queue();
        }else{
            message.getChannel().sendMessage("No tracks have been played.").queue();
        }
    }
    //Stops playback, and disconnects from the voice channel
    public void Stop(Guild guild){
        AudioManager localManager = guild.getAudioManager();
        GuildMusicManager guildManager = getGuildManager(guild);
        localManager.closeAudioConnection();
        guildManager.player.stopTrack();
        guildManager.scheduler.ClearQueue();

    }
    //Seeks currently playing song to a specific point, specified in milliseconds
    public void Seek(Long timeInMillis, TextChannel channel){
        GuildMusicManager guildManager = getGuildManager(channel.getGuild());
        guildManager.scheduler.Seek(timeInMillis);
    }
    //Sends a message to the original channel showing the current queue. Returns false if no songs are in queue.
    public boolean NowPlaying(Message msg){
        TextChannel channel = msg.getTextChannel();
        TrackScheduler scheduler = getGuildManager(channel.getGuild()).scheduler;
        AudioTrack nowPlaying = scheduler.player.getPlayingTrack();
        if(nowPlaying == null){
            channel.sendMessage("Nothing is currently playing.").queue();
            return false;
        }
        List<AudioTrack> traxx = new ArrayList<AudioTrack>();
        traxx.add(nowPlaying);
        for (AudioTrack track:scheduler.getQueue()) {
            traxx.add(track);
        }
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Now Playing: ")
                .setFooter("Requested by " + msg.getAuthor().getName())
                .addField("Currently Playing: ",traxx.get(0).getInfo().title,false);
        for(int i = 1; i < traxx.size(); i++){
            eb.addField("Queue position " + i + ":" ,traxx.get(i).getInfo().title,false);
        }
        channel.sendMessage(eb.build()).queue();
        return true;
    }

    //Returns true if a song is currently playing.  False if not.
    public boolean IsPlaying(Guild guild){
        if(getGuildManager(guild).scheduler.player.getPlayingTrack() == null) return false;
        return true;
    }

    //Removes song from queue at index.
    public void Cut(Message msg, int index){
        getGuildManager(msg.getGuild()).scheduler.Cut(index);
        return;
    }
    //Connects to voicechannel
    private static void ConnectToVC(AudioManager audioManager, Message msg){
        if(!audioManager.isConnected() && !audioManager.isAttemptingToConnect()){
            VoiceChannel vs = msg.getMember().getVoiceState().getChannel();
            audioManager.openAudioConnection(vs);
        }
    }

    public VoiceChannel getVC(Guild guild){
        AudioManager audioManager = guild.getAudioManager();
        if (!audioManager.isConnected()){
            return null;
        } else{
            return audioManager.getConnectedChannel();
        }
    }
    //Converts time from milliseconds -> hh:mm:ss
    private static String ConvertTime(Long input){
        Long hours = TimeUnit.MILLISECONDS.toHours(input);
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(input - TimeUnit.HOURS.toMillis(hours));
        Long seconds = (TimeUnit.MILLISECONDS.toSeconds(input-TimeUnit.MINUTES.toMillis(minutes)));
        StringBuilder sb = new StringBuilder();
        if(hours != 0){
            sb.append(hours);
            sb.append(":");
        }
        if(minutes != 0){
            sb.append(minutes);
        }else{
            sb.append("00");
        }
        sb.append(":");
        if(seconds < 10){
            sb.append("0"+ seconds);
        }else if(seconds != 0){
            sb.append(seconds);
        }
        else{
            sb.append("00");
        }
        return(sb.toString());
    }


}
