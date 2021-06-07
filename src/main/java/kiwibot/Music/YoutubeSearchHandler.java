package kiwibot.Music;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import kiwibot.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class YoutubeSearchHandler {

    private static YouTube yt;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static String apikey;

    YoutubeSearchHandler() {
        apikey = Main.configuration.ytApiToken;
        yt = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("Not-Hamsterbot").build();
    }

    public static SearchResult Search(String query) {
        YouTube.Search.List search;
        try {
            search = yt.search().list("snippet,id")
                    .setKey(apikey)
                    .setQ(query)
                    .setType("video")
                    .setMaxResults(1L)
                    .setFields("items(id/videoId,snippet)");
            ArrayList res = (ArrayList) search.execute().get("items");
            if(res.size()==0) {
               return null;
            }
            SearchResult video = (SearchResult) res.get(0);
            return video;
        } catch (GoogleJsonResponseException e) {
            System.err.println("YoutubeSearchHandler: There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("YoutubeSearchHandler: There was an IO error: " + e.getCause() + " : " + e.getMessage());
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    public static boolean ValidateYTUrl(String query) {
        String pattern =  "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if(!query.isEmpty() && query.matches(pattern)) {
            return true;
        }else{
            return false;
        }
    }
}
