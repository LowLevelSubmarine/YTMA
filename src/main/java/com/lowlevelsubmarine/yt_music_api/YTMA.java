package com.lowlevelsubmarine.yt_music_api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YTMA {

    private static final Pattern PAT_YTCFG_JSON = Pattern.compile("<script >ytcfg.set\\(([\\W\\w]*)\\);</script>");
    private static final String DOMAIN = "https://music.youtube.com";

    private String key;
    private final HttpManager httpManager = new HttpManager();

    public YTMA() {

    }

    HttpManager getHttpManager() {
        return this.httpManager;
    }

    public SearchResult search(String query) {
        return new SearchResult(this, query);
    }

    public void prepare() {
        getKey();
    }

    String getKey() {
        if (this.key == null) {
            try {
                URL url = new URL(DOMAIN);
                long start = System.currentTimeMillis();
                HttpURLConnection con = this.httpManager.getConnection(url);
                String response = this.httpManager.executeGet(con);
                Matcher htmlMatcher = PAT_YTCFG_JSON.matcher(response);
                if (htmlMatcher.find()) {
                    JSONObject json = new JSONObject(htmlMatcher.group(1));
                    System.out.println(System.currentTimeMillis() - start + "ms key fetching time");
                    this.key = json.getString("INNERTUBE_API_KEY");
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.key;
    }

}
