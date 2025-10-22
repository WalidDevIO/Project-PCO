package com.ubo.paco.events;

import com.ubo.paco.events.AbstractEvent;
import java.awt.Point;

public class MoveEvent extends AbstractEvent {

    private final Point position;

    public MoveEvent(Object source, Point position) {
        super(source);
        this.position = position;
    }

    @Override
    public void sendTo(Object target) {
        ((ViewElementMobile)target).onMove(this);
    }

    public Point getPosition() {
        return position;
    }
    
}
