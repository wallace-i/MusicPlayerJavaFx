package com.iandw.musicplayerjavafx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserSettings {
    private String rootMusicDirectoryString;
    private String themeFileNameString;

    public UserSettings() {
        JSONArray jsonArray = SettingsFileIO.jsonFileInput();
        jsonArray.forEach( settings -> parseSettingsObject( (JSONObject) settings));

        System.out.println(rootMusicDirectoryString);
        System.out.println(themeFileNameString);

    }

    private void parseSettingsObject(JSONObject settings) {
        JSONObject settingObject = (JSONObject) settings.get("userSettings");
        rootMusicDirectoryString = (String) settingObject.get("musicLibrary");
        themeFileNameString = (String) settingObject.get("themeFileName");
    }

    public void setRootMusicDirectoryString(String rootMusicDirectoryString) { this.rootMusicDirectoryString = rootMusicDirectoryString; }
    public void setThemeFileNameString(String themeFileNameString) { this.themeFileNameString = themeFileNameString; }

    public String getRootMusicDirectoryString() { return rootMusicDirectoryString; }
    public String getThemeFileNameString() { return themeFileNameString; }
}
