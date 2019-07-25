package com.lowlevelsubmarine.yt_music_api;

import org.json.JSONObject;

public class Context {

    private final JSONObject json;

    public Context() {
        this.json = new JSONObject();
        this.json.put("context", createContext());
    }

    public Context setVideoId(String videoId) {
        this.json.put("videoId", videoId);
        return this;
    }

    public Context setIsAudioOnly(boolean isAudioOnly) {
        this.json.put("isAudioOnly", isAudioOnly);
        return this;
    }

    public Context setTunerSettingsValue(TunerSetting setting) {
        this.json.put("tunerSettingValue", setting.getId());
        return this;
    }

    public Context setEnablePersistentPlaylistPanel(boolean enablePersistentPlaylistPanel) {
        this.json.put("enablePersistentPlaylistPanel", enablePersistentPlaylistPanel);
        return this;
    }

    public Context setQuery(String query) {
        this.json.put("query", query);
        return this;
    }

    public Context setParams(String params) {
        this.json.put("params", params);
        return this;
    }

    @Override
    public String toString() {
        return json.toString();
    }

    private JSONObject createContext() {
        JSONObject json = new JSONObject();
        json.put("client", createClient());
        return json;
    }

    private JSONObject createClient() {
        JSONObject json = new JSONObject();
        json.put("clientName", "WEB_REMIX");
        json.put("clientVersion", "0.1");
        json.put("gl", "US");
        json.put("hl", "en");
        return json;
    }

    public enum TunerSetting {

        AUTOMIX_SETTINGS_NORMAL("AUTOMIX_SETTING_NORMAL");

        private final String id;

        TunerSetting(String id) {
             this.id = id;
        }

        public String getId() {
            return this.id;
        }

    }

}
