package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementHorizontal extends Deplacement {
    public DeplacementHorizontal(int speed, Config config) {
        super(speed, config);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int x = elementMobile.getGpsLoc().x;
        x = x + speed;
        if(x > config.getWinWidth()) x = 0;
        elementMobile.setX(x);
    }
}
