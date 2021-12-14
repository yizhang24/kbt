package kbt.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.typesafe.config.ConfigException;

import kbt.Main;

public class YoutubeSearcher {
    private String API_KEY;
    private static YouTube youTube;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    
    public YoutubeSearcher() {
        API_KEY = getToken();
        youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {

            }
        }).setApplicationName("kbt").build();
    }

    public SearchResult search(String query) {
        YouTube.Search.List search;
        try {
            search = youTube.search().list(List.of("snippet", "id"))
                    .setKey(API_KEY)
                    .setQ(query)
                    .setType(List.of("video"))
                    .setMaxResults(1L)
                    .setFields("items(id/videoId,snippet)");
            ArrayList res = (ArrayList) search.execute().get("items");
            if (res.size() == 0) {
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

    public static boolean validateURL(String query) {
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!query.isEmpty() && query.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    private static String getToken() {
        if (Main.config != null && Main.config.hasPath("yttoken")) {
            return Main.config.getString("yttoken");
        } else {
            Main.configLoader.configWriter.writeYTToken();
            Main.configLoader.loadConfig();
            return Main.config.getString("yttoken");
        }
    }
}