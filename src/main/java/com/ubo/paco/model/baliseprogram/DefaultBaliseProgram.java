package com.ubo.paco.model.baliseprogram;

import com.ubo.paco.config.Config;
import com.ubo.paco.deplacement.DeplacementImmobile;
import com.ubo.paco.deplacement.DeplacementRemontee;
import com.ubo.paco.events.AskSyncEvent;
import com.ubo.paco.model.Balise;

public class DefaultBaliseProgram extends BaliseProgram {
    public DefaultBaliseProgram(Balise b, Config c) {
        super(b, c);
    }

    @Override
    public void tick() {
        if(!balise.haveToCollectData() && !balise.isDeplacementDone()){
            balise.setDeplacement(new DeplacementRemontee(config.getLinearMovementSpeed(), config));
        }

        // Déplacement terminé on passe en mode immobile et on demande une synchronisation
        if(balise.isDeplacementDone()) {
            //On est descendu au fond, on veut se déplacer aléatoirement
            if(balise.haveToCollectData()) {
                balise.setDeplacement(config.getBaliseRandomDeplacementStrategy(balise.getGpsLoc()));
            } else {
                //On est remonté en surface, on attend la synchronisation
                balise.setDeplacement(new DeplacementImmobile(0, config));
                balise.getEventHandler().send(new AskSyncEvent(balise));
            }
        }
    }
}
