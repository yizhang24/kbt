package kiwibot.Config;

import kiwibot.Main;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigTemplate {
    public static String discordToken;
    public static String ytApiToken;
    public static String ignoredMessage;
    public static List<String> btybMessages;
    public static HashMap<String, kiwibot.Config.Guild> guilds = new HashMap<>();

    ConfigTemplate(String _discordToken, String _ytApiToken, String _ignoreMessage, List<String> _btybMessage){
        discordToken = _discordToken;
        ytApiToken = _ytApiToken;
        ignoredMessage = _ignoreMessage;
        btybMessages = _btybMessage;


    }

    public static void addGuilds(){
        if (guilds == null){
            guilds = new HashMap<>();
        }
        List<net.dv8tion.jda.api.entities.Guild> _guilds = Main.client.getGuilds();
        for (net.dv8tion.jda.api.entities.Guild guild:_guilds) {
            if(!guilds.containsKey(guild.getId())) guilds.put(guild.getId(), new Guild(guild));
        }
    }

    public static void setDJRole(String _guildID, String _djRoleID) throws NullPointerException{
        guilds.get(_guildID).djRoleID = _djRoleID;
    }

    public static Guild getGuildData(String guildID){
        if(guilds.containsKey(guildID)) return guilds.get(guildID);
        else{
            System.out.println("Config.java: No guild found with id " + guildID);
            return null;
        }
    }

    public static void setPrefix(String guildID, String _prefix){
        if(guilds.containsKey(guildID)){
            guilds.get(guildID).prefix = _prefix;
        }
    }
}
