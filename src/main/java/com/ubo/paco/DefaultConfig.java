package com.ubo.paco;

public class DefaultConfig extends Config {
    @Override
    public int getWinWidth() {
        return 1280;
    }

    @Override
    public int getWinHeight() {
        return 720;
    }

    @Override
    public int getSeaLevel() {
        return getWinHeight() / 2;
    }

    @Override
    public int getMaxData() {
        return 30;
    }

    @Override
    public int getSyncWindowSize() {
        return 50;
    }

    @Override
    public int getSyncDurationMs() {
        return 2000;
    }

    @Override
    public int getMovementIntervalMs() {
        return 50;
    }

    @Override
    public int getDataCollectionFrequency() {
        return 10;
    }
}
