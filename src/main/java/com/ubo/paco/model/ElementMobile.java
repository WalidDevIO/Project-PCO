package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;

import java.awt.*;

public class ElementMobile {
    private Deplacement depl;
    private Point gpsLoc;
    private Boolean inSync=false;

    public ElementMobile(Deplacement deplacement, Point point){
        this.depl=deplacement;
        this.gpsLoc=point;
    }

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
