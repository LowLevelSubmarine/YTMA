package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Song {

    private final String id;
    private final String title;
    private final String artist;
    private final String album;
    private final Cover cover;
    private final long duration;

    Song(JSONObject json, boolean hasCategoryField) {
        this.id = json.getJSONObject("doubleTapCommand").getJSONObject("watchEndpoint").getString("videoId");
        JSONArray flexColumns = json.getJSONArray("flexColumns");
        this.title = JSONTools.flexColumnToString(flexColumns.getJSONObject(0));
        String[] fields = new String[4];
        try {
            for (int i = 0; i < 4; i++) {
                fields[i] = JSONTools.flexColumnToString(flexColumns.getJSONObject(i + 1));
            }
        } catch (JSONException e) { /*Leave at null*/ }
        int shift = hasCategoryField ? 0 : -1;
        this.artist = fields[1+shift];
        this.album = fields[2+shift];
        this.duration = FormatTools.durationTextToMillis(fields[3+shift]);
        this.cover = new Cover(json.getJSONObject("thumbnail")
                .getJSONObject("musicThumbnailRenderer")
                .getJSONObject("thumbnail")
                .getJSONArray("thumbnails"));
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getDuration() {
        return duration;
    }

    public Cover getCover() {
        return this.cover;
    }
    
}
