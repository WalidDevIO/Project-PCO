package com.ubo.paco.model.baliseprogram;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.Balise;

public abstract class BaliseProgram {
    protected Balise balise;
    protected Config config;

    public BaliseProgram(Balise b, Config c) {
        this.balise = b;
        this.config = c;
    }

    public abstract void tick();
}
