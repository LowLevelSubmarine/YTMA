package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Song {

    private final String id;
    private final String title;
    private final String artist;
    private String album;
    private final long duration;

    Song(JSONObject obj, boolean containsCategory) {
        this.id = obj.getJSONObject("doubleTapCommand").getJSONObject("watchEndpoint").getString("videoId");
        JSONArray flexColumns = obj.getJSONArray("flexColumns");
        this.title = JSONTools.flexColumnToString(flexColumns.getJSONObject(0));
        if (containsCategory) {
            this.artist = JSONTools.flexColumnToString(flexColumns.getJSONObject(2));
            this.album = JSONTools.flexColumnToString(flexColumns.getJSONObject(3));
            this.duration = FormatTools.durationTextToMillis(JSONTools.flexColumnToString(flexColumns.getJSONObject(4)));
        } else {
            this.artist = JSONTools.flexColumnToString(flexColumns.getJSONObject(1));
            try {
                this.album = JSONTools.flexColumnToString(flexColumns.getJSONObject(2));
            } catch (JSONException e) {
                this.album = null;
                System.out.println("Error: " + this.title + " #" + this.id);
            }
            this.duration = FormatTools.durationTextToMillis(JSONTools.flexColumnToString(flexColumns.getJSONObject(3)));
        }
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
