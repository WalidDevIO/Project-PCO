package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.Config;

import java.awt.*;

public class Satellite extends ElementMobile {

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
        if (Math.abs(satelliteX - baliseX) <= Config.getConfig().getSyncWindowSize()) {
            this.sync();
            balise.sync();
        }
    }

}