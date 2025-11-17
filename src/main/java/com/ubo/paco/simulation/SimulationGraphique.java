package com.ubo.paco.simulation;

import com.ubo.paco.config.Config;
import com.ubo.paco.config.DefaultConfig;
import com.ubo.paco.deplacement.*;
import com.ubo.paco.events.*;
import com.ubo.paco.graphicsElement.*;
import com.ubo.paco.graphicsElement.PositionStrategy.CenterPositionStrategy;
import com.ubo.paco.graphicsElement.PositionStrategy.PositionStrategy;
import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import com.ubo.paco.view.ViewElementMobile;
import nicellipse.component.NiSpace;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulation graphique : les satellites et les balises sont affichés dans une fenêtre
 */
public class SimulationGraphique implements Simulation {
    private Config config = new DefaultConfig();

    NiSpace space = new NiSpace("PACO Simulation", new Dimension(
            config.getWinWidth(),
            config.getWinHeight()
    ));
    private boolean initialized = false;

    private List<Satellite> satellites = new ArrayList<>();
    private List<Balise> balises = new ArrayList<>();

    private boolean running = false;

    public SimulationGraphique() {}

    public SimulationGraphique(Config config) {
        this.config = config;
    }

    public SimulationGraphique(Config config, NiSpace space) {
        this.config = config;
        this.space = space;
    }

    int random(int min, int max){
        return new Random().nextInt(min, max+1);
    }

    private void generateDecors() {
        space.add(new NiSea(config));
        space.add(new NiSky(config));
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
        Component spriteSatellite = new NiSatellite();
        Component spriteSync = new NiSync();
        PositionStrategy positionStrategy = new CenterPositionStrategy();
        ViewElementMobile view = new ViewElementMobile(spriteSatellite, spriteSync, positionStrategy);

        Point point = new Point(x, y);
        Deplacement depl = new DeplacementHorizontal(config.getRandomSpeed(), config);
        Satellite sat = new Satellite(depl, point, config);
        satellites.add(sat);
        space.add(view);
        space.setComponentZOrder(view, 0);

        // positionnement initial des composants de la vue
        positionStrategy.position(spriteSatellite, spriteSync, view, sat);

        // abonnement a la vue
        sat.getEventHandler().registerListener(MoveEvent.class, view);
        sat.getEventHandler().registerListener(StartSyncEvent.class, view);
        sat.getEventHandler().registerListener(EndSyncEvent.class, view);

        // abonnement des balises existants a ce nouveau satellite
        for(Balise b : balises) b.getEventHandler().registerListener(AskSyncEvent.class, sat);

        return sat;
    }

    @Override
    public Balise createBalise(int x, int y, String deplacement) {
        // creation de la balise
        Component spriteBalise = new NiBalise();
        Component spriteSync = new NiSync();
        PositionStrategy positionStrategy = new CenterPositionStrategy();
        ViewElementMobile view = new ViewElementMobile(spriteBalise, spriteSync, positionStrategy);

        Point point = new Point(x, y);
        Deplacement depl = parseDeplacement(deplacement, x, y);
        Balise bal = new Balise(depl, point, config);
        balises.add(bal);
        space.add(view);
        space.setComponentZOrder(view, 0);

        // positionnement initial des composants de la vue
        positionStrategy.position(spriteBalise, spriteSync, view, bal);

        // abonnement a la vue
        bal.getEventHandler().registerListener(MoveEvent.class, view);
        bal.getEventHandler().registerListener(StartSyncEvent.class, view);
        bal.getEventHandler().registerListener(EndSyncEvent.class, view);

        // abonnement a tous les satellites existants
        for(Satellite s : satellites) bal.getEventHandler().registerListener(AskSyncEvent.class, s);

        return bal;
    }

    @Override
    public void start() {
        if(!initialized) {
            space.openInWindow();
            this.generateDecors();
            space.repaint();
            initialized = true;
        }

        if(running) return;
        running = true;
        for(Satellite s : satellites) s.start();
        for(Balise b : balises) b.start();
    }

    @Override
    public void stop() {
        if(!running) return;
        running = false;
        for(Satellite s : satellites) s.stop();
        for(Balise b : balises) b.stop();
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
