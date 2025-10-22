package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementImmobile extends Deplacement {
    public DeplacementImmobile(Config config, int speed) {
        super(config, speed);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {

    }
}
