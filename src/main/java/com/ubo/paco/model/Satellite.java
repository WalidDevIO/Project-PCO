package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.deplacement.DeplacementHorizontal;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.config.Config;

import java.awt.*;

/**
 * Satellite.
 * Le satellite est positionné dans la simulation, se déplace horizontalement et peut rentrer en synchronisation avec une Balise.
 */
public class Satellite extends ElementMobile {

    /**
     * Crée le satellite
     * @param deplacement déplacement initial du satellite
     * @param point position initiale du satellite
     * @param config configuration de la simulation
     */
    public Satellite(Deplacement deplacement, Point point, Config config) {
        super(deplacement, point, config);
    }

    /**
     * Réception d'une demande de synchro de la part d'une balise
     * @param event la demande de synchro, sous forme d'un événement
     */
    public void onSyncAsked(AskSyncEvent event) {
        Balise balise = event.getBalise();
        if(this.getInSync() || balise.getInSync()){
            return;
        }

        int satelliteX = this.getGpsLoc().x;
        int baliseX = balise.getGpsLoc().x;
        if (Math.abs(satelliteX - baliseX) <= config.getSyncWindowSize()) {
            runner.synchronize(this, balise);
        }
    }

    @Override
    public void sync() {
        this.setDeplacement(new DeplacementHorizontal(1, config));
        super.sync();
        this.setDeplacement(new DeplacementHorizontal(config.getRandomSpeed(), config));
    }

}