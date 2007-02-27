/*
 * GradientBackground.java
 *
 * Created on November 15, 2006, 2:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.backgrounds;

import ab5k.DesktopBackground;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.jdesktop.swingx.painter.PinstripePainter;

/**
 *
 * @author joshy
 */
public class GradientBackground extends DesktopBackground {
    
    /** Creates a new instance of GradientBackground */
    public GradientBackground() {
        setBackgroundName("Blue Gradient");
    }

    public void paint(Graphics2D g) {
        if (getDesktopPane() == null) {
            return;
        }
        
        g = (Graphics2D) g.create();
        // the background
        g.setPaint(new GradientPaint(new Point(0,0), 
                new Color(190,190,204),
                new Point(desktopPane.getWidth(), desktopPane.getHeight()),
                new Color(50,50,204)));
        g.fillRect(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
        
        PinstripePainter pin = new PinstripePainter(new Color(50,50,204,30), 45.0,8.0,8.0);
        Graphics2D gfx = (Graphics2D) g.create();
        pin.paint(gfx, desktopPane, desktopPane.getWidth(), desktopPane.getHeight());
        gfx.dispose();
        
        // the text
        String text = "AB5k";
        g.setColor(new Color(255,255,255,30));
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,400));
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(text,g);
        g.translate(desktopPane.getWidth()/2,desktopPane.getHeight()/2);
        g.translate(-(bounds.getX()+bounds.getWidth())/2,
                0-bounds.getY()-bounds.getHeight()/2);
        g.drawString(text,0,0);
        g.dispose();
    }
    
}