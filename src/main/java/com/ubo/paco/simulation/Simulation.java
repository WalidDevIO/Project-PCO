package com.ubo.paco.simulation;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;

/**
 * DSL pour gérer la simulation.
 */
public interface Simulation {
    /**
     * Crée et ajoute un satellite à la simulation
     * @param x position x de départ du satellite
     * @param y position y de départ du satellite
     * @return le satellite créé
     */
    public Satellite createSatellite(int x, int y);

    /**
     * Crée et ajoute une balise à la simulation
     * @param x position x de départ de la balise
     * @param y position y de départ de la balise
     * @param deplacement mode de déplacement de la balise
     * @return la balise créée
     */
    public Balise createBalise(int x, int y, String deplacement);

    /**
     * Démarre la simulation (cela démarre automatiquement tous les satellites et balises qui y ont été créés).
     */
    public void start();

    /**
     * Arrête/met en pause la simulation (fige tous les satellites et balises qui y ont été créés).
     */
    public void stop();

    /**
     * Indique si la simulation est démarrée.
     * @return état d'exécution de la simulation
     */
    public boolean isRunning();
}
