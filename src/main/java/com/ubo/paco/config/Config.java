package com.ubo.paco.config;

import java.awt.Point;

import com.ubo.paco.deplacement.Deplacement;

public abstract class Config {

    /**
     * Obtient la largeur de la fenêtre
     * @return la largeur de la fenêtre
     */
    public abstract int getWinWidth();

    /**
     * Obtient la hauteur de la fenêtre
     * @return la hauteur de la fenêtre
     */
    public abstract int getWinHeight();

    /**
     * Obtient la hauteur de la mer
     * @return la hauteur de la mer
     */
    public abstract int getSeaLevel();

    /**
     * Obtient le nombre max de données que peut collecter une balise
     * @return le nombre max de données que peut collecter une balise
     */
    public abstract int getMaxData();

    /**
     * Obtient le nombre min de données que peut collecter une balise
     * @return le nombre min de données que peut collecter une balise
     */
    public abstract int getMinData();

    /**
     * Obtient la taille de la fenêtre de synchro entre une balise et un satellite.
     * Il s'agit de la taille de la colonne dans laquelle balise et satellite doivent se trouver pour pouvoir entrer en communication.
     * @return la taille de la fenêtre de synchro
     */
    public abstract int getSyncWindowSize();

    /**
     * Obtient la durée d'une synchronisation entre balise et satellite (la durée pendant laquelle ils communiquent).
     * @return la durée d'une synchro entre balise et satellite
     */
    public abstract int getSyncDurationMs();

    /**
     * Obtient la durée entre chaque mouvement d'un élément mobile
     * @return la durée entre chaque mouvement d'un élément mobile
     */
    public abstract int getMovementIntervalMs();

    /**
     * Obtient la fréquence de collection d'une nouvelle donnée par une balise
     * @return la fréquence de collection d'une nouvelle donnée par une balise
     */
    public abstract int getDataCollectionFrequency();

    /**
     * Obtient la vitesse du mouvement linéaire d'un élement mobile
     * Cette vitesse est utilisée pour les descentes et les remontées.
     * @return la vitesse du mouvement linéaire d'un élement mobile
     */
    public abstract int getLinearMovementSpeed();

    /**
     * Génère une vitesse aléatoire pour créer un déplacement
     * @return une vitesse aléatoire
     */
    public abstract int getRandomSpeed();

    /**
     * Obtient le niveau minimum de profondeur pour considérer qu'on est sous la mer.
     * On peut voir ça comme la hauteur des vagues.
     * @return le niveau de profondeur pour considérer qu'on est sous la mer
     */
    public abstract int getSeaThreshold();

    /**
     * Obtient une stratégie de déplacement aléatoire pour une balise, à l'exception des déplacements de descente et de remontée.
     * @param gpsLoc la position de l'élément mobile au moment de la création du déplacement
     * @return un nouveau déplacement
     */
    public abstract Deplacement getBaliseRandomDeplacementStrategy(Point gpsLoc);

}