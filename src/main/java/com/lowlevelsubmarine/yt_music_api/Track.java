package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONObject;

public class Track {

    private final String id;
    private final String title;
    private final String artist;
    private final String album;
    private final long duration;

    Track(JSONObject obj) {
        this.id = obj.getJSONObject("doubleTapCommand").getJSONObject("watchEndpoint").getString("videoId");
        JSONArray flexColumns = obj.getJSONArray("flexColumns");
        this.title = JSONTools.flexColumnToString(flexColumns.getJSONObject(0));
        this.artist = JSONTools.flexColumnToString(flexColumns.getJSONObject(2));
        this.album = JSONTools.flexColumnToString(flexColumns.getJSONObject(3));
        this.duration = FormatTools.durationTextToMillis(JSONTools.flexColumnToString(flexColumns.getJSONObject(4)));
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
    
}
