package com.ubo.paco.events;

import com.ubo.paco.view.ViewElementMobile;

public class SyncEvent extends AbstractEvent {

    public SyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((ViewElementMobile)target).onSync(this);
    }
    
}
