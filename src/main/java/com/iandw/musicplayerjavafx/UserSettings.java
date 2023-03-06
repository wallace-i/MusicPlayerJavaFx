package com.iandw.musicplayerjavafx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserSettings implements Runnable {
    private String rootMusicDirectoryString;
    private String themeFileNameString;
    private boolean writeOnClose;

    public UserSettings() {}

    @Override
    public void run() {
        JSONArray jsonArray = SettingsFileIO.jsonFileInput();
        jsonArray.forEach( settings -> parseSettingsObject( (JSONObject) settings));

        System.out.println("Root Directory: " + rootMusicDirectoryString);
        System.out.println("Theme File: " + themeFileNameString);
    }

    private void parseSettingsObject(JSONObject settings) {
        JSONObject settingObject = (JSONObject) settings.get("userSettings");
        rootMusicDirectoryString = (String) settingObject.get("musicLibrary");
        themeFileNameString = (String) settingObject.get("themeFileName");
    }

    public void setRootMusicDirectoryString(String rootMusicDirectoryString) {
        this.rootMusicDirectoryString = rootMusicDirectoryString;
        writeOnClose = true;
    }
    public void setThemeFileNameString(String themeFileNameString) {
        this.themeFileNameString = themeFileNameString;
        writeOnClose = true;
    }

    public String getRootMusicDirectoryString() { return rootMusicDirectoryString; }
    public String getThemeFileNameString() { return themeFileNameString; }
    public boolean getWriteOnClose() { return writeOnClose; }
}
