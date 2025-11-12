package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

/**
 * Déplacement sur l'axe horizontal.
 * L'élement mobile bougé repasse à gauche de l'écran s'il en quitte le bord droit.
 */
public class DeplacementHorizontal extends Deplacement {
    /**
     * Crée le déplacement.
     * @param speed vitesse du déplacement
     * @param config configuration de la simulation
     */
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
