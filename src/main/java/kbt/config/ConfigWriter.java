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
import kbt.Main;

public class ConfigWriter {

    private Scanner scanner = new Scanner(System.in);
    
    public void createConfig(){
        System.out.println(Constants.GREEN + "Core Config file not found! Creating new config..." + Constants.RESET);
        writeDiscordToken();
        writeYTToken();
    } 

    public void writeConfig(Config config) {
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
    
    public void writeDiscordToken() {
        System.out.println(Constants.GREEN + "Enter Discord Api Token (You can get an api token at " + Constants.CYAN
                + "https://discord.com/developers/applications" + Constants.GREEN + ")" + Constants.RESET);

        String token;
        HashMap<String, Object> values = new HashMap<String, Object>();

        try {
            token = scanner.nextLine();
            values.put("discordtoken", token);
        } catch (NoSuchElementException e1) {
            e1.printStackTrace();
        }

        Config newConfig;
        if(Main.config != null) {
            newConfig = ConfigFactory.parseMap(values).withFallback(Main.config);
        } else {
            newConfig = ConfigFactory.parseMap(values);
        }
        writeConfig(newConfig);
    }

    public void writeYTToken() {
        System.out.println(
                Constants.GREEN + "Enter Youtube API Token (You can get an api token at " + Constants.RED_BRIGHT
                        + "https://console.cloud.google.com/" + Constants.RED_BRIGHT + ")" + Constants.RESET);

        String token;
        HashMap<String, Object> values = new HashMap<String, Object>();

        try {
            token = scanner.nextLine();
            values.put("yttoken", token);
        } catch (NoSuchElementException e1) {
            e1.printStackTrace();
        }

        Config newConfig;
        if(Main.config != null) {
            newConfig = ConfigFactory.parseMap(values).withFallback(Main.config);
        } else {
            newConfig = ConfigFactory.parseMap(values);
        }
        writeConfig(newConfig);
    }
}
