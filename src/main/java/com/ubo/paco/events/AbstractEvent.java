package com.ubo.paco.events;

import java.util.EventObject;

public abstract class AbstractEvent extends EventObject {
    public AbstractEvent(Object source) {
        super(source);
    }

    public abstract void sendTo(Object target);
    
}
