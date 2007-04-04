/*
 * BufferedDialogContainer.java
 *
 * Created on March 27, 2007, 5:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.security.DefaultContext;
import ab5k.util.AnimRepainter;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class BufferedDialogContainer extends BufferedDeskletContainer {
    
    private BufferedDeskletContainer parent;
    
    private boolean packed = false;
    
    /** Creates a new instance of BufferedDialogContainer */
    public BufferedDialogContainer(BufferedWM wm, DefaultContext context, BufferedDeskletContainer parent) {
        super(wm, context);
        this.parent = parent;
    }
    
    
    public void setVisible(boolean visible) {
        if(!packed) {
            pack();
            packed = true;
            double x = parent.getLocation().getX() + parent.getSize().getWidth()/2 - getSize().getWidth()/2;
            double y = parent.getLocation().getY() + parent.getSize().getHeight()/2 - getSize().getHeight()/2;
            setLocation(new Point2D.Double(x,y));
            setDirty(true);
        }
        if(visible) {
            super.setVisible(visible);
            startOpenAnimation().start();
        } else {
            Animator anim = startOpenAnimation();
            anim.setDirection(Animator.Direction.BACKWARD);
            anim.setInitialFraction(1f);
            anim.addTarget(new TimingTarget() {
                public void begin() {
                }
                public void end() {
                    BufferedDialogContainer.super.setVisible(false);
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            });
            anim.start();
        }
    }
    /*
    private Animator startCloseAnimation() {
        Animator anim = new Animator(600);
        final Rectangle clip = new Rectangle(0,0,0,0);
        anim.addTarget(new PropertySetter(clip,"location",
                new Point((int)getSize().getWidth()/2,0),
                new Point(0,0)));
        anim.addTarget(new PropertySetter(clip,"size",
                new Dimension(0,(int)getSize().getHeight()), getSize()));
        setClip(clip);
        anim.addTarget(new AnimRepainter(wm.panel));
        return anim;
    }
     */
    
    private Animator startOpenAnimation() {
        Animator anim = new Animator(400);
        final Rectangle clip = new Rectangle(0,0,0,0);
        anim.addTarget(new PropertySetter(this,"alpha",0f,0.8f));
        anim.addTarget(new PropertySetter(this,"rotation",Math.PI,0.0));
        anim.addTarget(new PropertySetter(clip,"location",
                new Point((int)getSize().getWidth()/2,0),
                new Point(0,0)));
        anim.addTarget(new PropertySetter(clip,"size",
                new Dimension(0,(int)getSize().getHeight()), getSize()));
        setClip(clip);
        anim.addTarget(new AnimRepainter(wm.panel));
        return anim;
    }
}
