/*
 * DPeer.java
 *
 * Created on April 9, 2007, 4:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 *
 * @author joshy
 */
public abstract class DCPeer {
    protected BufferedDeskletContainer bdc;
    protected boolean shaped = false;
    
    /** Creates a new instance of DPeer */
    public DCPeer(BufferedDeskletContainer bdc) {
        this.bdc = bdc;
    }
    
    public abstract Point2D getLocation();
    
    public abstract void setLocation(Point2D point);
    
    public abstract Dimension2D getSize();
    
    public abstract void setSize(Dimension2D size);
    
    
    // do nothing for now
    public void setResizable(boolean b) {
    }
    public boolean isResizable() {
        return false;
    }
    public Shape getShape() {
        return null;
    }
    public void setShape(Shape shape) {
    }

    public abstract void setVisible(boolean visible);
    
    public abstract boolean isVisible();

    public boolean isShaped() {
        return shaped;
    }
    
    public void setShaped(boolean shaped) {
        this.shaped = shaped;
    }
    
    public void contentChanged() {
        
    }
}
