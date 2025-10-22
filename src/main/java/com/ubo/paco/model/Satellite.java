package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.events.AskSyncEvent;

import java.awt.*;

public class Satellite extends ElementMobile {

    public Satellite(Deplacement deplacement, Point point) {
        super(deplacement, point);
    }

    public void onSyncAsked(AskSyncEvent event) {
        // Implémentation de la synchronisation avec la balise
    }

}