/*
 * CustomDesktopPane.java
 *
 * Created on June 5, 2006, 7:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JDesktopPane;

/**
 *
 * @author joshy
 */
public class CustomDesktopPane extends JDesktopPane {
    
    /** Creates a new instance of CustomDesktopPane */
    private DesktopBackground desktopBackground;
    
    public CustomDesktopPane() {
    }
    
    /*
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        for(int x=0; x<getWidth(); x=x+10) {
            for(int y=0; y<getHeight(); y=y+10) {
                g.drawLine(x+animVal,    y,
                           x+10-animVal, y+10);
            }
        }
    }
     */
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        if(desktopBackground != null) {
            desktopBackground.paint((Graphics2D)g);
        }
    }

    public void setDesktopBackground(DesktopBackground bg) {
        bg.setDesktopPane(this);
        this.desktopBackground = bg;
        repaint();
    }
    
}
