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
    private Config readConfig;
    private Config finalConfig;
    private final Gson gson = new GsonBuilder().create();
    private final Path jsonPath;


    ConfigLoader(Path _jsonPath) throws IOException {
        System.out.println(_jsonPath.toString());
        jsonPath = _jsonPath;
        try{
            FileReader reader = new FileReader(jsonPath.toString());
            readConfig = gson.fromJson(reader, Config.class);
            finalConfig = createNewConfig(readConfig.discordToken, readConfig.ytApiToken, readConfig.prefix, readConfig.ignoredMessage, readConfig.btybMessages, readConfig.guilds);
        } catch(FileNotFoundException e){
            System.out.println("Config file not found.  Creating new config.");
            finalConfig = createNewConfig(null, null, null, null, null, null);
        } catch(JsonSyntaxException e){
            System.out.println("Config file is different than expected.  Creating new config.");
            finalConfig = createNewConfig(null, null, null, null, null, null);
        }


    }
    public Config build(){
        return finalConfig;
    }

    public Config createNewConfig(String _discordToken, String _musicToken, String _prefix, String _ignoreMessage, List<String> _btybMsg, HashMap<String, Config.Guild> guilds) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String discordToken;
        String ytApiToken;
        String prefix;
        String ignoredMessage;
        List<String> btybMessages;
        if(_discordToken != null){
            discordToken = _discordToken;
        } else{
            System.out.println("Input Discord Token:");
            discordToken = scanner.nextLine();
        }

        if(_musicToken != null){
            ytApiToken = _musicToken;
        } else{
            System.out.println("Input Youtube API Token:");
            ytApiToken = scanner.nextLine();
        }

        if(_prefix != null){
            prefix = _prefix;
        } else{
            System.out.println("Input Prefix:");
            prefix = scanner.nextLine();
        }

        if(_ignoreMessage != null){
            ignoredMessage = _ignoreMessage;
        } else{
            System.out.println("Input message sent to ignored users:");
            ignoredMessage = scanner.nextLine();
        }

        if(_btybMsg != null){
            btybMessages = _btybMsg;
        } else{
            System.out.println("Input \"Brought to you by \" messages. Type \"_exit\" to exit.  10 message maximum.");
            btybMessages = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                String in = scanner.nextLine();
                if(in.equals("_exit")) break;
                btybMessages.add(in);
            }
        }
        Config config = new Config(discordToken, ytApiToken, prefix, ignoredMessage, btybMessages);
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

    public void updateConfig(Config config){
        try{
            FileWriter writer = new FileWriter(jsonPath.toString());
            gson.toJson(config, writer);
            writer.flush();
            writer.close();

        } catch(IOException err){
            err.printStackTrace();
        }
    }
}
