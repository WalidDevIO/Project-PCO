package com.ubo.paco.graphicsElement;

import nicellipse.component.NiEllipse;

import javax.swing.*;
import java.awt.*;

public class NiSync extends JComponent {

    public NiSync() {
        this.setOpaque(false);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setDoubleBuffered(true);

        int numberOfRings = 6;
        int spacing = 30;
        int maxSize = numberOfRings * spacing * 2;
        this.setSize(maxSize, maxSize);

        for (int i = 1; i <= numberOfRings; i++) {
            NiEllipse ellipse = new NiEllipse();
            ellipse.setOpaque(false);
            ellipse.setBackground(new Color(0, 0, 0, 0));
            ellipse.setDoubleBuffered(true);
            int size = i * spacing * 2;
            int offset = (maxSize - size) / 2; // centrer horizontalement et verticalement
            ellipse.setBounds(offset, offset, size, size);
            this.add(ellipse);
        }
    }
}