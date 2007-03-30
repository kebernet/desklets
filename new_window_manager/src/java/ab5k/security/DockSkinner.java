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
import ab5k.util.AnimRepainter;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.MouseTrigger;
import org.jdesktop.animation.timing.triggers.MouseTriggerEvent;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.GlowPathEffect;

import org.joshy.util.u;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;


/**
 *
 * @author joshy
 */
public class DockSkinner {
    /** Creates a new instance of DockSkinner */
    public DockSkinner() {
    }
    
    public static void configureDockConatiner(final DockContainer dock) {
        
        final Border highlightBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(
                0, 0, 3, 0, new Color(218, 218, 218)),
                BorderFactory.createMatteBorder(5, 5, 5, 5,
                new Color(215, 116, 0)));
        final Border normalBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, new Color(218, 218, 218)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //dock.panel.setBackground(Color.GREEN);
        dock.panel.setBorder(normalBorder);
        
        RectanglePainter rp = new RectanglePainter(new Color(234, 214, 171),
                new Color(215, 116, 0));
        rp.setInsets(new Insets(8, 8, 8, 8));
        rp.setBorderWidth(1f);
        rp.setStyle(RectanglePainter.Style.BOTH);
        rp.setRoundWidth(4);
        rp.setRoundHeight(4);
        rp.setRounded(true);
        
        GlowPathEffect glow = new GlowPathEffect();
        glow.setBrushColor(new Color(215, 116, 0));
        glow.setEffectWidth(10);
        rp.setAreaEffects(glow);
        
        final AlphaPainter hiPainter = new AlphaPainter();
        hiPainter.setPainters(rp);
        hiPainter.setAlpha(0f);
        
        dock.panel.setBackgroundPainter(hiPainter);
        
        // create a .3 sec anim
        final Animator anim = new Animator(300);
        anim.addTarget(new PropertySetter(hiPainter,"alpha",0f,1f));
        
        // make the container repaint properly
        anim.addTarget(new AnimRepainter(dock.panel));
        MouseTrigger.addTrigger(dock.panel,anim,MouseTriggerEvent.ENTER,true);
        //dock.panel.setOpaque(true);
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
                    new RectanglePainter(grad, Color.GRAY, 1, RectanglePainter.Style.BOTH)
                    ));
        } catch (Exception ex) {
            u.p(ex);
        }
    }
    
}
