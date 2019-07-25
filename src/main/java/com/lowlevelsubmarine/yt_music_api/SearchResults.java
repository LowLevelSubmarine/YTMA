package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class SearchResults {

    private final YTMA ytma;
    private final LinkedList<Song> songs = new LinkedList<>();
    private final LinkedList<Video> videos = new LinkedList<>();
    private String songParams;
    private String videoParams;
    private String query;

    SearchResults(YTMA ytma, String query) {
        this.ytma = ytma;
        try {
            this.query = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            URI uri = buildURI(this.ytma.getKey());
            HttpURLConnection con = this.ytma.getHttpManager().getConnection(uri.toURL());
            con.setRequestProperty(Statics.HTTP_HEADER_REFERER, Statics.REFERER_PATH);
            String postContent = new Context().setQuery(query).toString();
            long start = System.currentTimeMillis();
            String response = this.ytma.getHttpManager().executePost(con, postContent);
            System.out.println(System.currentTimeMillis() - start + "ms search fetching time");
            parseJSON(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String raw) {
        JSONObject json = new JSONObject(raw);
        JSONArray contents = json.getJSONObject("contents")
                .getJSONObject("sectionListRenderer")
                .getJSONArray("contents");
        for (int i = 0; i < contents.length(); i++) {
            JSONObject item = contents.getJSONObject(i);
            if (item.has("musicShelfRenderer")) {
                JSONObject shelfRenderer = item.getJSONObject("musicShelfRenderer");
                String category = JSONTools.convertRuns(shelfRenderer.getJSONObject("title"));
                JSONArray shelfContents = shelfRenderer.getJSONArray("contents");
                if (category.equals("Songs")) {
                    for (int a = 0; a < shelfContents.length(); a++) {
                        this.songs.add(new Song(shelfContents.getJSONObject(a).getJSONObject("musicResponsiveListItemRenderer"), true));
                    }
                } else if (category.equals("Videos")) {
                    for (int a = 0; a < shelfContents.length(); a++) {
                        this.videos.add(new Video(shelfContents.getJSONObject(a).getJSONObject("musicResponsiveListItemRenderer"), true));
                    }
                }
            }
        }
        JSONArray headerChips = json
                .getJSONObject("header")
                .getJSONObject("musicHeaderRenderer")
                .getJSONObject("header")
                .getJSONObject("chipCloudRenderer")
                .getJSONArray("chips");
        for (int i = 0; i < headerChips.length(); i++) {
            JSONObject chipRenderer = headerChips.getJSONObject(i).getJSONObject("chipCloudChipRenderer");
            String category = JSONTools.convertRuns(chipRenderer.getJSONObject("text"));
            String token = chipRenderer.getJSONObject("navigationEndpoint").getJSONObject("searchEndpoint").getString("params");
            if (category.equals("Songs")) {
                this.songParams = token;
            } else if (category.equals("Videos")) {
                this.videoParams = token;
            }
        }
    }

    private URI buildURI(String key) {
        try {
            return new URI(Statics.SEARCH_ENDPOINT_PATH + "?alt=json&key=" + key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getQuery() {
        return this.query;
    }

    public LinkedList<Song> getSongs() {
        return this.songs;
    }

    public LinkedList<Video> getVideos() {
        return this.videos;
    }

    public SongResult fetchSongResults() {
        return new SongResult(this.ytma, this.query, this.songParams);
    }

    public VideoResults fetchVideoResults() {
        return new VideoResults(this.ytma, this.query, this.videoParams);
    }

}
