package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONObject;

public class JSONTools {

    public static String convertRuns(JSONObject parent) {
        return parent.getJSONArray("runs").getJSONObject(0).getString("text");
    }

    public static String flexColumnToString(JSONObject flexColumn) {
        JSONObject flexColumnRenderer = flexColumn.getJSONObject("musicResponsiveListItemFlexColumnRenderer");
        if (flexColumnRenderer.has("text")) {
            return convertRuns(flexColumnRenderer.getJSONObject("text"));
        } else {
            return null;
        }
    }

}
