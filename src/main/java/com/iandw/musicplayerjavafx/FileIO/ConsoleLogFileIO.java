package com.iandw.musicplayerjavafx.FileIO;

import com.iandw.musicplayerjavafx.ResourceURLs;

import java.io.FileWriter;
import java.io.IOException;

public class ConsoleLogFileIO {

    public static void outputConsoleLog(String consoleLog) {
        try {
            // Write track objects to file
            System.out.println("Writing to consolelog.txt");
            FileWriter fileWriter = new FileWriter(ResourceURLs.getConsolelogURL());
            fileWriter.write(consoleLog);
            fileWriter.close();

        } catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }


}
