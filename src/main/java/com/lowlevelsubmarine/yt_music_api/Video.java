package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Video {

    private final String id;
    private String title;
    private String channel;
    private int views;
    private long duration;

    public Video(JSONObject json, boolean hasCategoryField) {
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
        this.channel = fields[1+shift];
        this.views = parseViews(fields[2+shift]);
        this.duration = FormatTools.durationTextToMillis(fields[3+shift]);
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
