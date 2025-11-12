package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

/**
 * Déplacement de descente jusqu'à une hauteur donnée.
 *
 * Contrairement au déplacement de remontée, où la hauteur voulue est celle de la mer (obtenue dans la config),
 * ici chaque élément mobile peut descendre à une hauteur différente, à définir dans le constructeur.
 */
public class DeplacementDescente extends Deplacement {
    private int bottom;
    private boolean done = false;

    /**
     * Crée le déplacement.
     * @param speed vitesse du déplacement
     * @param bottom hauteur à atteindre
     * @param config configuration de la simulation
     */
    public DeplacementDescente(int speed, int bottom, Config config) {
        super(speed, config);
        this.bottom = bottom;
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        int y = elementMobile.getGpsLoc().y;
        if(y < bottom) {
            elementMobile.setY(y + this.speed);
        } else done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
