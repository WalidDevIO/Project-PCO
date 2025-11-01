package com.ubo.paco.graphicsElement.PositionStrategy;

import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;

import java.awt.*;

public interface PositionStrategy {
    void position(Component child, Component sync, NiRectangle parent, ElementMobile model);
}