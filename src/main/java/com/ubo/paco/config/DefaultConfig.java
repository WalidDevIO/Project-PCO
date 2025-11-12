package com.ubo.paco.config;

import java.util.Random;
import java.awt.Point;

import com.ubo.paco.deplacement.*;

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
    public int getMinData() {
        return 10;
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

    @Override
    public int getLinearMovementSpeed() {
        return 4;
    }

    @Override
    public int getRandomSpeed() {
        return new Random().nextInt(2, 5);
    }

    @Override
    public int getSeaThreshold() {
        return 50;
    }

    @Override
    public Deplacement getBaliseRandomDeplacementStrategy(Point gpsLoc) {
        Random rand = new Random();
        int choice = rand.nextInt(0,10);
        if(choice < 2) return new DeplacementImmobile(0, this);
        else if(choice < 5) return new DeplacementHorizontal(getRandomSpeed(), this);
        else if(choice < 8) return new DeplacementSinusoide(getRandomSpeed(), gpsLoc.x, gpsLoc.y, this);
        else return new DeplacementAutonome(0, this);
    }
}
