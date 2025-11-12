package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

/**
 * La classe abstraite Deplacement représente une stratégie de déplacement appliquée à un ElementMobile.
 */

public abstract class Deplacement {
    protected int speed;
    protected Config config;

    /**
     * Crée un nouvel objet de Deplacement
     * @param speed vitesse du déplacement
     * @param config configuration de la simulation
     */
    public Deplacement(int speed, Config config) {
        this.config = config;
        this.speed = speed;
    }

    /**
     * Fait une étape "un tick" de déplacement
     * @param elementMobile l'objet à déplacer
     */
    public abstract void bouge(ElementMobile elementMobile);

    /**
     * Indique si le déplacement est terminé.
     * Certains déplacements n'ont pas d'indicateur de fin (déplacement horizontal, sinusoidal...).
     * Cette valeur est surtout utile pour les déplacements de descente et de remontée qui doivent atteindre une hauteur.
     * @return true si déplacement est terminé, false s'il est encore en cours.
     */
    public boolean isDone() {
        return false;
    }
}
