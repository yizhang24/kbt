package kiwibot;

import kiwibot.Commands.*;
import kiwibot.Config.ConfigLoader;
import kiwibot.Config.ConfigInfo;
import kiwibot.Music.MusicHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static JDABuilder jda;
    public static JDA client;
    public static String prefix;
    public static CommandHelper commandHelper = new CommandHelper();
    public static CommandHandler commandHandler;
    public static MusicHandler musicHandler;
    public static ConfigInfo configuration;
    public static ConfigLoader configLoader;


    public static void main(String[] args) throws IOException, LoginException, InterruptedException {

        Path jsonPath = Paths.get(System.getProperty("user.dir") + "\\kiwibotconfig.json");

        configLoader = new ConfigLoader(jsonPath);
        configuration = configLoader.finalConfig;



        commandHandler = new CommandHandler();

        try {
            musicHandler = new MusicHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }


        jda = JDABuilder.createDefault(configuration.discordToken);


        commandHelper.registerCommand(new StatusCommand());
        commandHelper.registerCommand(new IgnoreCommand());
        commandHelper.registerCommand(new CensorCommand());
        commandHelper.registerCommand(new VoteCommand());
        commandHelper.registerCommand(musicHandler);
        commandHelper.registerCommand(new RepeatCommand());
        commandHelper.registerCommand(new FortniteCommand());
        commandHelper.registerCommand(new SettingsCommand());

        jda.addEventListeners(commandHandler);

        jda.setActivity(Activity.competing("Pokemon: Mewtwo Strikes Back Evolution"));
        client = jda.build().awaitReady();

        configuration.addGuilds();
        configLoader.updateConfig(configuration);
    }

    public void ChangeStatus(OnlineStatus _status){
        client.getPresence().setPresence(_status,false);
    }

}
