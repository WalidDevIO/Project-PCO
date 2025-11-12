package com.ubo.paco;

import com.ubo.paco.config.Config;
import com.ubo.paco.config.DefaultConfig;
import com.ubo.paco.deplacement.DeplacementHorizontal;
import com.ubo.paco.events.*;
import com.ubo.paco.graphicsElement.*;
import com.ubo.paco.graphicsElement.PositionStrategy.CenterPositionStrategy;
import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import com.ubo.paco.view.ViewElementMobile;
import nicellipse.component.NiSpace;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    Config config = new DefaultConfig();
    NiSpace space = new NiSpace("PACO Simulation", new Dimension(
            config.getWinWidth(),
            config.getWinHeight()
    ));
    List<Satellite> satellitesPool = new ArrayList<>();
    List<Thread> threadPool = new ArrayList<>();

    public Simulation() {
    }

    public Simulation(Config config, NiSpace space) {
        this.config = config;
        this.space = space;
    }

    public ViewElementMobile addSatellite(Component component, Component syncComponent, CenterPositionStrategy strategyPosition, Satellite satellite) {
        satellitesPool.add(satellite);
        ViewElementMobile view = new ViewElementMobile(component, syncComponent, strategyPosition);
        EventHandler eventHandler = satellite.getEventHandler();
        eventHandler.registerListener(MoveEvent.class, view);
        eventHandler.registerListener(StartSyncEvent.class, view);
        eventHandler.registerListener(EndSyncEvent.class, view);
        space.add(view);
        threadPool.add(new Thread(satellite));
        return view;
    }

    public ViewElementMobile addBalise(Component component, Component syncComponent, CenterPositionStrategy strategyPosition, Balise balise) {
        ViewElementMobile view = new ViewElementMobile(component, syncComponent, strategyPosition);
        EventHandler eventHandler = balise.getEventHandler();
        eventHandler.registerListener(MoveEvent.class, view);
        eventHandler.registerListener(StartSyncEvent.class, view);
        eventHandler.registerListener(EndSyncEvent.class, view);
        for(Satellite satellite : satellitesPool){
            balise.getEventHandler().registerListener(AskSyncEvent.class, satellite);
        }
        space.add(view);
        threadPool.add(new Thread(balise));
        return view;
    }


    public List<ViewElementMobile> generateSatellites(int count) {

        List<ViewElementMobile> satellites = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            satellites.add(
                this.addSatellite(
                        new NiSatellite(),
                        new NiSync(),
                        new CenterPositionStrategy(),
                        new Satellite(
                                new DeplacementHorizontal(config.getRandomSpeed(), config),
                                new Point(0,random(0, config.getSeaLevel() - config.getSeaThreshold())),
                                config
                        )
                )
            );
        }
        return satellites;
    }

    public List<ViewElementMobile> generateBalises(int count) {
        List<ViewElementMobile> balises = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Point origin = new Point(
                    random(0, config.getWinWidth()),
                    random(config.getSeaLevel() + config.getSeaThreshold(), config.getWinHeight())
            );
            balises.add(
                this.addBalise(
                        new NiBalise(),
                        new NiSync(),
                        new CenterPositionStrategy(),
                        new Balise(
                                config.getBaliseRandomDeplacementStrategy(origin),
                                origin,
                                config
                        )
                )
            );
        }
        return balises;
    }

    int random(int min, int max){
        return new Random().nextInt(min, max+1);
    }

    public void generateDecors() {
        space.add(new NiSea(config));
        space.add(new NiSky(config));
    }

    public void startSimulation() {
        space.openInWindow();
        this.generateDecors();
        space.repaint();
        for (Thread thread : threadPool) {
            thread.start();
        }
    }
}
