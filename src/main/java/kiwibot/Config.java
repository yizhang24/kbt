package kiwibot;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {
    public String discordToken;
    public String ytApiToken;
    public String prefix;
    public String ignoredMessage;
    public List<String> btybMessages;
    public HashMap<String, Guild> guilds = new HashMap<>();

    Config(String _discordToken, String _ytApiToken, String _prefix, String _ignoreMessage, List<String> _btybMessage){
        discordToken = _discordToken;
        ytApiToken = _ytApiToken;
        prefix = _prefix;
        ignoredMessage = _ignoreMessage;
        btybMessages = _btybMessage;


    }

    public void addGuilds(){
        if (guilds == null){
            guilds = new HashMap<>();
        }
        List<net.dv8tion.jda.api.entities.Guild> _guilds = Main.client.getGuilds();
        for (net.dv8tion.jda.api.entities.Guild guild:_guilds) {
            guilds.put(guild.getId(), new Guild(guild));
        }
    }

    public void setDJRole(String _guildID, String _djRoleID) throws NullPointerException{
        guilds.get(_guildID).djRoleID = _djRoleID;
    }

    public class Guild{
        public String id;
        public String name;
        public String djRoleID;

        Guild(net.dv8tion.jda.api.entities.Guild _guild){
            this.id = _guild.getId();
            this.name = _guild.getName();
        }
    }
}
