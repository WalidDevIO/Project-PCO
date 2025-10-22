package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;

import java.awt.*;

public class Balise extends ElementMobile {

    private int data=0;

    public Balise(Deplacement deplacement, Point point) {
        super(deplacement, point);
    }

    public int getData() {
        return data;
    }

    private void incr(){
        this.data++;
    }
}
