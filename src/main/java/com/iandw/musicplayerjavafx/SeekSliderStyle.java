package com.iandw.musicplayerjavafx;

public class SeekSliderStyle {
    private static final String lightStyle =
            "-track-color: linear-gradient(to right, " +
            "-fx-accent 0%%, " +
            "-fx-accent %1$.1f%%, " +
            "-default-track-color %1$.1f%%, " +
            "-default-track-color 100%%);";

    private static final String greenStyle =
            "-track-color: linear-gradient(to right, " +
            "-fx-accent 0%%, " +
            "-fx-accent %1$.1f%%, " +
            "transparent %1$.1f%%, " +
            "transparent 100%%);";

    public static String getStyle(UserSettings userSettings) {
        final String light = "style-light.css";
        final String green = "style-green.css";

        switch (userSettings.getThemeFileNameString()) {
            case light -> { return lightStyle; }
            case green -> { return greenStyle; }

        }

        // default return
        return lightStyle;
    }
}
