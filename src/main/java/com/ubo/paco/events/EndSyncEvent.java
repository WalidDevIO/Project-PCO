package com.ubo.paco.events;

import com.ubo.paco.view.ViewElementMobile;

/**
 * Événement envoyé à la vue par un élément mobile pour indiquer que la synchronisation est terminée
 */
public class EndSyncEvent extends AbstractEvent {
    
    public EndSyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((ViewElementMobile)target).onSyncEnd(this);
    }
}
