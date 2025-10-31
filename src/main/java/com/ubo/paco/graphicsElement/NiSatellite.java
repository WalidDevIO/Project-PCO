package com.ubo.paco.graphicsElement;

import javax.swing.*;
import java.awt.*;



public class NiSatellite extends JComponent {
    private static final long serialVersionUID = 1L;

    public NiSatellite() {
        setSize(100, 50);
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

        int panelW = 25;
        int panelH = 20;
        int panelY = (h - panelH) / 2;
        g2.setPaint(new GradientPaint(0, 0, new Color(80, 130, 230),
                0, h, new Color(40, 80, 180)));
        g2.fillRoundRect(5, panelY, panelW, panelH, 4, 4);
        g2.fillRoundRect(w - panelW - 5, panelY, panelW, panelH, 4, 4);

        g2.setColor(new Color(0, 0, 0, 60));
        for (int x = 10; x < 30; x += 5)
            g2.drawLine(x, panelY + 2, x, panelY + panelH - 2);
        for (int x = w - 30; x < w - 5; x += 5)
            g2.drawLine(x, panelY + 2, x, panelY + panelH - 2);

        int coreW = 25;
        int coreH = 25;
        int coreX = (w - coreW) / 2;
        int coreY = panelY - 2; // toujours relatif au panneau
        g2.setPaint(new GradientPaint(0, 0, new Color(180, 180, 190),
                0, h, new Color(120, 120, 130)));
        g2.fillOval(coreX, coreY, coreW, coreH);

        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(coreX + coreW / 2, coreY + coreH / 2, coreX + coreW / 2, coreY - 10);
        g2.fillOval(coreX + coreW / 2 - 2, coreY - 12, 4, 4);

        g2.dispose();
    }

}