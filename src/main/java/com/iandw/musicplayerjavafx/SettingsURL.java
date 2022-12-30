package com.iandw.musicplayerjavafx;

import java.util.Objects;

public class SettingsURL {
    private static final String settingsURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "Settings.json")).toString().substring(6));

    public static String getSettingsURL() { return settingsURL; }
}
