package com.ubo.paco.events;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;

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
