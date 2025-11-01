package com.ubo.paco.graphicsElement.PositionStrategy;

import com.ubo.paco.model.ElementMobile;
import nicellipse.component.NiRectangle;

import java.awt.*;

public class CenterPositionStrategy implements PositionStrategy {
    @Override
    public void position(Component child, Component sync, NiRectangle parent, ElementMobile model) {
        // Taille du parent = max entre enfant et sync
        int width = Math.max(child.getWidth(), sync.getWidth());
        int height = Math.max(child.getHeight(), sync.getHeight());
        parent.setSize(width, height);

        // Le centre du parent = position du model
        Point modelPos = model.getGpsLoc(); // centre logique
        int parentX = modelPos.x - width / 2;
        int parentY = modelPos.y - height / 2;
        parent.setLocation(parentX, parentY);

        // Centrer l'enfant dans le parent
        int childX = (width - child.getWidth()) / 2;
        int childY = (height - child.getHeight()) / 2;
        child.setLocation(childX, childY);

        // Centrer le sync dans le parent
        int syncX = (width - sync.getWidth()) / 2;
        int syncY = (height - sync.getHeight()) / 2;
        sync.setLocation(syncX, syncY);
    }
}
