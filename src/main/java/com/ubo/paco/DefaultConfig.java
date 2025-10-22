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
        return 15;
    }

    @Override
    public int getSyncWindowSize() {
        return 50;
    }
}
