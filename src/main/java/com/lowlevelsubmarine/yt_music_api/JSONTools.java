package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONObject;

public class JSONTools {

    public static String convertRuns(JSONObject parent) {
        return parent.getJSONArray("runs").getJSONObject(0).getString("text");
    }

    public static String flexColumnToString(JSONObject flexColumn) {
        return JSONTools.convertRuns(flexColumn
                .getJSONObject("musicResponsiveListItemFlexColumnRenderer")
                .getJSONObject("text"));
    }

}
