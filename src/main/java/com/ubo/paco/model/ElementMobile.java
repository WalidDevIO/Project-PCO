package com.ubo.paco.model;

import com.ubo.paco.Config;
import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.EventHandler;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.StartSyncEvent;

import java.awt.*;

public class ElementMobile implements Runnable {
    private Deplacement depl;
    private Point gpsLoc;
    private Boolean inSync = false;
    private final EventHandler eventHandler = new EventHandler();

    public ElementMobile(Deplacement deplacement, Point point){
        this.depl=deplacement;
        this.gpsLoc=point;
    }

    public void bouge () {
        this.depl.bouge(this);
    }

    public void setDeplacement(Deplacement depl) {
        this.depl = depl;
    }

    public boolean isDeplacementDone() {
        return this.depl.isDone();
    }

    public void setGpsLoc(Point position) {
        this.gpsLoc = position;
    }

    public void setY(Integer y) {
        this.setGpsLoc(new Point(this.gpsLoc.x, y));
    }

    public void setX(Integer x) {
        this.setGpsLoc(new Point(x, this.gpsLoc.y));
    }

    public Point getGpsLoc() {
        return this.gpsLoc;
    }

    public Boolean getInSync() {
        return inSync;
    }

    public void setInSync(Boolean inSync) {
        this.inSync = inSync;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void sync() {
        this.inSync = true;
        getEventHandler().send(new StartSyncEvent(this));
        try {
            Thread.sleep(Config.getConfig().getSyncDurationMs());
        } catch (InterruptedException e) {
            //Do nothing
        }

        this.inSync = false;
        getEventHandler().send(new EndSyncEvent(this));
    }

    @Override
    public void run() {
        while (true) {
            makeFrame();
        }
    }

    protected void makeFrame() {
        this.bouge();
        eventHandler.send(new MoveEvent(this, this.gpsLoc));
        try {
            Thread.sleep(Config.getConfig().getMovementIntervalMs());
        } catch (InterruptedException e) {
            //Do nothing
        }
    }
}
