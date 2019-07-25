package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONArray;
import org.json.JSONObject;

public class Cover {

    private String url60;
    private String url120;
    private String url226;
    private String url544;

    Cover(JSONArray thumbnails) {
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