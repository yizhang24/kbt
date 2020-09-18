package kiwibot;

import kiwibot.Commands.CancelCommand;
import kiwibot.Commands.IgnoreCommand;
import kiwibot.Commands.StatusCommand;
import kiwibot.Commands.VoteCommand;
import kiwibot.Music.MusicHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static JDABuilder jda;
    public static JDA client;
    public static String prefix = "owo ";
    public static Path parentPath;
    public static StatusHelper statusHandler = new StatusHelper();
    public static CommandHelper commandHelper = new CommandHelper();
    public static void main(String[] args) throws IOException, LoginException {

        //Gets parent dir, where settings and the token are located
        parentPath = Paths.get(System.getProperty("user.dir")).getParent();
        prefix = "owo ";

        //Gets the Discord API token from token.txt
        Path tokenPath = Paths.get(parentPath.toString(),"token.txt");
        String token = new String(Files.readAllBytes(tokenPath));
        System.out.println(token);

        jda = JDABuilder.createDefault(token);

        jda.addEventListeners(commandHelper.registerCommand(new StatusCommand(prefix)));
        jda.addEventListeners(commandHelper.registerCommand(new IgnoreCommand(prefix)));
        jda.addEventListeners(commandHelper.registerCommand(new CancelCommand(prefix)));
        jda.addEventListeners(commandHelper.registerCommand(new VoteCommand(prefix)));
        jda.addEventListeners(new MusicHandler());
        jda.setActivity(Activity.watching("Ray think that golden weed is funny. (It's not)"));
        client = jda.build();

    }
    public void ChangeStatus(OnlineStatus _status){
        client.getPresence().setPresence(_status,false);
    }

}
