package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

/**
 * "Déplacement" sur-place, l'élément mobile ne bouge pas
 */
public class DeplacementImmobile extends Deplacement {
    /**
     * Crée le déplacement
     * @param speed ignorée (vitesse du déplacement)
     * @param config configuration de la simulation
     */
    public DeplacementImmobile(int speed, Config config) {
        super(speed, config);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {

    }
}
