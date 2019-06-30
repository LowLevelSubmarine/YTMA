package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Video {

    private final String id;
    private final String title;
    private final String channel;
    private final int views;
    private final long duration;

    public Video(JSONObject json) {
        this.id = json.getJSONObject("doubleTapCommand").getJSONObject("watchEndpoint").getString("videoId");
        JSONArray flexColumns = json.getJSONArray("flexColumns");
        this.title = JSONTools.flexColumnToString(flexColumns.getJSONObject(0));
        this.channel = JSONTools.flexColumnToString(flexColumns.getJSONObject(2));
        this.views = parseViews(JSONTools.flexColumnToString(flexColumns.getJSONObject(3)));
        this.duration = FormatTools.durationTextToMillis(JSONTools.flexColumnToString(flexColumns.getJSONObject(4)));
    }

    private int parseViews(String raw) {
        Matcher matcher = Pattern.compile("(\\d{1,3})(\\S)* views").matcher(raw);
        if (matcher.find()) {
            int val = Integer.valueOf(matcher.group(1));
            if (matcher.group(2) != null) {
                switch (matcher.group(2)) {
                    case "B":
                        return val*1000*3;
                    case "M":
                        return val*1000*2;
                    case "K":
                        return val*1000;
                }
            } else {
                return val;
            }
        }
        return -1;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    public int getViews() {
        return views;
    }

    public long getDuration() {
        return duration;
    }

}
