package com.ubo.paco.view;

import com.ubo.paco.events.EndSyncEvent;
import com.ubo.paco.events.MoveEvent;
import com.ubo.paco.graphicsElement.PositionStrategy.PositionStrategy;
import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;
import com.ubo.paco.events.StartSyncEvent;

import java.awt.*;

public class ViewElementMobile<C extends Component, S extends Component> extends NiRectangle {
    private ElementMobile model;
    private C component;
    private S syncComponent;
    private PositionStrategy<C,S> strategyPosition;

    public ViewElementMobile(C component, S syncComponent, PositionStrategy<C,S> strategyPosition, ElementMobile model) {
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

    public C getComponent() {
        return component;
    }

    public void setComponent(C component) {
        this.component = component;
    }

    public S getSyncComponent() {
        return syncComponent;
    }

    public void setSyncComponent(S syncComponent) {
        this.syncComponent = syncComponent;
    }

    public PositionStrategy<C, S> getStrategyPosition() {
        return strategyPosition;
    }

    public void setStrategyPosition(PositionStrategy<C, S> strategyPosition) {
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
