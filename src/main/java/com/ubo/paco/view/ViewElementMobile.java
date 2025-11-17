package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.graphicsElement.PositionStrategy.PositionStrategy;
import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;
import com.ubo.paco.events.StartSyncEvent;
import com.ubo.paco.events.ViewEventReceiver;

import java.awt.*;

/**
 * Représentation graphique d'un élément mobile
 */
public class ViewElementMobile extends NiRectangle implements ViewEventReceiver {
    private Component component;
    private Component syncComponent;
    private PositionStrategy strategyPosition;

    public ViewElementMobile(Component component, Component syncComponent, PositionStrategy strategyPosition) {
        this.syncComponent = syncComponent;
        this.component = component;
        this.strategyPosition = strategyPosition;

        this.setBorder(null);
        this.setOpaque(false);
        this.setLayout(null);

        this.add(component);
    }


    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getSyncComponent() {
        return syncComponent;
    }

    public void setSyncComponent(Component syncComponent) {
        this.syncComponent = syncComponent;
    }

    public PositionStrategy getStrategyPosition() {
        return strategyPosition;
    }

    public void setStrategyPosition(PositionStrategy strategyPosition) {
        this.strategyPosition = strategyPosition;
    }

    @Override
    public void onMove(MoveEvent event) {
        ElementMobile model = (ElementMobile) event.getSource();
        this.strategyPosition.position(this.component, syncComponent, this, model);
    }

    @Override
    public void onSyncStart(StartSyncEvent event) {
        this.add(syncComponent);
    }

    @Override
    public void onSyncEnd(EndSyncEvent event) {
        this.remove(syncComponent);
    }
}
