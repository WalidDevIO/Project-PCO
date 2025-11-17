import com.ubo.paco.config.Config;
import com.ubo.paco.config.DefaultConfig;
import com.ubo.paco.simulation.Simulation;
import com.ubo.paco.simulation.SimulationHeadless;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Config config = new DefaultConfig();
        Random rand = new Random();
        Simulation simulation = new SimulationHeadless(config);

        simulation.createBalise(
                rand.nextInt(0, config.getWinWidth()),
                rand.nextInt(config.getSeaLevel() + config.getSeaThreshold(), config.getWinHeight()),
                "horizontal"
        );

        simulation.createSatellite(
                rand.nextInt(0, config.getWinWidth()),
                rand.nextInt(0, config.getSeaLevel() - config.getSeaThreshold())
        );

        simulation.createBalise(
                rand.nextInt(0, config.getWinWidth()),
                rand.nextInt(config.getSeaLevel() + config.getSeaThreshold(), config.getWinHeight()),
                "sinusoidal"
        );

        simulation.start();
    }
}