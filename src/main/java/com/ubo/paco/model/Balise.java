package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.deplacement.DeplacementDescente;
import com.ubo.paco.config.Config;
import com.ubo.paco.model.baliseprogram.BaliseProgram;
import com.ubo.paco.model.baliseprogram.DefaultBaliseProgram;

import java.awt.*;
import java.util.Random;

/**
 * Balise.
 * La balise est positionnée dans la simulation, se déplace selon différents modes pour collecter des données et peut rentrer en synchronisation avec un Satellite.
 */
public class Balise extends ElementMobile {

    private int data=0; // données collectées
    private int dataCountRequired=0; // nombre de données collectées
    private int tickCount=0; // compte le nombre de ticks/frames passées (utilisé pour le calcul de la fréquence de collection)

    /**
     * La balise dispose d'un mode de déplacement une fois immergée.
     * Elle doit se souvenir de ce déplacement
     */
    private Deplacement deplacementStrategy;

    /**
     * Le programme de la balise permet de ne pas rester figé dans un cycle descente -> collecte -> remontée -> synchro
     * On pourrait par exemple définir un programme où deux descentes sont nécessaires avant de faire une synchro
     */
    private BaliseProgram prog;

    /**
     * Crée la balise
     * @param deplacement mode de déplacement initial de la balise
     * @param point position initiale de la balise
     * @param config configuration de la simulation
     */
    public Balise(Deplacement deplacement, Point point, Config config) {
        super(deplacement, point, config);
        this.deplacementStrategy = deplacement;
        this.prog = new DefaultBaliseProgram(this, config);
        this.dataCountRequired = new Random().nextInt(config.getMinData(), config.getMaxData());
    }

    /**
     * Obtient les données collectées
     * @return les données collectées
     */
    public int getData() {
        return data;
    }

    /**
     * Collecte un nouveau point de donnée
     */
    public void incr(){
        this.data++;
    }

    /**
     * Indique si la collecte de données est toujours en cours
     * @return un booléen qui indique s'il faut continuer de collecter des données
     */
    public boolean haveToCollectData() {
        return this.data < dataCountRequired;
    }

    public Deplacement getDeplacementStrategy() {
        return deplacementStrategy;
    }

    public void setDeplacementStrategy(Deplacement deplacementStrategy) {
        this.deplacementStrategy = deplacementStrategy;
    }

    @Override
    public void sync() {
        super.sync();
        // synchro terminee, on redescend
        setDeplacement(
            new DeplacementDescente(
                config.getLinearMovementSpeed(),
                new Random().nextInt(config.getSeaLevel() + config.getSeaThreshold(), config.getWinHeight()),
                config
            )
        );
        data=0;
        this.dataCountRequired = new Random().nextInt(config.getMinData(), config.getMaxData());
    }

    @Override
    public void run() {
        while (this.running){
            // Collecte de données aléatoirement
            this.tickCount++;
            if(this.tickCount%config.getDataCollectionFrequency()==0 && haveToCollectData()) incr();

            // tick du cycle de la balise
            this.prog.tick();

            // mise a jour vue
            makeFrame();
        }
    }
}
