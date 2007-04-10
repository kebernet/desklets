/*
 * Buffered2DPeer.java
 *
 * Created on April 9, 2007, 5:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.util.AnimRepainter;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author joshy
 */
public class Buffered2DPeer extends DCPeer {
    private BufferedImage buffer;
    private float alpha = 1f;
    private double rotation = 0;
    private double scale = 1.0;
    private boolean dirty = true;
    Dimension2D size = new Dimension(50, 50);
    private Point2D location = new Point(-1, -1);

    private boolean visible = true;
    private boolean isDialog = false;

    private Shape clip;
    
    /** Creates a new instance of Buffered2DPeer */
    public Buffered2DPeer(BufferedDeskletContainer bdc, boolean dialog) {
        super(bdc);
        this.isDialog = dialog;
    }
    
    public Buffered2DPeer(BufferedDeskletContainer bdc) {
        this(bdc,false);
    }

    public Dimension2D getSize() {
        return size;
    }

    public void setSize(Dimension2D dimension2D) {
        Dimension2D old = this.size;
        if(!old.equals(dimension2D)) {
            setDirty(true);
            this.size = dimension2D;
        }
    }
    
    public Point2D getLocation() {
        return location;
    }
    
    public void setLocation(Point2D point) {
        this.location = point;
        setDirty(true);
        bdc.wm.getRenderPanel().repaint();
    }
    
    public BufferedImage getBuffer() {
        return buffer;
    }
    
    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }
    
    public float getAlpha() {
        return alpha;
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    
    public double getRotation() {
        return rotation;
    }
    
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    public double getScale() {
        return scale;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    void setDirty(boolean b) {
        this.dirty = b;
    }
    
    boolean isDirty() {
        return this.dirty;
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
        }
        //this.bdc.wm.getRenderPanel().repaint();
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
    /*
     *   public void setVisible(boolean visible) {
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
        if(bdc.wm.getRenderPanel() instanceof JComponent) {
            anim.addTarget(new AnimRepainter((JComponent)bdc.wm.getRenderPanel()));
        }
        return anim;
    }

    public void setClip(Shape clip) {
        this.clip = clip;
    }
    
    public Shape getClip() {
        return this.clip;
    }
}
