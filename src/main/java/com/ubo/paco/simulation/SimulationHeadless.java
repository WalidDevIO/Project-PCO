package com.ubo.paco.simulation;

import com.ubo.paco.config.Config;
import com.ubo.paco.config.DefaultConfig;
import com.ubo.paco.deplacement.*;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.StartSyncEvent;
import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import com.ubo.paco.view.ViewHeadless;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulation headless : représente l'état de la simulation par des lignes de logs
 */
public class SimulationHeadless implements Simulation {
    private List<Satellite> satellites = new ArrayList<>();
    private List<Balise> balises = new ArrayList<>();

    private Config config = new DefaultConfig();
    private boolean running = false;

    public SimulationHeadless() {
    }

    public SimulationHeadless(Config config) {
        this.config = config;
    }

    private Deplacement parseDeplacement(String deplacement, int x, int y) {
        return switch(deplacement.toLowerCase()) {
            case "horizontal" -> new DeplacementHorizontal(config.getRandomSpeed(), config);
            case "sinusoidal" -> new DeplacementSinusoide(config.getRandomSpeed(), x, y, config);
            case "immobile" -> new DeplacementImmobile(0, config);
            default -> new DeplacementAutonome(config.getRandomSpeed(), config);
        };
    }

    @Override
    public Satellite createSatellite(int x, int y) {
        // creation du satellite
        Point point = new Point(x, y);
        Deplacement depl = new DeplacementHorizontal(config.getRandomSpeed(), config);
        Satellite sat = new Satellite(depl, point, config);
        satellites.add(sat);

        // abonnement a la vue
        ViewHeadless logger = new ViewHeadless();
        sat.getEventHandler().registerListener(MoveEvent.class, logger);
        sat.getEventHandler().registerListener(StartSyncEvent.class, logger);
        sat.getEventHandler().registerListener(EndSyncEvent.class, logger);

        // abonnement des balises existants a ce nouveau satellite
        for(Balise b : balises) b.getEventHandler().registerListener(AskSyncEvent.class, sat);

        return sat;
    }

    @Override
    public Balise createBalise(int x, int y, String deplacement) {
        // creation de la balise
        Point point = new Point(x, y);
        Deplacement depl = parseDeplacement(deplacement, x, y);
        Balise bal = new Balise(depl, point, config);
        balises.add(bal);

        // abonnement a la vue
        ViewHeadless logger = new ViewHeadless();
        bal.getEventHandler().registerListener(MoveEvent.class, logger);
        bal.getEventHandler().registerListener(StartSyncEvent.class, logger);
        bal.getEventHandler().registerListener(EndSyncEvent.class, logger);

        // abonnement a tous les satellites existants
        for(Satellite s : satellites) bal.getEventHandler().registerListener(AskSyncEvent.class, s);

        return bal;
    }

    @Override
    public void start() {
        if(running) return;
        for(Satellite s : satellites) s.start();
        for(Balise b : balises) b.start();
    }

    @Override
    public void stop() {
        if(!running) return;
        for(Satellite s : satellites) s.stop();
        for(Balise b : balises) b.stop();
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
