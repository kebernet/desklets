package org.glossitope.container.wm.buffered;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;


public class DeskletToplevel extends JPanel {

    private BufferedDeskletContainer container;
    DeskletToplevel(BufferedDeskletContainer container) {
        setOpaque(false);
        this.container = container;
        //this.setDoubleBuffered(false);
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setColor(Color.GREEN);
        //g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g2);
        g2.dispose();
        //g.setColor(Color.RED);
        //g.drawString("desklet top level",5,15);
    }
    
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintChildren(g2);
        g2.dispose();
    }

    BufferedDeskletContainer getContainer() {
        return container;
    }
}