package kiwibot;

import java.util.List;

public class Config {
    public String discordToken;
    public String ytApiToken;
    public String prefix;
    public String ignoredMessage;
    public List<String> btybMessages;

    Config(String _discordToken, String _ytApiToken, String _prefix, String _ignoreMessage, List<String> _btybMessage){
        discordToken = _discordToken;
        ytApiToken = _ytApiToken;
        prefix = _prefix;
        ignoredMessage = _ignoreMessage;
        btybMessages = _btybMessage;
    }
}
