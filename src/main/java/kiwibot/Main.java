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
    public static StatusCommand statusCommand;
    public static IgnoreCommand ignoreCommand;
    public static CensorCommand censorCommand;
    public static VoteCommand voteCommand;
    public static RepeatCommand repeatCommand;
    public static FortniteCommand fortniteCommand;
    public static SettingsCommand settingsCommand;
    public static ShibeCommand shibeCommand;

    public static void main(String[] args) throws IOException, LoginException, InterruptedException {

        Path jsonPath = Paths.get(System.getProperty("user.dir") + "\\kiwibotconfig.json");

        configLoader = new ConfigLoader(jsonPath);
        configuration = configLoader.finalConfig;
        commandHandler = new CommandHandler();

        statusCommand = new StatusCommand();
        ignoreCommand = new IgnoreCommand();
        censorCommand = new CensorCommand();
        voteCommand = new VoteCommand();
        repeatCommand = new RepeatCommand();
        fortniteCommand = new FortniteCommand();
        settingsCommand = new SettingsCommand();
        shibeCommand = new ShibeCommand();

        try {
            musicHandler = new MusicHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }


        jda = JDABuilder.createDefault(configuration.discordToken);


        commandHelper.registerCommand(statusCommand);
        commandHelper.registerCommand(ignoreCommand);
        commandHelper.registerCommand(censorCommand);
        commandHelper.registerCommand(voteCommand);
        commandHelper.registerCommand(musicHandler);
        commandHelper.registerCommand(repeatCommand);
        commandHelper.registerCommand(fortniteCommand);
        commandHelper.registerCommand(settingsCommand);
        commandHelper.registerCommand(shibeCommand);

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
