/*
 * Buffered2DPeer.java
 *
 * Created on April 9, 2007, 5:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import ab5k.util.AnimRepainter;

/**
 *
 * @author joshy
 */
public class Buffered2DPeer extends BufferedPeer {
    private Point2D location = new Point(-1, -1);

    private boolean visible = true;
    private boolean isDialog = false;

    
    /** Creates a new instance of Buffered2DPeer */
    public Buffered2DPeer(BufferedDeskletContainer bdc, boolean dialog) {
        super(bdc);
        this.isDialog = dialog;
    }
    
    public Buffered2DPeer(BufferedDeskletContainer bdc) {
        this(bdc,false);
    }

    
    public Point2D getLocation() {
        return location;
    }
    
    public void setLocation(Point2D point) {
        this.location = point;
        //setDirty(true);
        bdc.wm.getRenderPanel().repaint();
    }

    @Override
    public void setSize(Dimension2D dimension2D) {
        super.setSize(dimension2D);
        bdc.wm.getRenderPanel().repaint();
    }
    
    
    

    public void setVisible(boolean visible) {
        // center it
        if(isDialog) {
            if(visible) {
                center();
                this.visible = true;
                startOpenAnimation().start();
            } else {
                // open animation in reverse
                Animator anim = startOpenAnimation();
                anim.setDirection(Animator.Direction.BACKWARD);
                anim.setInitialFraction(1f);
                anim.addTarget(new TimingTarget() {
                    public void begin() {
                    }
                    public void end() {
                        Buffered2DPeer.this.visible = false;
                    }
                    public void repeat() {
                    }
                    public void timingEvent(float f) {
                    }
                });
                anim.start();
            }
        } else {
            this.visible = visible;
        }
    }

    private void center() {
        BufferedDeskletContainer content = bdc.getProxy().contentContainer;
        Point2D cloc = content.getLocation();
        Dimension2D csize = content.getSize();
        Dimension2D size = this.getSize();
        Point2D loc = new Point2D.Double(cloc.getX() + (csize.getWidth()-size.getWidth())/2,
                cloc.getY() + (csize.getHeight() - size.getHeight())/2);
        bdc.setLocation(loc);
    }

    public boolean isVisible() {
        return visible;
    }
    
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
        if(bdc.wm.getRenderPanel() instanceof JComponent) {
            anim.addTarget(new AnimRepainter((JComponent)bdc.wm.getRenderPanel()));
        }
        return anim;
    }

    
    
}
