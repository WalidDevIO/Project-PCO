package com.ubo.paco;

public abstract class Config {

    // Singleton configuration instance
    private static Config instance = null;

    public abstract int getWinWidth();
    public abstract int getWinHeight();
    public abstract int getSeaLevel();
    public abstract int getMaxData();
    public abstract int getSyncWindowSize();
    public abstract int getSyncDurationMs();
    public abstract int getMovementIntervalMs();
    public abstract int getDataCollectionFrequency();

    public static Config getConfig() {
        if (instance == null) {
            instance = new DefaultConfig();
        }
        return instance;
    }

    public static void setConfig(Config config) {
        instance = config;
    }
}