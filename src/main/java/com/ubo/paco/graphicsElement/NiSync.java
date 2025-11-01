package com.ubo.paco.graphicsElement;

import javax.swing.*;
import java.awt.*;

public class NiSync extends JComponent {

    private final int numberOfRings = 6;
    private final int spacing = 30;
    private final int strokeWidth = 2;

    public NiSync() {
        this.setOpaque(false);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setDoubleBuffered(true);

        int maxSize = numberOfRings * spacing * 2;
        this.setPreferredSize(new Dimension(maxSize, maxSize));
        this.setSize(maxSize, maxSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (int i = 1; i <= numberOfRings; i++) {
            int size = i * spacing * 2;
            int offsetX = centerX - size / 2;
            int offsetY = centerY - size / 2;

            // Fade outer rings slightly
            float alpha = Math.max(0.12f, 1.0f - (i - 1) * 0.12f);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(offsetX, offsetY, size, size);
        }

        g2.dispose();
    }
}