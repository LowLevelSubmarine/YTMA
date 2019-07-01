package com.lowlevelsubmarine.yt_music_api.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by amitaymolko on 2/19/16.
 */
public class JsonAPI {

    public interface JsonCallback {
        void onResponse(int statusCode, JSONObject json);
    }

    private static JsonAPI ourInstance = new JsonAPI();
    private HashMap<String, String> headers = new HashMap<>();
    public static JsonAPI getInstance() {
        return ourInstance;
    }

    private JsonAPI() {
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }
    public void removeHeader(String name) {
        headers.remove(name);
    }

    public void get(String url, final JsonCallback callback) {
        HttpRequest r = new HttpRequest(HttpRequest.Method.GET, url);
        r.setHeaders(headers);
        r.setCallback(callbackForJsonCallback(callback));
        new HttpThread(r).start();
    }

    public void post(String url, JSONObject postData, JsonCallback callback) {
        HttpRequest r = new HttpRequest(HttpRequest.Method.POST, url);
        r.setHeaders(headers);
        r.setCallback(callbackForJsonCallback(callback));
        r.setPostData(postData.toString());
        new HttpThread(r).start();
    }

    private HttpRequest.RequestCallback callbackForJsonCallback(final JsonCallback jsonCallback) {
        return r -> {
            JSONObject json = null;
            try {
                json =  new JSONObject(r.getResponse());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonCallback.onResponse(r.getResponseCode(), json);
        };
    }
}