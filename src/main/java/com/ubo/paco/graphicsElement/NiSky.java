package com.ubo.paco.graphicsElement;

import com.ubo.paco.config.Config;
import nicellipse.component.NiRectangle;



public class NiSky extends NiRectangle {

    protected Config config;

    public NiSky(Config config) {
        super();
        this.config = config;
        this.setBackground(new java.awt.Color(135, 206, 235));
        this.setLocation(0, 0);
        this.setSize(config.getWinWidth(), config.getSeaLevel());
    }
}
