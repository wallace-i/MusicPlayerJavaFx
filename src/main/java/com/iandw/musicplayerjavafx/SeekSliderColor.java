package com.iandw.musicplayerjavafx;

public class SeekSliderColor {

    private static final String lightStyle =
            "-track-color: linear-gradient(to right, " +
                    "-fx-accent 0%%, " +
                    "-fx-accent %1$.1f%%, " +
                    "transparent %1$.1f%%, " +
                    "transparent 100%%);";

    private static final String darkStyle =
            "-track-color: linear-gradient(to right, " +
                    "#004de6 0%%, " +
                    "#004de6 %1$.1f%%, " +
                    "#363840 %1$.1f%%, " +
                    "#363840 100%%);";

    private static final String greenStyle =
            "-track-color: linear-gradient(to right, " +
                    "#3cb478 0%%, " +
                    "#3cb478 %1$.1f%%, " +
                    "#e6ffe6 %1$.1f%%, " +
                    "#e6ffe6 100%%);";

    private static final String blueStyle =
            "-track-color: linear-gradient(to right, " +
                    "#3c96b4 0%%, " +
                    "#3c96b4 %1$.1f%%, " +
                    "#e5f9ff %1$.1f%%, " +
                    "#e5f9ff 100%%);";

    private static final String pinkStyle =
            "-track-color: linear-gradient(to right, " +
                    "#b43cb4 0%%, " +
                    "#b43cb4 %1$.1f%%, " +
                    "#ffe5f9 %1$.1f%%, " +
                    "#ffe5f9 100%%);";

    private static final String consoleStyle =
            "-track-color: linear-gradient(to right, " +
                    "#267326 0%%, " +
                    "#267326 %1$.1f%%, " +
                    "#363840 %1$.1f%%, " +
                    "#363840 100%%);";


    public static String getStyle(String currentTheme) {
        final String light = "style-light.css";
        final String dark = "style-dark.css";
        final String green = "style-green.css";
        final String blue = "style-blue.css";
        final String pink = "style-pink.css";
        final String console = "style-console.css";

        switch (currentTheme) {
            case light -> { return lightStyle; }
            case dark  -> { return darkStyle;  }
            case green -> { return greenStyle; }
            case blue  -> { return blueStyle;  }
            case pink  -> { return pinkStyle;  }
            case console -> { return consoleStyle; }
        }

        // default return
        return lightStyle;
    }
}
