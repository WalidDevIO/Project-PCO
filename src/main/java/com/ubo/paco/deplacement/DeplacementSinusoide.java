package com.ubo.paco.deplacement;

import com.ubo.paco.Config;
import com.ubo.paco.model.ElementMobile;

public class DeplacementSinusoide extends Deplacement {
    private int xOffset, yOffset;

    /**
     *
     * @param speed
     * @param xOffset position x de la balise au depart de ce deplacement
     * @param yOffset position y de la balise au depart de ce deplacement
     */
    public DeplacementSinusoide(int speed, int xOffset, int yOffset) {
        super(speed);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int x = elementMobile.getGpsLoc().x;
        x = x + this.speed;
        if(x > Config.getConfig().getWinWidth()) x = 0;
        elementMobile.setX(x);

        int abscisse = elementMobile.getGpsLoc().x - xOffset;
        int ordonnee = (int) Math.cos(abscisse) * Config.getConfig().getSeaLevel() / 3 + yOffset;

        elementMobile.setY(ordonnee);
    }
}
