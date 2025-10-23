package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.deplacement.DeplacementDescente;
import com.ubo.paco.deplacement.DeplacementHorizontal;
import com.ubo.paco.deplacement.DeplacementImmobile;
import com.ubo.paco.deplacement.DeplacementRemontee;
import com.ubo.paco.deplacement.DeplacementSinusoide;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.Config;

import java.awt.*;
import java.util.Random;

public class Balise extends ElementMobile {

    private int data=0;
    private int runnerCount=0;

    public Balise(Deplacement deplacement, Point point) {
        super(deplacement, point);
    }

    public int getData() {
        return data;
    }

    private void incr(){
        this.data++;
    }

    private Deplacement getRandomDeplacementStrategy(){
        Random rand = new Random();
        int choice = rand.nextInt(0,2);
        Deplacement depl;
        switch (choice) {
            case 0 -> depl = new DeplacementHorizontal(4);
            default -> depl = new DeplacementSinusoide(4, this.getGpsLoc().x, this.getGpsLoc().y);
        }
        System.out.println("Chosen movement strategy: " + depl.getClass().getSimpleName());
        return depl;
    }

    private boolean haveToCollectData() {
        return this.data < Config.getConfig().getMaxData();
    }

    @Override
    public void sync() {
        super.sync();
        setDeplacement(new DeplacementDescente(4, new Random().nextInt(Config.getConfig().getSeaLevel(), Config.getConfig().getWinHeight()))); //Define random movement after sync
        data=0;
    }

    @Override
    public void run() {
        while (true){
            // Collecte de données aléatoirement
            this.runnerCount++;
            if(this.runnerCount%Config.getConfig().getDataCollectionFrequency()==0 && haveToCollectData()) incr();

            if(!haveToCollectData() && !isDeplacementDone()){
                setDeplacement(new DeplacementRemontee(4));
            }

            // Déplacement terminé on passe en mode immobile et on demande une synchronisation
            if(isDeplacementDone()) {
                //On est descendu au fond, on veut se déplacer aléatoirement
                if(haveToCollectData()) {
                    setDeplacement(getRandomDeplacementStrategy());
                } else {
                    //On est remonté en surface, on attend la synchronisation
                    setDeplacement(new DeplacementImmobile(0));
                    getEventHandler().send(new AskSyncEvent(this));
                }
            }

            // Bouge
            makeFrame();
        }
    }
}
