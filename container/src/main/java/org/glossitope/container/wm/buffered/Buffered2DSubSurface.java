package org.glossitope.container.wm.buffered;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author joshy
 */
public class Buffered2DSubSurface {
    
    /** Creates a new instance of BSurface */
    public Buffered2DSubSurface() {
    }
    
    private boolean visible = true;
    private Dimension2D size;
    private Point2D location = new Point(0,0);
    private double rotation = 0;
    private double scale = 1.0;
    private float alpha = 1f;
    
    private BufferedImage img;

    public Rectangle subRect;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Dimension2D getSize() {
        return size;
    }

    public void setSize(Dimension2D size) {
        this.size = size;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
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

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
    
}
