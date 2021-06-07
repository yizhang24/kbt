package kiwibot.Config;

import kiwibot.Main;

import java.util.HashMap;
import java.util.List;

public class ConfigInfo {
    public static String discordToken;
    public static String ytApiToken;
    public static String ignoredMessage;
    public static List<String> btybMessages;
    public static HashMap<String, GuildInfo> guilds = new HashMap<>();

    ConfigInfo(String _discordToken, String _ytApiToken, String _ignoreMessage, List<String> _btybMessage){
        discordToken = _discordToken;
        ytApiToken = _ytApiToken;
        ignoredMessage = _ignoreMessage;
        btybMessages = _btybMessage;


    }

    public void addGuilds(){
        if (guilds == null){
            guilds = new HashMap<>();
        }
        List<net.dv8tion.jda.api.entities.Guild> _guilds = Main.client.getGuilds();
        for (net.dv8tion.jda.api.entities.Guild guild:_guilds) {
            if(!guilds.containsKey(guild.getId())) guilds.put(guild.getId(), new GuildInfo(guild));
        }
    }

    public void setDJRole(String _guildID, String _djRoleID) throws NullPointerException{
        guilds.get(_guildID).djRoleID = _djRoleID;
    }

    public GuildInfo getGuildData(String guildID){
        if(guilds.containsKey(guildID)) return guilds.get(guildID);
        else{
            System.out.println("Config.java getGuildData: No guild found with id " + guildID);
            return null;
        }
    }

    public void setGuildData(String guildID, GuildInfo data){
        if(guilds.containsKey(guildID)){
            guilds.put(guildID, data);
        }
        else{
            System.out.println("Config.java setGuildData: No guild found with id " + guildID);

        }
    }

    public void setPrefix(String guildID, String _prefix){
        if(guilds.containsKey(guildID)){
            guilds.get(guildID).prefix = _prefix;
        }
    }
}
