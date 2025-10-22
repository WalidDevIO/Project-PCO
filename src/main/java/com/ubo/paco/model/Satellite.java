package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.events.AskSyncEvent;

import java.awt.*;

public class Satellite extends ElementMobile {

    private static int syncWindowSize = 50; //TODO: Remove

    public Satellite(Deplacement deplacement, Point point) {
        super(deplacement, point);
    }

    public void onSyncAsked(AskSyncEvent event) {
        Balise balise = event.getBalise();
        if(this.getInSync() || balise.getInSync()){
            return;
        }

        int satelliteX = this.getGpsLoc().x;
        int baliseX = balise.getGpsLoc().x;
        if (Math.abs(satelliteX - baliseX) <= syncWindowSize) {
            this.sync();
            balise.sync();
        }
    }

}