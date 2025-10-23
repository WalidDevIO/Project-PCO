package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;
import com.ubo.paco.events.StartSyncEvent;

import java.awt.*;

public class ViewElementMobile extends NiRectangle {
    ElementMobile model;


    public ViewElementMobile() {
        this.setDimension(new Dimension(20,20));
    }

    public void onMove(MoveEvent event) {
        this.setLocation(event.getPosition());
    }

    public ViewElementMobile(ElementMobile model) {
        this.model = model;
    }

    public void setModel(ElementMobile model) {
        this.model = model;
    }

    public void onSyncStart(StartSyncEvent event) {
        // Afficher ondes de synchronisation
    }

    public void onSyncEnd(EndSyncEvent event) {
        // Supprimer ondes de synchronisation
    }
}
