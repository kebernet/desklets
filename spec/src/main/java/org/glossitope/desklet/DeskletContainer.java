/*
 * DeskletContainer.java
 *
 * Created on February 22, 2007, 5:22 PM

 */

package org.glossitope.desklet;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 *
 * @author cooper
 * @author joshua@marinacci.org
 */
public abstract class DeskletContainer {
    
    public abstract void setContent( JComponent component );
    public abstract JComponent getContent();
    
    public abstract void setBackgroundDraggable( boolean backgroundDraggable );
    public abstract boolean isBackgroundDraggable();
    
    public abstract void setShaped( boolean shaped );
    public abstract boolean isShaped();
    
    public abstract void setShape( Shape shape );
    public abstract Shape getShape();
    
    public abstract void setResizable( boolean resizable );
    public abstract boolean isResizable();
    
    public abstract void setVisible(boolean visible );
    public abstract boolean isVisible();
    
    public abstract void setSize(Dimension2D size);
    public abstract Dimension2D getSize();
    
    public abstract void setLocation(Point2D location);
    public abstract Point2D getLocation();
    
    public abstract void pack();
}
