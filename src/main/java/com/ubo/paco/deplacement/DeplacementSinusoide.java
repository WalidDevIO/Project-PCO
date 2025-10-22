package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementSinusoide extends Deplacement {
    private int xOffset, yOffset;

    /**
     *
     * @param config
     * @param speed
     * @param xOffset position x de la balise au depart de ce deplacement
     * @param yOffset position y de la balise au depart de ce deplacement
     */
    public DeplacementSinusoide(Config config, int speed, int xOffset, int yOffset) {
        super(config, speed);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        
    }
}
