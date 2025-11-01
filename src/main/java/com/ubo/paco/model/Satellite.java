package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.deplacement.DeplacementHorizontal;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.config.Config;

import java.awt.*;
import java.util.Random;

public class Satellite extends ElementMobile {

    public Satellite(Deplacement deplacement, Point point, Config config) {
        super(deplacement, point, config);
    }

    public void onSyncAsked(AskSyncEvent event) {
        Balise balise = event.getBalise();
        if(this.getInSync() || balise.getInSync()){
            return;
        }

        int satelliteX = this.getGpsLoc().x;
        int baliseX = balise.getGpsLoc().x;
        if (Math.abs(satelliteX - baliseX) <= config.getSyncWindowSize()) {
            runner.runTwoSyncs(this, balise);
        }
    }

    @Override
    public void sync() {
        this.setDeplacement(new DeplacementHorizontal(1, config));
        super.sync();
        this.setDeplacement(new DeplacementHorizontal(new Random().nextInt(1, 5), config));
    }

}