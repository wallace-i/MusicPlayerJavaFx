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


    // Input functions
    public static JSONArray jsonFileInput() {

        System.out.println("Reading user settings from JSON file.");
        Path jsonURL = Paths.get(ResourceURLs.getSettingsURL());
        JSONParser jsonParser = new JSONParser();
        Object obj = new Object();

        try (FileReader reader = new FileReader(String.valueOf(jsonURL))) {
            obj = jsonParser.parse(reader);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return (JSONArray) obj;

    }



    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          Output Modules
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static void jsonFileOutput(UserSettings userSettings) {
        final String rootMusicDirectoryString = userSettings.getRootMusicDirectoryString();
        final String themeFileNameString = userSettings.getThemeFileNameString();

        JSONObject userSettingsDetails = new JSONObject();
        userSettingsDetails.put("musicLibrary", rootMusicDirectoryString);
        userSettingsDetails.put("themeFileName", themeFileNameString);

        JSONObject userSettingsObject = new JSONObject();
        userSettingsObject.put("userSettings", userSettingsDetails);

        JSONArray userSettingsList = new JSONArray();
        userSettingsList.add(userSettingsObject);

        try (FileWriter file = new FileWriter(ResourceURLs.getSettingsURL())) {
            System.out.println("Writing user settings to settings.json");
            file.write(userSettingsList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
