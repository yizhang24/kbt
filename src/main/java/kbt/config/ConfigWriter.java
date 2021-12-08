package kbt.config;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import kbt.Constants;

public class ConfigWriter {
    
    public Config createConfig(){
        HashMap<String, Object> values = new HashMap<String, Object>();
        
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

            
        System.out.println(Constants.GREEN + "Core Config file not found! Creating new config..." + Constants.RESET);
        System.out.println(Constants.GREEN + "Enter Discord Api Token (You can get an api token at " + Constants.CYAN + "https://discord.com/developers/applications" + Constants.GREEN + ")" + Constants.RESET);

        String token;

        try {
            token = reader.readLine();
            values.put("discordtoken", token);
            reader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        Config config = ConfigFactory.parseMap(values);
        writeConfig(config);
        return config;
    } 

    public void writeConfig(Config config){
        try {
            FileWriter writer;
            writer = new FileWriter(Constants.CORE_CONF_PATH.toFile());
            writer.write(config.root().render(ConfigRenderOptions.concise().setFormatted(true)));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
