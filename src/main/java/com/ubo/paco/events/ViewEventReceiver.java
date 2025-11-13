package com.ubo.paco.events;

public interface ViewEventReceiver {
    void onMove(MoveEvent event);
    void onSyncStart(StartSyncEvent event);
    void onSyncEnd(EndSyncEvent event);
}
