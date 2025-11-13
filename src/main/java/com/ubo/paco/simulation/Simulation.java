package com.ubo.paco.simulation;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;

public interface Simulation {
    public Satellite createSatellite(int x, int y);
    public Balise createBalise(int x, int y, String deplacement);
    public void start();
    public void stop();
    public boolean isRunning();
}
