package com.lowlevelsubmarine.yt_music_api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

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

    String getKey() {
        if (this.key == null) {
            long start = System.currentTimeMillis();
            HttpGet get = new HttpGet(DOMAIN);
            HttpResponse response = this.httpManager.execute(get);
            String html = HttpTools.toHtml(response);
            Matcher htmlMatcher = PAT_YTCFG_JSON.matcher(html);
            if (htmlMatcher.find()) {
                JSONObject jsonYtCfg = new JSONObject(htmlMatcher.group(1));
                System.out.println(System.currentTimeMillis() - start + "ms key fetching time");
                this.key = jsonYtCfg.getString("INNERTUBE_API_KEY");
            } else {
                return null;
            }
        }
        return this.key;
    }

}
