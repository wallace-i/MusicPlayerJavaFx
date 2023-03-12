package com.iandw.musicplayerjavafx;

public class SeekSliderStyle {

    private static final String lightStyle =
            "-track-color: linear-gradient(to right, " +
            "-fx-accent 0%%, " +
            "-fx-accent %1$.1f%%, " +
            "transparent %1$.1f%%, " +
            "transparent 100%%);";

    private static final String darkStyle =
            "-track-color: linear-gradient(to right, " +
                    "#214283 0%%, " +
                    "#214283 %1$.1f%%, " +
                    "#363840 %1$.1f%%, " +
                    "#363840 100%%);";

    private static final String greenStyle =
            "-track-color: linear-gradient(to right, " +
            "#3cb45a 0%%, " +
            "#3cb45a %1$.1f%%, " +
            "#e6ffe6 %1$.1f%%, " +
            "#e6ffe6 100%%);";



    public static String getStyle(UserSettings userSettings) {
        final String light = "style-light.css";
        final String dark = "style-dark.css";
        final String green = "style-green.css";

        switch (userSettings.getThemeFileNameString()) {
            case light -> { return lightStyle; }
            case dark  -> { return darkStyle;  }
            case green -> { return greenStyle; }
        }

        // default return
        return lightStyle;
    }
}
