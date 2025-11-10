package com.ubo.paco.config;

import java.awt.Point;

import com.ubo.paco.deplacement.Deplacement;

public abstract class Config {

    public abstract int getWinWidth();
    public abstract int getWinHeight();
    public abstract int getSeaLevel();
    public abstract int getMaxData();
    public abstract int getSyncWindowSize();
    public abstract int getSyncDurationMs();
    public abstract int getMovementIntervalMs();
    public abstract int getDataCollectionFrequency();
    public abstract int getLinearMovementSpeed();
    public abstract int getRandomSpeed();
    public abstract Deplacement getBaliseRandomDeplacementStrategy(Point gpsLoc);

}