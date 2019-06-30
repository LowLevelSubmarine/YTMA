package com.lowlevelsubmarine.yt_music_api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.LinkedList;

public class SongResult {

    private final YTMA ytma;
    private final LinkedList<Song> songs = new LinkedList<>();

    public SongResult(YTMA ytma, String query, String params) {
        this.ytma = ytma;
        try {
            URI uri = buildURI(this.ytma.getKey());
            HttpPost post = new HttpPost(uri);
            post.setHeader(Statics.HTTP_HEADER_REFERER, Statics.REFERER_PATH);
            post.setEntity(new StringEntity(new Context().setQuery(query).setParams(params).toString()));
            long start = System.currentTimeMillis();
            //Thread.sleep(200);
            HttpResponse response = this.ytma.getHttpManager().execute(post);
            System.out.println(System.currentTimeMillis() - start + "ms song fetching time");
            parseJSON(HttpTools.toHtml(response));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseJSON(String raw) {
        JSONObject json = new JSONObject(raw);
        JSONArray contents = json.getJSONObject("contents")
                .getJSONObject("sectionListRenderer")
                .getJSONArray("contents")
                /*.getJSONObject(0)
                .getJSONObject("musicShelfRenderer")
                .getJSONArray("contents")*/;
        for (int i = 0; i < contents.length(); i++) {
            JSONObject item = contents.getJSONObject(i);
            if (item.has("musicShelfRenderer")) {
                JSONArray shelfContents = item.getJSONObject("musicShelfRenderer").getJSONArray("contents");
                for (int a = 0; a < shelfContents.length(); a++) {
                    this.songs.add(new Song(shelfContents.getJSONObject(a).getJSONObject("musicResponsiveListItemRenderer"), false));
                }
            }
        }
    }

    private URI buildURI(String key) {
        try {
            return new URIBuilder(Statics.SEARCH_ENDPOINT_PATH)
                    .addParameter("alt", "json")
                    .addParameter("key", key).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedList<Song> getSongs() {
        return this.songs;
    }

}
