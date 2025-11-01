package com.ubo.paco.graphicsElement;

import com.ubo.paco.Config;
import nicellipse.component.NiRectangle;



public class NiSky extends NiRectangle {
    public NiSky() {
        super();
        this.setBackground(new java.awt.Color(135, 206, 235));
        this.setLocation(0, 0);
        this.setSize(Config.getConfig().getWinWidth(), Config.getConfig().getSeaLevel());
    }
}
