package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementDescente extends Deplacement {
    private int bottom;
    private boolean done = false;

    public DeplacementDescente(Config config, int speed, int bottom) {
        super(config, speed);
        this.bottom = bottom;
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int y = elementMobile.getGpsLoc().y;
        if(y < bottom) {
            elementMobile.setY(y + this.speed);
        } else done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
