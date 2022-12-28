package com.iandw.musicplayerjavafx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonReadWrite {

    private static String rootMusicDirectoryString;

    private static void jsonRead(URL jsonURL) {

        System.out.println("reading json");
        JSONParser jsonParser = new JSONParser();
        Path jsonFilePath = Paths.get(jsonURL.toString().substring(6));

        try (FileReader reader = new FileReader(jsonFilePath.toString())) {
            Object obj = jsonParser.parse(reader);
            JSONArray settingsList = (JSONArray) obj;
            settingsList.forEach( settings -> parseSettingsObject( (JSONObject) settings));
            System.out.println(rootMusicDirectoryString);


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


    }

    public static void jsonWrite() {
        JSONObject userSettingJSONObj = new JSONObject();
        userSettingJSONObj.put("musicLibrary", "C:" + File.separator + "dev" + File.separator + "DemoMusic" );

        try (FileWriter file = new FileWriter("Settings.json")) {
            file.write(userSettingJSONObj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String readMusicDirectoryString(URL jsonURL) {
       jsonRead(jsonURL);

       return rootMusicDirectoryString;
    }


    private static void parseSettingsObject(JSONObject setting) {
        JSONObject settingObject = (JSONObject) setting.get("userSettings");
        rootMusicDirectoryString = (String) settingObject.get("musicLibrary");

    }
}
