package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Song {

    private static final Pattern PATTERN_BRACKETS = Pattern.compile(" *[\\[(]([^)\\]]+)[)\\]]");

    private final String id;
    private final String title;
    private final String rawTitle;
    private final String mainArtist;
    private final LinkedList<String> artists = new LinkedList<>();
    private final String album;
    private final Cover cover;
    private final long duration;

    Song(JSONObject json, boolean hasCategoryField) {
        if (json.has("flexColumns")) {
            this.id = json.getJSONObject("doubleTapCommand").getJSONObject("watchEndpoint").getString("videoId");
            JSONArray flexColumns = json.getJSONArray("flexColumns");
            this.rawTitle = JSONTools.flexColumnToString(flexColumns.getJSONObject(0));
            String[] fields = new String[4];
            try {
                for (int i = 0; i < 4; i++) {
                    fields[i] = JSONTools.flexColumnToString(flexColumns.getJSONObject(i + 1));
                }
            } catch (JSONException e) { /*Leave at null*/ }
            int shift = hasCategoryField ? 0 : -1;
            this.mainArtist = fields[1+shift];
            this.album = fields[2+shift];
            this.duration = FormatTools.durationTextToMillis(fields[3+shift]);
            this.cover = new Cover(json.getJSONObject("thumbnail")
                    .getJSONObject("musicThumbnailRenderer")
                    .getJSONObject("thumbnail")
                    .getJSONArray("thumbnails"));
        } else {
            this.id = json.getString("videoId");
            this.rawTitle = JSONTools.convertRuns(json.getJSONObject("title"));
            this.mainArtist = JSONTools.convertRuns(json.getJSONObject("shortBylineText"));
            this.album = null;
            this.duration = FormatTools.durationTextToMillis(JSONTools.convertRuns(json.getJSONObject("lengthText")));
            this.cover = new Cover(json.getJSONObject("thumbnail").getJSONArray("thumbnails"));
        }
        this.title = removeBrackets(this.rawTitle, this::processTitleBracket);
    }

    private boolean processTitleBracket(String bracketContent) {
        String featString = "feat. ";
        if (bracketContent.startsWith(featString)) {
            bracketContent = bracketContent.substring(featString.length());
            if (bracketContent.contains(", ")) {
                for (String feat : bracketContent.split(", ")) {
                    //Only add if it isn't a duplicate
                    if (!this.artists.contains(feat)) {
                        this.artists.add(feat);
                    }
                }
            } else {
                this.artists.add(bracketContent);
            }
            return false;
        }
        return true;
    }

    private String removeBrackets(String string, BracketProcessor bracketProcessor) {
        String ret = "";
        while (!string.equals("")) {
            Matcher matcher = PATTERN_BRACKETS.matcher(string);
            if (matcher.find()) {
                String bracketContent = matcher.group(1);
                if (bracketProcessor.process(bracketContent)) {
                    ret += string.substring(0, matcher.end());
                } else {
                    ret += string.substring(0, matcher.start());
                }
                string = string.substring(matcher.end());
            } else {
                ret += string;
                string = "";
            }
        }
        return ret;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMainArtist() {
        return mainArtist;
    }

    public LinkedList<String> getArtists() {
        return this.artists;
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

    private interface BracketProcessor {
        boolean process(String bracketContent);
    }
    
}
