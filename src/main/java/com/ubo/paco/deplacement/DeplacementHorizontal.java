package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementHorizontal extends Deplacement {
    public DeplacementHorizontal(Config config, int speed) {
        super(config, speed);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int x = elementMobile.getGpsLoc().x;
        x = x + speed;
        if(x > conf.getWinWidth()) x = 0;
        elementMobile.setX(x);
    }
}
