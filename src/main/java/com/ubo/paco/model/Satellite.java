package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.deplacement.DeplacementHorizontal;
import com.ubo.paco.deplacement.DeplacementImmobile;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.Config;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Satellite extends ElementMobile implements Runnable {

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
            CompletableFuture<Void> f1 = CompletableFuture.runAsync(this::sync);
            CompletableFuture<Void> f2 = CompletableFuture.runAsync(balise::sync);

            CompletableFuture.allOf(f1, f2).join();
        }
    }

    @Override
    public void sync() {
        this.setDeplacement(new DeplacementHorizontal(1));
        super.sync();
        this.setDeplacement(new DeplacementHorizontal(new Random().nextInt(1, 5)));
    }

}