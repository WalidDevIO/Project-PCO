package com.ubo.paco.deplacement;

import com.ubo.paco.model.ElementMobile;

public abstract class Deplacement {
    protected int speed;

    public Deplacement(int speed) {
        this.speed = speed;
    }

    public abstract void bouge(ElementMobile elementMobile);

    public boolean isDone() {
        return false;
    }
}
