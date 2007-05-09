/*
 * RadarSweep.java
 *
 * Created on July 3, 2006, 6:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.backgrounds;

import org.glossitope.container.DesktopBackground;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author joshy
 */
public class RadarSweep extends DesktopBackground {
    protected int animVal = 0;
    
    /** Creates a new instance of RadarSweep */
    public RadarSweep() {
        setBackgroundName("Radar Sweep");
        new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                animVal = (animVal+1)%30;
                if(getDesktopPane() != null) {
                    getDesktopPane().repaint();
                }
            }
        }).start();
    }

    public void paint(Graphics2D g) {
        if (getDesktopPane() == null) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
        g.setColor(Color.GREEN);
        double angle = 360.0 / 30.0 * (double) animVal;
        for (int i = 0; i < 30; i++) {
            drawSweep(g, new Color(0, 1.0f - i * 0.033f, 0), angle - i * 2);
        }
    }

    private void drawSweep(final Graphics2D g, Color color, double angle) {
        g.setColor(color);
        angle = angle * 2.0 * Math.PI / 360.0;
        int cx = desktopPane.getWidth() / 2;
        int cy = desktopPane.getHeight() / 2;
        double len = Math.min(desktopPane.getWidth(), desktopPane.getHeight()) / 2;
        int ox = cx + (int) (Math.cos(angle) * len);
        int oy = cy + (int) (Math.sin(angle) * len);
        g.drawLine(cx, cy, ox, oy);
    }
    
}
