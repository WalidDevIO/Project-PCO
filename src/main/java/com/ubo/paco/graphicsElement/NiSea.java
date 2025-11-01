package com.ubo.paco.graphicsElement;

import com.ubo.paco.Config;
import nicellipse.component.NiRectangle;

import java.awt.*;

public class NiSea extends NiRectangle {
    public NiSea() {
        super();
        this.setBackground(new Color(0, 105, 148));
        this.setLocation(0, Config.getConfig().getSeaLevel());
        this.setSize(Config.getConfig().getWinWidth(), Config.getConfig().getWinHeight() - Config.getConfig().getSeaLevel());
    }
}
