package com.iandw.musicplayerjavafx;

import java.nio.file.Path;

public class Settings {
    private static Path rootDirectoryPath;


    public Settings() {
        this.rootDirectoryPath = null;
    }

    // Get music library folder
    public Path getRootDirectoryPath() { return rootDirectoryPath; }

    // Set music library folder
    public void setRootDirectoryPath(Path path) {
        this.rootDirectoryPath = path;
    }
}
