package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.StartSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.ViewEventReceiver;

/**
 * Représentation headless (lignes de logs) d'un élément mobile.
 * La position de l'élément est affichée à intervalle régulier dans les logs.
 */
public class ViewHeadless implements ViewEventReceiver {

    int tickCount = 0;
    int positionNotifyInterval = 200;

    @Override
    public void onMove(MoveEvent event) {
        if(tickCount % positionNotifyInterval == 0) {
            System.out.println(
                    "----- Headless View Move Event (All " + positionNotifyInterval + " ticks) -----\n"
                    + "Move event received for source: "
                    + event.getSource().getClass().getSimpleName()
                    + " "
                    + event.getSource().hashCode()
                    + "\nNew position: " + event.getPosition()
            );
            System.out.flush();
        }
        tickCount++;
    }

    @Override
    public void onSyncStart(StartSyncEvent event) {
        System.out.println(
                "----- Headless View Sync Start Event -----\n"
                + "Sync start event received for source: "
                + event.getSource().getClass().getSimpleName()
                + " "
                + event.getSource().hashCode()
        );
        System.out.flush();
    }

    @Override
    public void onSyncEnd(EndSyncEvent event) {
        System.out.println(
                "----- Headless View Sync End Event -----\n"
                + "Sync end event received for source: "
                + event.getSource().getClass().getSimpleName()
                + " "
                + event.getSource().hashCode()
        );
        System.out.flush();
    }
    
}
