package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.deplacement.DeplacementDescente;
import com.ubo.paco.config.Config;
import com.ubo.paco.model.baliseprogram.BaliseProgram;
import com.ubo.paco.model.baliseprogram.DefaultBaliseProgram;

import java.awt.*;
import java.util.Random;

public class Balise extends ElementMobile {

    private int data=0;
    private int dataCountRequired=0;
    private int runnerCount=0;

    private Deplacement deplacementStrategy;

    private BaliseProgram prog;

    public Balise(Deplacement deplacement, Point point, Config config) {
        super(deplacement, point, config);
        this.deplacementStrategy = deplacement;
        this.prog = new DefaultBaliseProgram(this, config);
        this.dataCountRequired = new Random().nextInt(config.getMinData(), config.getMaxData());
    }

    public int getData() {
        return data;
    }

    public void incr(){
        this.data++;
    }

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
        while (true){
            // Collecte de données aléatoirement
            this.runnerCount++;
            if(this.runnerCount%config.getDataCollectionFrequency()==0 && haveToCollectData()) incr();

            // tick du cycle de la balise
            this.prog.tick();

            // mise a jour vue
            makeFrame();
        }
    }
}
