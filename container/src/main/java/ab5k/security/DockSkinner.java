/*
 * DockSkinner.java
 *
 * Created on February 27, 2007, 7:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ab5k.security;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.MouseTrigger;
import org.jdesktop.animation.timing.triggers.MouseTriggerEvent;
import org.jdesktop.swingx.JXBoxPanel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXInsets;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.GlowPathEffect;
import org.joshy.util.u;

import ab5k.MainPanel;
import ab5k.util.AnimRepainter;


/**
 *
 * @author joshy
 */
public class DockSkinner {
    /** Creates a new instance of DockSkinner */
    public enum Theme { ORANGE, DARK };
    public static Theme theme = Theme.DARK;
    
    public DockSkinner() {
    }
    
    public static void configureDockConatiner(final DockContainer dockCont) {
        dockCont.panel.setPadding(new Insets(5,5,5,5));
        dockCont.panel.setBorder(BorderFactory.createEmptyBorder());
        dockCont.panel.setMargins(new Insets(0,0,0,0));
        
        final AlphaPainter hiPainter = new AlphaPainter();
        hiPainter.setAlpha(0f);
        hiPainter.setCacheable(false);
        
        if(theme == Theme.ORANGE) {
            dockCont.panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 218, 218)));
            RectanglePainter rp = new RectanglePainter(new Color(234, 214, 171), new Color(215, 116, 0));
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
            hiPainter.setPainters(rp);
            dockCont.panel.setBackgroundPainter(hiPainter);
        }
        if(theme == Theme.DARK) {
            //dockCont.panel.setMargins(new Insets(2,35,1,4));
            dockCont.panel.setMargins(new Insets(2,8,1,8));
            dockCont.panel.setPadding(new Insets(5,8,5,5));
            RectanglePainter rp = new RectanglePainter(new Color(0xffaa90aa), new Color(0xff4E515F));
            rp.setInsets(new Insets(6,6,6,6));
            rp.setBorderWidth(1f);
            rp.setStyle(RectanglePainter.Style.BOTH);
            rp.setRoundWidth(4);
            rp.setRoundHeight(4);
            rp.setRounded(false);
            
            GlowPathEffect glow = new GlowPathEffect();
            glow.setBrushColor(Color.WHITE);
            glow.setEffectWidth(10);
            rp.setAreaEffects(glow);
            hiPainter.setPainters(rp);
            
            RectanglePainter rp2 = new RectanglePainter(new Color(0xffaaaaaa), new Color(255, 0, 0));
            rp2.setInsets(new JXInsets(5));
            rp2.setStyle(RectanglePainter.Style.FILLED);
            rp2.setRounded(true);
            rp2.setRoundHeight(5);
            rp2.setRoundWidth(5);
            rp2.setBorderWidth(0f);
            rp2.setFillPaint(new Color(255,255,255,80));;
            
            CompoundPainter cp = new CompoundPainter(rp2,hiPainter);
            cp.setCacheable(false);
            dockCont.panel.setBackgroundPainter(cp);
        }
        
        
        
        // create a .3 sec anim
        final Animator anim = new Animator(300);
        anim.addTarget(new PropertySetter(hiPainter,"alpha",0f,1f));
        
        // make the container repaint properly
        anim.addTarget(new AnimRepainter(dockCont.panel));
        MouseTrigger.addTrigger(dockCont.panel,anim,MouseTriggerEvent.ENTER,true);
    }
    
    public static void skinDock(MainPanel main) {
        try {
            
            JXButton logo = (JXButton) main.logoButton;
            logo.setText("");
            logo.setOpaque(false);
            
            JXBoxPanel dockPanel = (JXBoxPanel) main.dockPanel;
            dockPanel.setOpaque(true);
            
            if(theme == Theme.ORANGE) {
                logo.setIcon(new ImageIcon(
                        DockSkinner.class.getResource("/skins/dock/orange/images/logo.png")));
                GradientPaint grad = new GradientPaint(0,0, new Color(218,218,218),
                        (float)dockPanel.getPreferredSize().getWidth(),0, new Color(255,255,255));
                dockPanel.setBackgroundPainter(new CompoundPainter(
                        new RectanglePainter(grad, Color.GRAY, 1, RectanglePainter.Style.BOTH)
                        ));
            }
            if(theme == Theme.DARK) {
                logo.setIcon(new ImageIcon(
                        DockSkinner.class.getResource("/skins/dock/dark/images/dock3.logo.png")));
                BufferedImage bg = ImageIO.read(DockSkinner.class.getResource("/skins/dock/dark/images/dock3b.bg.png"));
                BufferedImage bg2 = ImageIO.read(DockSkinner.class.getResource("/skins/dock/dark/images/dock4.bg.png"));
                ImagePainter ptr = new ImagePainter(bg, ImagePainter.HorizontalAlignment.LEFT, ImagePainter.VerticalAlignment.TOP);
                ptr.setVerticalRepeat(true);
                ImagePainter ptr2 = new ImagePainter(bg2, ImagePainter.HorizontalAlignment.LEFT, ImagePainter.VerticalAlignment.TOP);
                ptr2.setScaleToFit(true);
                ptr2.setScaleType(ImagePainter.ScaleType.Distort);
                ptr2.setVerticalRepeat(false);
                
                RectanglePainter rp = new RectanglePainter();
                rp.setStyle(RectanglePainter.Style.BOTH);
                rp.setFillPaint(Color.RED);
                rp.setBorderPaint(Color.GRAY);
                rp.setRoundWidth(40);
                rp.setRoundHeight(20);
                rp.setRounded(false);
                
                
                MattePainter matteOverlay = new MattePainter();
                matteOverlay.setFillPaint(new GradientPaint(new Point(0,0), new Color(0,0,0,0), new Point(1,0), new Color(0,0,0,255)));
                matteOverlay.setPaintStretched(true);
                CompoundPainter comp = new CompoundPainter(rp,ptr2,matteOverlay);
                comp.setClipPreserved(true);
                
                ImagePainter im2 = new ImagePainter(DockSkinner.class.getResource("/skins/backgrounds/di-sails-blue-left.png"));
                im2.setHorizontalAlignment(ImagePainter.HorizontalAlignment.LEFT);
                RectanglePainter overlay = new RectanglePainter(new Color(255,255,255,80),Color.DARK_GRAY);
                overlay.setInsets(new JXInsets(5));
                comp.setPainters(im2,overlay);
                dockPanel.setBackgroundPainter(comp);
                dockPanel.setSize(300,dockPanel.getHeight());
                //dockPanel.setMargins(new JXInsets(5));
                
                configDarkButton((JXButton) main.manageButton);
                configDarkButton((JXButton) main.quitButton);
            }
            
            main.getSpinner().setVisible(false);
        } catch (Exception ex) {
            u.p(ex);
        }
    }

    private static void configDarkButton(final JXButton btn) {
        btn.setIcon(null);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        btn.setMargin(new JXInsets(0));
        btn.setToolTipText(null);
        btn.setForeground(Color.LIGHT_GRAY);
        Painter bg = new MattePainter(new GradientPaint(new Point(0,0), Color.BLACK, new Point(0,1), Color.DARK_GRAY),true);
        btn.setBackgroundPainter(bg);
        AbstractPainter fg = (AbstractPainter) btn.getForegroundPainter();
        RectanglePainter rect = new RectanglePainter(new Color(0,0,0,0), new Color(0,0,0,0));;
        rect.setInsets(new Insets(2,20,2,20));
        CompoundPainter comp = new CompoundPainter(rect,fg);
        comp.setCacheable(false);
        btn.setForegroundPainter(comp);
        
        
        Animator anim = PropertySetter.createAnimator(500,rect,"fillPaint",new Color(0,0,0,0), Color.DARK_GRAY);
        anim.addTarget(new PropertySetter(rect,"borderPaint", new Color(0,0,0,0), Color.BLACK));
        anim.addTarget(new AnimRepainter(btn));
        MouseTrigger.addTrigger(btn,anim,MouseTriggerEvent.ENTER,true);
    }
    
}
