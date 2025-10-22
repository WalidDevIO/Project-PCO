package com.ubo.paco.model;

import com.ubo.paco.deplacement.Deplacement;

import java.awt.*;

public class Balise extends ElementMobile implements Runnable {

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

    @Override
    public void run() {
        while (true){
            incr();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
