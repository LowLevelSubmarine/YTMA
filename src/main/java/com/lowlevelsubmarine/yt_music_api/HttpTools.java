package com.lowlevelsubmarine.yt_music_api;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpTools {

    public static String toHtml(HttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
