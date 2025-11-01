package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementRemontee extends Deplacement {
    private boolean done = false;

    public DeplacementRemontee(int speed, Config config) {
        super(speed, config);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int y = elementMobile.getGpsLoc().y;
        if(y >= config.getSeaLevel()) {
            elementMobile.setY(y - this.speed);
        } else done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
