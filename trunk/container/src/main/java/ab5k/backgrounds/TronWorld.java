/*
 * TronWorld.java
 *
 * Created on July 3, 2006, 6:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.backgrounds;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import ab5k.DesktopBackground;

/**
 *
 * @author joshy
 */
public class TronWorld extends DesktopBackground {
    protected int animVal = 0;

    /** Creates a new instance of TronWorld */
    public TronWorld() {
        setBackgroundName("Tron World");
        new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                animVal = (animVal+1)%30;
                if(getDesktopPane() != null) {
                    getDesktopPane().repaint();
                }
            }
        }).start();
    }

    public void paint(Graphics2D g) {
        int w = getDesktopPane().getWidth();
        int h = getDesktopPane().getHeight();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);//getDesktopPane().getWidth(),getDesktopPane().getHeight());
        
        g.setColor(Color.GREEN);

        for(int i=0; i<10; i++) {
            g.drawLine(0,i*50+animVal*5, getDesktopPane().getWidth(), i*50+animVal*5);
        }
        
        // slanted lines
        int gap = w/8;
        for(int i=0; i<=8; i++) {
            g.drawLine(w/2,0,
                    i*gap,getDesktopPane().getHeight());
        }
        
        for(int i=0; i<8; i++) {
            g.drawLine(w/2,0, 0, h-gap*i);
            g.drawLine(w/2,0, w, h-gap*i);
        }
        
        
        g.setColor(Color.BLACK);
        g.fillRect(0,0,w,200);
        
        Polygon poly = new Polygon();
        poly.addPoint(0,200);
        poly.addPoint(200,50);
        poly.addPoint(250,75);
        poly.addPoint(500,25);
        
        poly.addPoint(w,200);
        poly.addPoint(0,200);
        
        g.setColor(Color.GREEN);
        g.drawPolygon(poly);
    }
    
}
