package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import ab5k.security.DefaultContext;
import ab5k.util.MoveMouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joshy.util.u;


public class BufferedDeskletContainer extends BaseDC {
    JComponent comp;
    private Point2D location = new Point(-1, -1);
    
    private boolean draggable = false;
    MoveMouseListener mml;
    
    private BufferedImage buffer;
    private float alpha = 1f;
    private double rotation = 0;
    private double scale = 1.0;

    private boolean dirty = true;

    private boolean visible = false;
    
    BufferedDeskletContainer(BufferedWM wm, DefaultContext context) {
        super(wm, context);
        comp = new DeskletToplevel();
        //comp.setBorder(BorderFactory.createLineBorder(Color.RED));
        comp.setLayout(new BorderLayout());
        mml = new MoveMouseListener(this, wm);
        setBackgroundDraggable(false);
    }
    
    Dimension2D size = new Dimension(50, 50);
    
    public Dimension2D getSize() {
        return size;
    }
    
    public void setSize(Dimension2D dimension2D) {
        comp.setSize((Dimension) dimension2D);
        this.size = dimension2D;
    }
    
    public double getScale() {
        return scale;
    }

    public Point2D getLocation() {
        return location;
    }
    
    public void setLocation(Point2D point) {
        this.location = point;
    }

    
    // must rewrite this to use a global mouse since the desklet won't
    // get the mouse events anymore
    public void setBackgroundDraggable(boolean backgroundDraggable) {
        if(backgroundDraggable == this.draggable) {
            return;
        }
        if(backgroundDraggable) {
            comp.addMouseListener(mml);
            comp.addMouseMotionListener(mml);
        } else {
            comp.removeMouseListener(mml);
            comp.removeMouseMotionListener(mml);
        }
    }
    
    public void setContent(JComponent content) {
        if(this.content != null) {
            comp.remove(this.content);
        }
        this.content = content;
        comp.add(content,"Center");
        pack();
    }
    
    public void setResizable(boolean b) {
    }
    
    public void setShaped(boolean b) {
    }
    
    public void setVisible(boolean b) {
        this.visible = b;
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

    public void setScale(double scale) {
        this.scale = scale;
    }

    void setDirty(boolean b) {
        this.dirty = b;
    }

    boolean isDirty() {
        return this.dirty;
    }

    // do nothing for now
    public void setShape(Shape shape) {
    }

    public void pack() {
        // how do we do packing?
        comp.setSize(comp.getLayout().preferredLayoutSize(comp));
        setSize(comp.getSize());
        setDirty(true);
    }

    public boolean isVisible() {
        return visible;
    }



}