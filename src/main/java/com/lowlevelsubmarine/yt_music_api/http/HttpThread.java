package com.lowlevelsubmarine.yt_music_api.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by amitaymolko on 2/16/16.
 */

public class HttpThread extends Thread {

    private final HttpRequest request;

    public HttpThread(HttpRequest request) {
        this.request =request;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        HttpResponse response = new HttpResponse();
        try {
            if (request == null || request.getURL() == null || request.getMethod() == null) {
                throw new Exception();
            }
            url = new URL(request.getURL());
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod(request.getMethodString());

            if (request.getHeaders() != null) {
                for (HashMap.Entry<String, String> pair : request.getHeaders().entrySet()) {
                    urlConnection.setRequestProperty(pair.getKey(), pair.getValue());
                }
            }
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            if (request.getPostData() != null) {
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream());
                byte[] postData       = request.getPostData().getBytes();
                wr.write( postData );
            }
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            String responseString;
            if(responseCode == HttpsURLConnection.HTTP_OK){
                responseString = readStream(urlConnection.getInputStream());
            }else{
                responseString = readStream(urlConnection.getErrorStream());
            }
            response = new HttpResponse(responseCode, responseString, request.getCallback());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
        response.getCallback().onResponse(response);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}