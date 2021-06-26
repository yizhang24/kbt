package kiwibot.Commands;

import java.io.IOException;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShibeCommand extends MasterCommand {

    private String url = "http://shibe.online/api/shibes?count=1&urls=true&httpsUrls=true";
    OkHttpClient client = new OkHttpClient();

    public ShibeCommand() {
        this.name = "shibe";
        commands.add("shibe");
        commands.add("doge");
    }

    public void HandleCommand(MessageReceivedEvent _e, List<String> _args) {
        switch (_args.get(0)) {
            case "doge":
            case "shibe":
            Request request = new Request.Builder().url(url).build();
            try {
                Response res = client.newCall(request).execute();
                String url = res.body().string().replace("([[\"])([\"\\]])", "");
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Here's your funny yellow dog");
                embed.setImage(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("ShibeCommand.java: No command recognized");
                break;
        }

    }
}
