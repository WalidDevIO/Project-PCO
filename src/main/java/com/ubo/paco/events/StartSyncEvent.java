package com.ubo.paco.events;

import com.ubo.paco.view.ViewElementMobile;

public class StartSyncEvent extends AbstractEvent {

    public StartSyncEvent(Object source) {
        super(source);
    }

    @Override
    public void sendTo(Object target) {
        ((ViewElementMobile)target).onSyncStart(this);
    }
    
}
