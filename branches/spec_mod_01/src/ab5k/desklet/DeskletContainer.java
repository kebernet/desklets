/*
 * DeskletContainer.java
 *
 * Created on February 22, 2007, 5:22 PM

 */

package ab5k.desklet;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 *
 * @author cooper
 */
public abstract class DeskletContainer {
    
    public abstract void setContent( JComponent component );
    public abstract void setBackgroundDraggable( boolean backgroundDraggable );
    public abstract void setShaped( boolean shaped );
    public abstract void setShape( Shape shape );
    public abstract void setResizable( boolean resizable );
    
    public abstract void setVisible(boolean visible );
    public abstract boolean isVisible();
    
    public abstract void setSize(Dimension2D size);
    public abstract Dimension2D getSize();
    
    public abstract void setLocation(Point2D location);
    public abstract Point2D getLocation();
    
    public abstract void pack();
}
