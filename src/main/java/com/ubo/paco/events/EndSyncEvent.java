package com.ubo.paco.events;

import com.ubo.paco.view.ViewElementMobile;

public class EndSyncEvent extends AbstractEvent {
    
    public EndSyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((ViewElementMobile)target).onSyncEnd(this);
    }
}
