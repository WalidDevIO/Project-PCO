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

/**
 * Représente un élément mobile de la simulation (satellite, balise)
 */
public class ElementMobile implements Runnable {
    private Deplacement depl;
    private Point gpsLoc;
    private AtomicBoolean inSync = new AtomicBoolean(false);
    private final EventHandler eventHandler = new EventHandler();
    protected Config config;
    protected SyncRunner runner = new SyncRunner(2);
    protected Thread thread;
    protected Boolean running = false;

    public ElementMobile(Deplacement deplacement, Point point, Config config) {
        this.config = config;
        this.depl=deplacement;
        this.gpsLoc=point;
        this.thread = new Thread(this);
    }

    /**
     * Active l'élément mobile
     */
    public void start() {
        if (running) return;          // éviter double start
        running = true;

        // recréer un thread si on a déjà stop → car un Thread ne peut PAS être relancé
        if (!thread.isAlive()) {
            thread = new Thread(this);
        }

        thread.start();
    }

    /**
     * Désactive l'élément mobile
     */
    public void stop() {
        running = false;              // demande d'arrêt

        // au cas où le thread dort → on l'interrompt
        if (thread != null) {
            thread.interrupt();
        }
    }

    public Thread getThread() {
        return this.thread;
    }

    /**
     * Met à jour la position de l'élément mobile grâce à son déplacement
     */
    public void bouge () {
        this.depl.bouge(this);
    }

    /**
     * Change le déplacement de l'élément
     * @param depl le nouveau déplacement
     */
    public void setDeplacement(Deplacement depl) {
        this.depl = depl;
    }

    /**
     * Indique si le déplacement actuel de l'élément est terminé
     * @return état d'achèvement du déplacement
     */
    public boolean isDeplacementDone() {
        return this.depl.isDone();
    }

    /**
     * Modifie la position de l'élément
     * @param position la nouvelle position de l'élément
     */
    public void setGpsLoc(Point position) {
        this.gpsLoc = position;
    }

    /**
     * Modifie la coordonnée y de l'élément
     * @param y nouvelle coordonnée y de l'élément
     */
    public void setY(Integer y) {
        this.setGpsLoc(new Point(this.gpsLoc.x, y));
    }

    /**
     * Modifie la coordonnée x de l'élément
     * @param x nouvelle coordonnée x de l'élément
     */
    public void setX(Integer x) {
        this.setGpsLoc(new Point(x, this.gpsLoc.y));
    }

    /**
     * Obtient la position de l'élément
     * @return la position de l'élément
     */
    public Point getGpsLoc() {
        return this.gpsLoc;
    }

    /**
     * Indique si l'élément est en cours de synchro avec un autre
     * @return l'état de synchro de l'élément
     */
    public Boolean getInSync() {
        return inSync.get();
    }

    /**
     * Définit l'état de synchro de l'élément
     * @param inSync le nouvel état de synchro de l'élément
     */
    public void setInSync(Boolean inSync) {
        this.inSync.set(inSync);
    }

    public boolean tryStartSync() {
        return inSync.compareAndSet(false, true);
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Process de synchro de l'élément (début, délai, fin)
     */
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
        while (running) {
            makeFrame();
        }
    }

    /**
     * Met a jour la position de l'élément par rapport à son déplacement et en notifie la vue
     */
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
