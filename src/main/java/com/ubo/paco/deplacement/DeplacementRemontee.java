package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

/**
 * Déplacement de remontée jusqu'à la hauteur de la mer.
 * La hauteur de la mer est obtenue depuis la config de l'application.
 */
public class DeplacementRemontee extends Deplacement {
    private boolean done = false;

    /**
     * Crée le déplacement
     * @param speed vitesse de la remontée
     * @param config configuration de la simulation
     */
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
