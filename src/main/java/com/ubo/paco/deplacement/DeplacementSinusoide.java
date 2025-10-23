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
        // move horizontally
        int x = elementMobile.getGpsLoc().x + this.speed;
        int width = Config.getConfig().getWinWidth();
        if (x > width) x -= width; // wrap-around
        elementMobile.setX(x);

        // compute a real sine wave for the y coordinate
        double abscisse = x - xOffset; // distance in pixels from the movement origin
        double wavelength = 200.0; // pixels per full sine cycle — adjust to taste
        double angularFreq = 2.0 * Math.PI / wavelength;
        double amplitude = Config.getConfig().getSeaLevel() / 3.0; // amplitude in pixels
        int ordonnee = yOffset + (int) Math.round(Math.sin(abscisse * angularFreq) * amplitude);

        elementMobile.setY(ordonnee);
    }
}
