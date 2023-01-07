package com.iandw.musicplayerjavafx;

import java.util.Objects;

public class ResourceURLs {
    private static final String settingsURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "Settings.json")).toString().substring(6));

    private static final String metadataHashMapURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "MetadataHashMap.ser")).toString().substring(6));

    private static final String searchTreeMapURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "SearchTreeMap.dat")).toString().substring(6));

    // Getters
    public static String getSettingsURL() { return settingsURL; }
    public static String getMetadataHashMapURL() { return metadataHashMapURL; }
    public static String getSearchTreeMapURL() { return searchTreeMapURL; }
}
