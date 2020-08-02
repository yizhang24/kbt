package kiwibot;

import kiwibot.Commands.StatusCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static JDABuilder jda;
    public static JDA client;
    public static String prefix = "!";
    public static Path parentPath;
    public static StatusHelper statusHandler = new StatusHelper();
    public static CommandHelper commandHelper = new CommandHelper();
    public static void main(String[] args) throws IOException, LoginException {

        //Gets parent dir, where settings and the token are located
        parentPath = Paths.get(System.getProperty("user.dir")).getParent();
        prefix = "!";

        //Gets the Discord API token from token.txt
        Path tokenPath = Paths.get(parentPath.toString(),"token.txt");
        String token = new String(Files.readAllBytes(tokenPath));
        System.out.println(token);

        jda = JDABuilder.createDefault(token);

        jda.addEventListeners(commandHelper.registerCommand(new StatusCommand(prefix)));

        client = jda.build();

    }
    public void ChangeStatus(OnlineStatus _status){
        client.getPresence().setPresence(_status,false);
    }

}
