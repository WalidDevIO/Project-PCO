package com.ubo.paco.graphicsElement;

import com.ubo.paco.config.Config;
import nicellipse.component.NiRectangle;

import java.awt.*;

public class NiSea extends NiRectangle {
    protected Config config;
    public NiSea(Config config) {
        super();
        this.config = config;
        this.setBackground(new Color(0, 105, 148));
        this.setLocation(0, config.getSeaLevel());
        this.setSize(config.getWinWidth(), config.getWinHeight() - config.getSeaLevel());
    }
}
