package com.ubo.paco.model;

import com.ubo.paco.config.Config;
import com.ubo.paco.ThreadRunner.SyncRunner;
import com.ubo.paco.deplacement.Deplacement;
import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.EventHandler;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.StartSyncEvent;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ElementMobile implements Runnable {
    private Deplacement depl;
    private Point gpsLoc;
    private AtomicBoolean inSync = new AtomicBoolean(false);
    private final EventHandler eventHandler = new EventHandler();
    protected Config config;
    protected SyncRunner runner = new SyncRunner(2);
    protected Thread thread;

    public ElementMobile(Deplacement deplacement, Point point, Config config) {
        this.config = config;
        this.depl=deplacement;
        this.gpsLoc=point;
        this.thread = new Thread(this);
    }

    public void start() {
        this.thread.start();
    }
    public void stop() {
        this.thread.interrupt();
    }

    public Thread getThread() {
        return this.thread;
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
        return inSync.get();
    }

    public void setInSync(Boolean inSync) {
        this.inSync.set(inSync);
    }

    public boolean tryStartSync() {
        return inSync.compareAndSet(false, true);
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void sync() {
        this.setInSync(true);
        getEventHandler().send(new StartSyncEvent(this));
        try {
            Thread.sleep(config.getSyncDurationMs());
        } catch (InterruptedException e) {
            //Do nothing
        }

        this.setInSync(false);
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
            Thread.sleep(config.getMovementIntervalMs());
        } catch (InterruptedException e) {
            //Do nothing
        }
    }
}
