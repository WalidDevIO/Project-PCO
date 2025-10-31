package com.ubo.paco.graphicsElement.PositionStrategy;

import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;

import java.awt.*;

public abstract class PositionStrategy<C extends Component, S extends Component> {
    abstract public void position(C child, S sync, NiRectangle parent, ElementMobile model);
}

