package com.ubo.paco;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ubo.paco.deplacement.DeplacementHorizontal;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.EventHandler;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.StartSyncEvent;
import com.ubo.paco.graphicsElement.*;
import com.ubo.paco.graphicsElement.PositionStrategy.CenterPositionStrategy;
import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import com.ubo.paco.view.ViewElementMobile;

import nicellipse.component.NiEllipse;
import nicellipse.component.NiRectangle;
import nicellipse.component.NiSpace;

/**
 * Hello world!
 */
public class App {

    Config config = new DefaultConfig();
    NiSpace space;
    List<Satellite> satellitesPool = new ArrayList<>();
    List<Thread> threadPool = new ArrayList<>();

    public static void main(String[] args) {
        new App();
    }

    int random(int min, int max){
        return new Random().nextInt(min, max);
    }

    void generateBalise(){
        Point randomPoint = new Point(
                0,
                random(config.getSeaLevel() + 50, config.getWinHeight())
        );
        Balise balise = new Balise(
                new DeplacementHorizontal(random(1, 5), config),
                randomPoint,
                config
        );
        ViewElementMobile<NiBalise, NiSync> view = new ViewElementMobile<>(new NiBalise(), new NiSync(), new CenterPositionStrategy<>(), balise);
        EventHandler eventHandler = balise.getEventHandler();
        eventHandler.registerListener(MoveEvent.class, view);
        eventHandler.registerListener(StartSyncEvent.class, view);
        eventHandler.registerListener(EndSyncEvent.class, view);
        for(Satellite satellite : satellitesPool){
            balise.getEventHandler().registerListener(AskSyncEvent.class, satellite);
        }
        space.add(view);
        threadPool.add(new Thread(balise));
    }

    void generateSatellite(){
        Point randomPoint = new Point(
                0,
                random(0, config.getSeaLevel() - 50)
        );
        Satellite satellite = new Satellite(
                new DeplacementHorizontal(random(1, 5), config),
                randomPoint,
                config
        );
        satellitesPool.add(satellite);
        ViewElementMobile<NiSatellite, NiSync> view = new ViewElementMobile<>(new NiSatellite(), new NiSync(), new CenterPositionStrategy<>(), satellite);
        EventHandler eventHandler = satellite.getEventHandler();
        eventHandler.registerListener(MoveEvent.class, view);
        eventHandler.registerListener(StartSyncEvent.class, view);
        eventHandler.registerListener(EndSyncEvent.class, view);
        space.add(view);
        threadPool.add(new Thread(satellite));
    }

    App(){
        space = new NiSpace("PACO Simulation", new Dimension(
                config.getWinWidth(),
                config.getWinHeight()
        ));

        space.openInWindow();


        for(int i = 0; i < 5; i++){
            generateSatellite();
        }
        for(int i = 0; i < 6; i++){
            generateBalise();
        }


        NiSea sea = new NiSea(config);
        space.add(sea);

        NiSky sky = new NiSky(config);
        space.add(sky);


        space.repaint();
        for(Thread thread : threadPool){
            thread.start();
        }

    }
}
