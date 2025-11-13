package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.StartSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.events.ViewEventReceiver;

public class ViewHeadless implements ViewEventReceiver {

    @Override
    public void onMove(MoveEvent event) {
        System.out.println("Move event received for source: " + event.getSource().getClass().getSimpleName());
        System.out.println("New position: " + event.getPosition());
    }

    @Override
    public void onSyncStart(StartSyncEvent event) {
        System.out.println("Sync start event received for source: " + event.getSource().getClass().getSimpleName());
    }

    @Override
    public void onSyncEnd(EndSyncEvent event) {
        System.out.println("Sync end event received for source: " + event.getSource().getClass().getSimpleName());
    }
    
}
