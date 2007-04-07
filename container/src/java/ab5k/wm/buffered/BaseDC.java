/*
 * BaseDC.java
 *
 * Created on March 27, 2007, 3:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 *
 * @author joshy
 */
public abstract class BaseDC extends DeskletContainer {
    
    /** Creates a new instance of BaseDC */
    public BaseDC(BufferedWM wm, DefaultContext context) {
        this.wm = wm;
        this.context = context;
    }
    
    private DefaultContext context;
    protected BufferedWM wm;
    protected JComponent content;
    
    

    
    public abstract Point2D getLocation();
    
    public abstract void setLocation(Point2D point);
    
    public JComponent getContent() {
        return this.content;
    }

    public DefaultContext getContext() {
        return context;
    }
    public void setResizable(boolean b) {
    }
    public boolean isResizable() {
        return false;
    }
    
    public Shape getShape() {
        return null;
    }
    
    // do nothing for now
    public void setShape(Shape shape) {
    }
}
