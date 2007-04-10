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
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

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

    public void setVisible(boolean b) {
        this.visible = b;
        
        // center it
        if(isDialog && this.visible) {
            BufferedDeskletContainer content = bdc.getProxy().contentContainer;
            Point2D cloc = content.getLocation();
            Dimension2D csize = content.getSize();
            Dimension2D size = this.getSize();
            Point2D loc = new Point2D.Double(cloc.getX() + (csize.getWidth()-size.getWidth())/2,
                    cloc.getY() + (csize.getHeight() - size.getHeight())/2);
            bdc.setLocation(loc);
        }
        this.bdc.wm.getRenderPanel().repaint();
    }

    public boolean isVisible() {
        return visible;
    }
    
    
}
