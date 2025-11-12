package com.ubo.paco.model.baliseprogram;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.Balise;

/**
 * Le programme d'une balise définit la gestion de ses déplacements.
 */
public abstract class BaliseProgram {
    protected Balise balise;
    protected Config config;

    /**
     * Crée le programme
     * @param b la balise concernée
     * @param c la configuration de la simulation
     */
    public BaliseProgram(Balise b, Config c) {
        this.balise = b;
        this.config = c;
    }

    /**
     * Exécute un nouveau cycle du programme
     */
    public abstract void tick();
}
