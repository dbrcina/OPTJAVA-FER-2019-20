package hr.fer.zemris.optjava.dz12.gui.jcomponent;

import hr.fer.zemris.optjava.dz12.engine.GPEngine;

import javax.swing.*;
import java.awt.*;

public class Platform extends JComponent {

    private GPEngine engine;
    private boolean[][] platform;
    private int platformWidth;
    private int platformHeight;
    private int rectWidth;
    private int rectHeight;
    private Color foodColor = Color.decode("0xC88141");

    public Platform(GPEngine engine) {
        this.engine = engine;
        platform = engine.getPlatform();
        platformWidth = engine.getPlatformWidth() * 20;
        platformHeight = engine.getPlatformHeight() * 20;
        rectWidth = rectHeight = 20;
        setPreferredSize(new Dimension(platformWidth, platformHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int y = 0, j = 0; y < platformHeight; y += rectHeight, j++) {
            for (int x = 0, i = 0; x < platformWidth; x += rectWidth, i++) {
                if (platform[i][j]) {
                    g2d.setColor(foodColor);
                    g2d.fillOval(x, y, rectWidth, rectHeight);
                }
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, rectWidth, rectHeight);
            }
        }
        int x = engine.getCurrentX() * rectWidth;
        int y = engine.getCurrentY() * rectHeight;
        g2d.setColor(Color.BLACK);
        switch (engine.getDirection()) {
            case 0: // right
                g2d.fillPolygon(
                        new int[]{x, x, x + rectWidth},
                        new int[]{y, y + rectHeight, y + rectHeight / 2},
                        3);
                break;
            case 1: // down
                g2d.fillPolygon(
                        new int[]{x, x + rectWidth, x + rectWidth / 2},
                        new int[]{y, y, y + rectHeight},
                        3);
                break;
            case 2: // left
                g2d.fillPolygon(
                        new int[]{x, x + rectWidth, x + rectWidth},
                        new int[]{y + rectHeight / 2, y, y + rectHeight},
                        3);
                break;
            case 3: // up
                g2d.fillPolygon(
                        new int[]{x, x + rectWidth / 2, x + rectWidth},
                        new int[]{y + rectHeight, y, y + rectHeight},
                        3);
                break;
        }
    }
}
