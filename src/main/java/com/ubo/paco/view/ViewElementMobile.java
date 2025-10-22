package com.ubo.paco.view;

import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;

import java.awt.*;

public class ViewElementMobile extends NiRectangle {
    ElementMobile model;


    public ViewElementMobile() {
        this.setDimension(new Dimension(20,20));
    }

    public void onMove(MoveEvent event) {
        Point pos = event.getPosition();
        this.setLocation(pos);
    }

    public ViewElementMobile(ElementMobile model) {
        this.model = model;
    }

    public void setModel(ElementMobile model) {
        this.model = model;
    }
}
