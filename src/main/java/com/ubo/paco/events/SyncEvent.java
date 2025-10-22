package com.ubo.paco.events;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;

public class SyncEvent extends AbstractEvent {

    public SyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((Satellite)target).onSync(this);
    }

    public Balise getBalise() {
        return (Balise) getSource();
    }
}
