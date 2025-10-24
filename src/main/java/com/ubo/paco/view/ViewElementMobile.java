package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;
import com.ubo.paco.events.StartSyncEvent;

import java.awt.*;
import java.time.Instant;

public class ViewElementMobile extends NiRectangle {
    ElementMobile model;
    ViewElementStatic viewElementStatic = new ViewElementStatic();
    Instant time;

    public ViewElementMobile(Color color) {
        this.setDimension(new Dimension(20,20));
        this.setBackground(color);
        viewElementStatic.setDimension(new Dimension(50,50));
    }

    public void onMove(MoveEvent event) {
        this.setLocation(event.getPosition());
    }

    public void setModel(ElementMobile model) {
        this.model = model;
    }

    public void onSyncStart(StartSyncEvent event) {
        // TODO: Afficher ondes de synchronisation
        System.out.println("onSyncStart");
        time = Instant.now();
        System.out.println(event.getSource() + ": " + time);
        this.add(viewElementStatic);
        this.repaint();

    }

    public void onSyncEnd(EndSyncEvent event) {
        // TODO: Supprimer ondes de synchronisation
        System.out.println("onSyncEnd");
        System.out.println(event.getSource() + ": " + time.toString());
        this.remove(viewElementStatic);
        this.repaint();
    }
}
