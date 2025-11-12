package com.ubo.paco.events;

import com.ubo.paco.view.ViewElementMobile;

/**
 * Événement envoyé à la vue par un élément mobile pour indiquer que celui-ci démarre une synchro
 */
public class StartSyncEvent extends AbstractEvent {

    public StartSyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((ViewElementMobile)target).onSyncStart(this);
    }
    
}
