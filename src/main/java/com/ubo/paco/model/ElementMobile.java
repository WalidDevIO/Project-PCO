package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;

import java.awt.*;

public class ElementMobile {
    Deplacement depl;
    Point gpsLoc;

    

    public void bouge () {
        this.depl.bouge(this);
    }

    public void setDeplacement(Deplacement depl) {
        this.depl = depl;
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
}
