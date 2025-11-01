package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.graphicsElement.PositionStrategy.PositionStrategy;
import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;
import com.ubo.paco.events.StartSyncEvent;

import java.awt.*;

public class ViewElementMobile extends NiRectangle {
    private ElementMobile model;
    private Component component;
    private Component syncComponent;
    private PositionStrategy strategyPosition;

    public ViewElementMobile(Component component, Component syncComponent, PositionStrategy strategyPosition, ElementMobile model) {
        this.model = model;
        this.syncComponent = syncComponent;
        this.component = component;
        this.strategyPosition = strategyPosition;

        this.setBorder(null);
        this.setOpaque(false);
        this.setLayout(null);

        this.strategyPosition.position(this.component, syncComponent, this, model);

        this.add(component);
    }

    public ElementMobile getModel() {
        return model;
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

    public void onMove(MoveEvent event) {
        this.strategyPosition.position(this.component, syncComponent, this, model);
    }

    public void setModel(ElementMobile model) {
        this.model = model;
    }

    public void onSyncStart(StartSyncEvent event) {
        this.add(syncComponent);
    }

    public void onSyncEnd(EndSyncEvent event) {
        this.remove(syncComponent);
    }
}
