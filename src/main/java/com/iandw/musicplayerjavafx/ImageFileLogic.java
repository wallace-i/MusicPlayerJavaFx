package com.iandw.musicplayerjavafx;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

public class ImageFileLogic {
    private final String userTheme;
    // CSS File Names
    private final String styleLightFileName = "style-light.css";
    private final String styleDarkFileName = "style-dark.css";
    private final String styleBlueFileName = "style-blue.css";
    private final String styleGreenFileName = "style-green.css";
    private final String stylePinkFileName = "style-pink.css";
    private final String styleConsoleFileName = "style-console.css";

    public ImageFileLogic(String userTheme) {
        this.userTheme = userTheme;
    }

    public Lighting getLighting() {
        Lighting lighting = null;

        switch (userTheme) {
            case styleLightFileName -> {
                lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(105, 105, 105)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

            }

            case styleDarkFileName -> {
                lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(130, 130, 130)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

            }

            case styleBlueFileName -> {
                lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(45, 112, 134)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

            }

            case styleGreenFileName -> {
                lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(61, 143, 82)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

            }

            case stylePinkFileName -> {
                lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(125, 54, 125)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

            }

            case styleConsoleFileName -> {
                lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(58, 120, 58)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

            }
        }

        return lighting;
    }

    public String getAlbumImage() {
        String albumImageFile = null;

        switch (userTheme) {
            case styleLightFileName   -> albumImageFile = ResourceURLs.getMusicnotesLightURL();
            case styleDarkFileName    -> albumImageFile = ResourceURLs.getMusicnotesDarkURL();
            case styleBlueFileName    -> albumImageFile = ResourceURLs.getMusicnotesBlueURL();
            case styleGreenFileName   -> albumImageFile = ResourceURLs.getMusicnotesGreenURL();
            case stylePinkFileName    -> albumImageFile = ResourceURLs.getMusicnotesPinkURL();
            case styleConsoleFileName -> albumImageFile = ResourceURLs.getMusicnotesConsoleURL();

        }

        return albumImageFile;

    }

}
