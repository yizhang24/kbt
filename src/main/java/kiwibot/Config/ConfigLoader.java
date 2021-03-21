package kiwibot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ConfigLoader {
    private ConfigTemplate readConfig;
    public ConfigTemplate finalConfig;
    private final Gson gson = new GsonBuilder().create();
    private final Path jsonPath;


    ConfigLoader(Path _jsonPath) throws IOException {
        jsonPath = _jsonPath;
        try{
            FileReader reader = new FileReader(jsonPath.toString());
            readConfig = gson.fromJson(reader, ConfigTemplate.class);
            finalConfig = createNewConfig(readConfig.discordToken, readConfig.ytApiToken, readConfig.ignoredMessage, readConfig.btybMessages, readConfig.guilds);
        } catch(FileNotFoundException e){
            System.out.println("Config Loader: Config file not found.  Creating new config.");
            finalConfig = createNewConfig(null, null, null, null, null);
        } catch(JsonSyntaxException e){
            System.out.println("Config Loader: Config file is different than expected.  Creating new config.");
            finalConfig = createNewConfig(null, null,  null, null, null);
        }


    }


    public ConfigTemplate createNewConfig(String _discordToken, String _musicToken, String _ignoreMessage, List<String> _btybMsg, HashMap<String, ConfigTemplate.Guild> guilds) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String discordToken;
        String ytApiToken;
        String ignoredMessage;
        List<String> btybMessages;
        if(_discordToken != null){
            discordToken = _discordToken;
        } else{
            System.out.println("Config Loader: Input Discord Token:");
            discordToken = scanner.nextLine();
        }

        if(_musicToken != null){
            ytApiToken = _musicToken;
        } else{
            System.out.println("Config Loader: Input Youtube API Token:");
            ytApiToken = scanner.nextLine();
        }

        if(_ignoreMessage != null){
            ignoredMessage = _ignoreMessage;
        } else{
            System.out.println("Config Loader: Input message sent to ignored users:");
            ignoredMessage = scanner.nextLine();
        }

        if(_btybMsg != null){
            btybMessages = _btybMsg;
        } else{
            System.out.println("Config Loader: Input \"Brought to you by \" messages. Type \"_exit\" to exit.  10 message maximum.");
            btybMessages = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                String in = scanner.nextLine();
                if(in.equals("_exit")) break;
                btybMessages.add(in);
            }
        }
        ConfigTemplate config = new ConfigTemplate(discordToken, ytApiToken, ignoredMessage, btybMessages);
        try{
            FileWriter writer = new FileWriter(jsonPath.toString());
            gson.toJson(config, writer);
            writer.flush();
            writer.close();

        } catch(IOException err){
            err.printStackTrace();
        }
        return config;
    }

    public void updateConfig(ConfigTemplate config){
        try{
            FileWriter writer = new FileWriter(jsonPath.toString());
            gson.toJson(config, writer);
            writer.flush();
            writer.close();

        } catch(IOException err){
            err.printStackTrace();
        }
    }
    public void updateConfig(){
        try{
            FileWriter writer = new FileWriter(jsonPath.toString());
            gson.toJson(finalConfig, writer);
            writer.flush();
            writer.close();

        } catch(IOException err){
            err.printStackTrace();
        }
    }
}
