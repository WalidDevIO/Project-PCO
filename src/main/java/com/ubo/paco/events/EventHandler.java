package com.ubo.paco.events;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class EventHandler {

    Map<Class<? extends AbstractEvent>, Set<Object>> registry = new HashMap<>();

    public void send(AbstractEvent event) {
        Set<Object> listeners = registry.get(event.getClass());
        if(listeners == null) return;
        for (Object listener : listeners) {
            event.sendTo(listener);
        }
    }

    public void registerListener(Class<? extends AbstractEvent> eventType, Object listener) {
        Set<Object> listeners = registry.get(eventType);
        if(listeners == null) {
            listeners = new HashSet<>();
            registry.put(eventType, listeners);
        }
        listeners.add(listener);
    }

    public void unregisterListener(Class<? extends AbstractEvent> eventType, Object listener) {
        Set<Object> listeners = registry.get(eventType);
        if(listeners != null) {
            listeners.remove(listener);
        }
    }

}
