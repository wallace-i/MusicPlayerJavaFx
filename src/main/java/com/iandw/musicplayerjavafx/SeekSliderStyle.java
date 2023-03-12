package com.iandw.musicplayerjavafx;

public class SeekSliderStyle {

    private static final String lightStyle =
            "-track-color: linear-gradient(to right, " +
            "-fx-accent 0%%, " +
            "-fx-accent %1$.1f%%, " +
            "transparent %1$.1f%%, " +
            "transparent 100%%);";

    private static final String greenStyle =
            "-track-color: linear-gradient(to right, " +
            "#3CB371 0%%, " +
            "#3CB371 %1$.1f%%, " +
            "#e6ffe6 %1$.1f%%, " +
            "#e6ffe6 100%%);";

    private static final String darkStyle =
            "-track-color: linear-gradient(to right, " +
            "#214283 0%%, " +
            "#214283 %1$.1f%%, " +
            "#363840 %1$.1f%%, " +
            "#363840 100%%);";

    public static String getStyle(UserSettings userSettings) {
        final String light = "style-light.css";
        final String green = "style-green.css";
        final String dark = "style-dark.css";

        switch (userSettings.getThemeFileNameString()) {
            case light -> { return lightStyle; }
            case green -> { return greenStyle; }
            case dark -> { return darkStyle; }

        }

        // default return
        return lightStyle;
    }
}
