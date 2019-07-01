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

    Song(JSONObject obj, boolean containsCategory) {
        this.id = obj.getJSONObject("doubleTapCommand").getJSONObject("watchEndpoint").getString("videoId");
        JSONArray flexColumns = obj.getJSONArray("flexColumns");
        String durationString;
        if (containsCategory) {
            this.artist = parseFlexColumnString(flexColumns, 2);
            this.album = parseFlexColumnString(flexColumns, 3);
            durationString = parseFlexColumnString(flexColumns, 4);
        } else {
            this.artist = parseFlexColumnString(flexColumns, 1);
            this.album = parseFlexColumnString(flexColumns, 2);
            durationString = parseFlexColumnString(flexColumns, 3);
        }
        this.title = JSONTools.flexColumnToString(flexColumns.getJSONObject(0));
        if (durationString != null) {
            this.duration = FormatTools.durationTextToMillis(durationString);
        } else {
            this.duration = -1;
        }
        this.cover = new Cover(obj.getJSONObject("thumbnail")
                .getJSONObject("musicThumbnailRenderer")
                .getJSONObject("thumbnail")
                .getJSONArray("thumbnails"));
    }

    private String parseFlexColumnString(JSONArray array, int index) {
        try {
            return JSONTools.flexColumnToString(array.getJSONObject(index));
        } catch (JSONException e) {
            return null;
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

    public Cover getCover() {
        return this.cover;
    }

    public class Cover {

        private String url60;
        private String url120;
        private String url226;
        private String url544;

        private Cover(JSONArray thumbnails) {
            for (int i = 0; i < thumbnails.length(); i++) {
                try {
                    JSONObject thumbnail = thumbnails.getJSONObject(i);
                    int width = thumbnail.getInt("width");
                    String url = thumbnail.getString("url");
                    if (width == 60) {
                        this.url60 = url;
                    } else if (width == 120) {
                        this.url120 = url;
                    } else if (width == 226) {
                        this.url226 = url;
                    } else if (width == 544) {
                        this.url544 = url;
                    }
                } catch (Exception e) {
                    //Does: Skip thumbnail
                }
            }
        }

        public String getUrl60() {
            return url60;
        }

        public String getUrl120() {
            return url120;
        }

        public String getUrl226() {
            return url226;
        }

        public String getUrl544() {
            return url544;
        }

    }
    
}
