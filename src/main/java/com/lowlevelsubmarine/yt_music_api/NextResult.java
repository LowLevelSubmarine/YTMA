package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

public class NextResult {

    private final YTMA ytma;
    private final String id;
    private final LinkedList<Song> songs = new LinkedList<>();

    public NextResult(YTMA ytma, String id) {
        this.ytma = ytma;
        this.id = id;
        try {
            URI uri = buildURI(this.ytma.getKey());
            HttpURLConnection con = this.ytma.getHttpManager().getConnection(uri.toURL());
            con.setRequestProperty(Statics.HTTP_HEADER_REFERER, Statics.REFERER_PATH);
            String postContent = new Context().setEnableAutoPlay(true).setVideoId(id).toString();
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
        JSONArray array = json.getJSONArray("text");
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
        }
    }

    private URI buildURI(String key) throws URISyntaxException {
        return new URI(Statics.NEXT_ENDPOINT_PATH + "?alt=json&key=" + key);
    }

}
