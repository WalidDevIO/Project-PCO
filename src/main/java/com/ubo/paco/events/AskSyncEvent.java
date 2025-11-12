package com.ubo.paco.events;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;

/**
 * Événement envoyé par une balise remontée à la surface pour demander sa synchronisation avec les satellites
 */
public class AskSyncEvent extends AbstractEvent {

    public AskSyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((Satellite)target).onSyncAsked(this);
    }

    public Balise getBalise() {
        return (Balise) getSource();
    }
}
