package kbt.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import kbt.Constants;

public class ConfigWriter {

    private Scanner scanner = new Scanner(System.in);
    
    public Config createConfig(){
        HashMap<String, Object> values = new HashMap<String, Object>();
            
        System.out.println(Constants.GREEN + "Core Config file not found! Creating new config..." + Constants.RESET);
        System.out.println(Constants.GREEN + "Enter Discord Api Token (You can get an api token at " + Constants.CYAN + "https://discord.com/developers/applications" + Constants.GREEN + ")" + Constants.RESET);

        String token;

        try {
            token = scanner.nextLine();
            values.put("discordtoken", token);
            //scanner.close();
        } catch (NoSuchElementException e1) {
            e1.printStackTrace();
        }
        
        Config config = ConfigFactory.parseMap(values);
        writeConfig(config);
        return config;
    } 

    public void writeConfig(Config config){
        try {
            FileWriter writer;
            File configFile = Constants.CORE_CONF_PATH.toFile();
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdir();
            }
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            writer = new FileWriter(Constants.CORE_CONF_PATH.toFile());
            writer.write(config.root().render(ConfigRenderOptions.concise().setFormatted(true)));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
