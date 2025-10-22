package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementRemontee extends Deplacement {
    private boolean done = false;

    public DeplacementRemontee(Config config, int speed) {
        super(config, speed);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int y = elementMobile.getGpsLoc().y;
        if(y > conf.getSeaLevel()) {
            elementMobile.setY(y - this.speed);
        } else done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
