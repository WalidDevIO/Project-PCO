import com.ubo.paco.config.Config;
import com.ubo.paco.config.DefaultConfig;
import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import com.ubo.paco.simulation.Simulation;
import com.ubo.paco.simulation.SimulationGraphique;
import com.ubo.paco.simulation.SimulationHeadless;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Config config = new DefaultConfig();
        Random rand = new Random();
        Simulation simulation = new SimulationHeadless(config);

        Balise bal1 = simulation.createBalise(
                rand.nextInt(0, config.getWinWidth()),
                rand.nextInt(config.getSeaLevel() + config.getSeaThreshold(), config.getWinHeight()),
                "horizontal"
        );

        Satellite sat1 = simulation.createSatellite(
                rand.nextInt(0, config.getWinWidth()),
                rand.nextInt(0, config.getSeaLevel() - config.getSeaThreshold())
        );

        Balise bal2 = simulation.createBalise(
                rand.nextInt(0, config.getWinWidth()),
                rand.nextInt(config.getSeaLevel() + config.getSeaThreshold(), config.getWinHeight()),
                "sinusoidal"
        );

        simulation.start();
    }
}