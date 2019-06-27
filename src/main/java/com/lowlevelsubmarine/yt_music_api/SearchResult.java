package com.lowlevelsubmarine.yt_music_api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.LinkedList;

public class SearchResult {

    private static final String ENDPOINT_PATH = "https://music.youtube.com/youtubei/v1/search";
    private static final String REFERER_PATH = "https://music.youtube.com/";

    private final YTMA ytma;
    private final LinkedList<Track> songs = new LinkedList<>();
    private final LinkedList<Video> videos = new LinkedList<>();

    SearchResult(YTMA ytma, String query) {
        this.ytma = ytma;
        try {
            URI uri = buildURI(this.ytma.getKey());
            HttpPost post = new HttpPost(uri);
            post.setHeader(Statics.HTTP_HEADER_REFERER, REFERER_PATH);
            post.setEntity(new StringEntity(createSearchHeader(query)));
            long start = System.currentTimeMillis();
            Thread.sleep(200);
            HttpResponse response = this.ytma.getHttpManager().execute(post);
            System.out.println(System.currentTimeMillis() - start + "ms searching time");
            parseJSON(HttpTools.toHtml(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String raw) {
        JSONObject json = new JSONObject(raw);
        JSONArray list = json.getJSONObject("contents").getJSONObject("sectionListRenderer").getJSONArray("contents");
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            if (item.has("musicShelfRenderer")) {
                JSONObject shelfRenderer = item.getJSONObject("musicShelfRenderer");
                String topic = JSONTools.convertRuns(shelfRenderer.getJSONObject("title"));
                JSONArray shelfContents = shelfRenderer.getJSONArray("contents");
                if (topic.equals("Songs")) {
                    for (int a = 0; a < shelfContents.length(); a++) {
                        this.songs.add(new Track(shelfContents.getJSONObject(a).getJSONObject("musicResponsiveListItemRenderer")));
                    }
                } else if (topic.equals("Videos")) {
                    for (int a = 0; a < shelfContents.length(); a++) {
                        this.videos.add(new Video(shelfContents.getJSONObject(a).getJSONObject("musicResponsiveListItemRenderer")));
                    }
                }
            }
        }
    }

    private URI buildURI(String key) {
        try {
            return new URIBuilder(ENDPOINT_PATH)
                    .addParameter("alt", "json")
                    .addParameter("key", key).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createSearchHeader(String query) {
        JSONObject json = new JSONObject();
        json.put("query", query);
        json.put("context", createContext());
        return json.toString();
    }

    private JSONObject createContext() {
        JSONObject json = new JSONObject();
        json.put("client", createClient());
        return json;
    }

    private JSONObject createClient() {
        JSONObject json = new JSONObject();
        json.put("clientName", "WEB_REMIX");
        json.put("clientVersion", "0.1");
        //json.put("experimentIds", JSONObject.NULL);
        //json.put("gl", "US");
        //json.put("hl", "en");
        return json;
    }

    public LinkedList<Track> getSongs() {
        return this.songs;
    }

}
