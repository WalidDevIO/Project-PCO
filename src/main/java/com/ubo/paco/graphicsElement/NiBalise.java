package com.ubo.paco.graphicsElement;

import javax.swing.*;
import java.awt.*;

public class NiBalise extends JComponent {
    private static final long serialVersionUID = 1L;

    public NiBalise() {
        setSize(50, 90);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setDoubleBuffered(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int bodyW = 22;
        int bodyH = 55;
        int bodyX = (w - bodyW) / 2;
        int bodyY = (h - bodyH) / 2;
        g2.setPaint(new GradientPaint(0, 0, new Color(70, 120, 150),
                0, h, new Color(30, 60, 90)));
        g2.fillRoundRect(bodyX, bodyY, bodyW, bodyH, 10, 10);

        int domeW = 30;
        int domeH = 20;
        int domeX = (w - domeW) / 2;
        int domeY = bodyY - domeH / 2;
        g2.setColor(new Color(80, 140, 180));
        g2.fillOval(domeX, domeY, domeW, domeH);

        int baseW = 35;
        int baseH = 15;
        int baseY = bodyY + bodyH - 5;
        g2.setColor(new Color(25, 45, 70));
        g2.fillOval((w - baseW) / 2, baseY, baseW, baseH);

        g2.setColor(new Color(180, 220, 255));
        g2.fillOval(bodyX + 5, bodyY + 15, 12, 12);
        g2.setColor(new Color(80, 120, 160));
        g2.drawOval(bodyX + 5, bodyY + 15, 12, 12);

        g2.dispose();
    }

}