package com.iandw.musicplayerjavafx;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

public class ColorImage {
    private final String userTheme;

    public ColorImage(String userTheme) {
        this.userTheme = userTheme;
    }

    public Lighting getLighting() {
        // CSS File Names
        final String styleLightFileName = "style-light.css";
        final String styleDarkFileName = "style-dark.css";
        final String styleBlueFileName = "style-blue.css";
        final String styleGreenFileName = "style-green.css";
        final String stylePinkFileName = "style-pink.css";
        final String styleConsoleFileName = "style-console.css";

        switch (userTheme) {
            case styleLightFileName -> {
                Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(105, 105, 105)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

                return lighting;
            }

            case styleDarkFileName -> {
                Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(154, 154, 154)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

                return lighting;
            }

            case styleBlueFileName -> {
                Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(45, 112, 134)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

                return lighting;
            }

            case styleGreenFileName -> {
                Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(61, 143, 82)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

                return lighting;
            }

            case stylePinkFileName -> {
                Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(125, 54, 125)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

                return lighting;
            }

            case styleConsoleFileName -> {
                Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.rgb(58, 120, 58)));
                ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
                lighting.setContentInput(bright);
                lighting.setSurfaceScale(0.0);

                return lighting;
            }
        }

        return null;
    }
}
