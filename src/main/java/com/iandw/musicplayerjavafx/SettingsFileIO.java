/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsFileIO {
    private static String rootMusicDirectoryString;
    private static String themeFileNameString;


    // Input functions
    private static void jsonFileInput() {

        System.out.println("reading json");
        Path jsonURL = Paths.get(ResourceURLs.getSettingsURL());
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(String.valueOf(jsonURL))) {
            Object obj = jsonParser.parse(reader);
            JSONArray settingsList = (JSONArray) obj;
            settingsList.forEach( settings -> parseSettingsObject( (JSONObject) settings));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }

    private static void parseSettingsObject(JSONObject settings) {
        JSONObject settingObject = (JSONObject) settings.get("userSettings");
        rootMusicDirectoryString = (String) settingObject.get("musicLibrary");
        themeFileNameString = (String) settingObject.get("themeFileName");
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          Output Modules
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void jsonOutputMusicDirectory(String newDirectoryString) {
        JSONObject userSettingsDetails = new JSONObject();
        userSettingsDetails.put("musicLibrary", newDirectoryString);

        JSONObject userSettingsObject = new JSONObject();
        userSettingsObject.put("userSettings", userSettingsDetails);

        JSONArray userSettingsList = new JSONArray();
        userSettingsList.add(userSettingsObject);

        try (FileWriter file = new FileWriter(ResourceURLs.getSettingsURL())) {
            System.out.println("Writing 'newDirectoryString' to settings.json");
            file.write(userSettingsList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void jsonOutputThemeFileName(String newThemeFileNameString) {
        JSONObject userSettingsDetails = new JSONObject();
        userSettingsDetails.put("themeFileName", newThemeFileNameString);

        JSONObject userSettingsObject = new JSONObject();
        userSettingsObject.put("userSettings", userSettingsDetails);

        JSONArray userSettingsList = new JSONArray();
        userSettingsList.add(userSettingsObject);

        try (FileWriter file = new FileWriter(ResourceURLs.getSettingsURL())) {
            System.out.println("Writing 'newThemeFileName' to settings.json");
            file.write(userSettingsList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static String getMusicDirectoryString() {
        jsonFileInput();

        return rootMusicDirectoryString;
    }

    public static String getThemeFileNameString() {
        jsonFileInput();

        return themeFileNameString;
    }
}
