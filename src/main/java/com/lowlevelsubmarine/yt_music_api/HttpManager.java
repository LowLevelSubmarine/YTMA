package com.lowlevelsubmarine.yt_music_api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpManager {

    private final HttpClient client = HttpClientBuilder.create()
            .setUserAgent(Statics.USER_AGENT)
            .setDefaultCookieStore(new BasicCookieStore())
            .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build())
            .build();

    public HttpManager() {

    }

    public HttpResponse execute(HttpRequestBase httpRequest) {
        httpRequest.setHeader("accept-language", "en-US");
        try {
            return this.client.execute(httpRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
