package com.ubo.paco.deplacement;

import com.ubo.paco.config.Config;
import com.ubo.paco.model.ElementMobile;

import java.util.Random;

public class DeplacementAutonome extends Deplacement {
    private Deplacement currentDeplacement;
    private Random rand = new Random();

    private Deplacement getRandomDeplacement(ElementMobile em) {
        int choice = rand.nextInt(2);
        return switch(choice) {
            case 0 -> new DeplacementHorizontal(config.getRandomSpeed(), config);
            case 1 -> new DeplacementImmobile(0, config);
            default -> new DeplacementSinusoide(config.getRandomSpeed(), em.getGpsLoc().x, em.getGpsLoc().y, config);
        };
    }

    public DeplacementAutonome(int speed, Config config) {
        super(speed, config);
    }

    @Override
    public void bouge(ElementMobile elementMobile) {
        if(currentDeplacement == null) {
            currentDeplacement = getRandomDeplacement(elementMobile);
        } else if(rand.nextInt(50) == 10) {
            Deplacement newDeplacement = getRandomDeplacement(elementMobile);
            if(newDeplacement.getClass() != currentDeplacement.getClass()) {
                currentDeplacement = newDeplacement;
                System.out.println("Decision autonome de changer de deplacement : " + currentDeplacement.getClass().getSimpleName());
            }
        }
        this.currentDeplacement.bouge(elementMobile);
    }
}
