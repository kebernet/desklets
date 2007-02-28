/*
 * DockSkinner.java
 *
 * Created on February 27, 2007, 7:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.security;

import ab5k.MainPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.GlowPathEffect;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class DockSkinner {
    
    /** Creates a new instance of DockSkinner */
    public DockSkinner() {
    }
    
    public static void skinDock(MainPanel main) {
        try {
            JXButton logo = (JXButton) main.logoButton;
            logo.setText("");
            logo.setIcon(new ImageIcon(
                    DockSkinner.class.getResource("/dock/orange/images/logo.png")));
            logo.setOpaque(false);
            //logo.setForegroundPainter(null);
            JXPanel dockPanel = (JXPanel) main.dockPanel;
            GradientPaint grad = new GradientPaint(0,0, new Color(218,218,218),
                    (float)dockPanel.getPreferredSize().getWidth(),0, new Color(255,255,255));
            dockPanel.setBackgroundPainter(new CompoundPainter(
                    new RectanglePainter(grad, Color.GRAY, 3, RectanglePainter.Style.BOTH)
                    ));
        } catch (Exception ex) {
            u.p(ex);
        }
    }
    
    public static void configureDockConatiner(final DockContainer dock) {
        final Border highlightBorder = 
                BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,3,0,new Color(218,218,218)),
                BorderFactory.createMatteBorder(5,5,5,5, new Color(215,116,0))
                );
        final Border normalBorder =
                BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,3,0,new Color(218,218,218)),
                BorderFactory.createEmptyBorder(5,5,5,5)
                );
        dock.panel.setBackground(Color.GREEN);
        dock.panel.setBorder(normalBorder);
        
        
        RectanglePainter rp = new RectanglePainter(
                new Color(234,214,171),new Color(215,116,0));
        rp.setInsets(new Insets(8,8,8,8));
        rp.setBorderWidth(1f);
        rp.setStyle(RectanglePainter.Style.BOTH);
        rp.setRoundWidth(4);
        rp.setRoundHeight(4);
        rp.setRounded(true);
        
        GlowPathEffect glow = new GlowPathEffect();
        glow.setBrushColor(new Color(215,116,0));
        rp.setPathEffects(glow);
        final Painter hiPainter = new CompoundPainter(rp);
        
        final Painter noPainter = new CompoundPainter();
        dock.panel.setBackgroundPainter(noPainter);
        
        MouseListener ml = new MouseListener() {
            public void mouseClicked(MouseEvent e) {  }
            public void mouseEntered(MouseEvent e) { 
                dock.panel.setBackgroundPainter(hiPainter); }
            public void mouseExited(MouseEvent e) {
                dock.panel.setBackgroundPainter(noPainter); }
            public void mousePressed(MouseEvent e) {  }
            public void mouseReleased(MouseEvent e) {  }
        };
        dock.panel.addMouseListener(ml);
        //dock.panel.setOpaque(true);
    }
    
}
