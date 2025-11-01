package com.ubo.paco;

public abstract class Config {

    public abstract int getWinWidth();
    public abstract int getWinHeight();
    public abstract int getSeaLevel();
    public abstract int getMaxData();
    public abstract int getSyncWindowSize();
    public abstract int getSyncDurationMs();
    public abstract int getMovementIntervalMs();
    public abstract int getDataCollectionFrequency();

}