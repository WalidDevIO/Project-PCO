package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public abstract class Deplacement {
    protected int speed;
    protected Config conf;
    public Deplacement(Config config, int speed) {
        this.speed = speed;
    }

    public abstract void bouge(ElementMobile elementMobile);

    public boolean isDone() {
        return false;
    }
}
