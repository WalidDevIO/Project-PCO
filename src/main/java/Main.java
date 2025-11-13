import com.ubo.paco.simulation.Simulation;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.generateSatellites(5);
        simulation.generateBalises(5);
        simulation.startSimulation();

    }
}