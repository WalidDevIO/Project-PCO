package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.StartSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.ViewEventReceiver;

public class ViewHeadless implements ViewEventReceiver {

    int tickCount = 0;

    @Override
    public void onMove(MoveEvent event) {
        if(tickCount % 1000 == 0) {
            System.out.println("----- Headless View Move Event (All 1000 ticks) -----");
            System.out.println("Move event received for source: " + event.getSource().getClass().getSimpleName());
            System.out.println("New position: " + event.getPosition());
        }
        tickCount++;
    }

    @Override
    public void onSyncStart(StartSyncEvent event) {
        System.out.println("----- Headless View Sync Start Event -----");
        System.out.println("Sync start event received for source: " + event.getSource().getClass().getSimpleName());
    }

    @Override
    public void onSyncEnd(EndSyncEvent event) {
        System.out.println("----- Headless View Sync End Event -----");
        System.out.println("Sync end event received for source: " + event.getSource().getClass().getSimpleName());
    }
    
}
