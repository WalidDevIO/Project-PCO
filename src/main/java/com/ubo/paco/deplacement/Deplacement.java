package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public abstract class Deplacement {
    protected int speed;
    protected Config config;

    public Deplacement(int speed, Config config) {
        this.config = config;
        this.speed = speed;
    }

    public abstract void bouge(ElementMobile elementMobile);

    public boolean isDone() {
        return false;
    }
}
