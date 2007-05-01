/*
 * DockContainer.java
 *
 * Created on February 22, 2007, 7:33 PM
 */
package ab5k.security;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXBoxPanel;

import ab5k.desklet.DeskletContainer;


/**
 *
 * @author cooper
 */
public class DockContainer extends DeskletContainer {
    JComponent content;
    JXBoxPanel panel = new JXBoxPanel();
    //JXPanel panel = new JXPanel();
    /*{
    protected void paintComponent(Graphics g) {
    if( !shaped ){
    super.paintComponent( g );
    }
    }
    };*/
    boolean shaped = false;

    /** Creates a new instance of DockContainer */
    public DockContainer() {
        super();
        setShaped(true);
    }

    public Dimension2D getSize() {
        return panel.getSize();
    }

    public int hashCode() {
        int retValue;

        retValue = super.hashCode();

        return retValue;
    }

    public void setBackgroundDraggable(boolean backgroundDraggable) {
    }

    public void setContent(JComponent component) {
        if(content != null) {
            panel.remove(content);
        }

        content = component;
        panel.add(content);
    }

    //override to do nothing. you can't resize them
    public void setResizable(boolean resizable) {
        //ignored?
    }

    public void setShaped(boolean shaped) {
        this.shaped = shaped;
        panel.setOpaque(!shaped);
    }

    public void setSize(Dimension2D size) {
        panel.setSize(new Dimension((int) size.getWidth(),
                (int) size.getHeight()));
    }

    public void setVisible(boolean visible) {
        panel.setVisible(visible);
    }

    //override to do nothing. they are already shaped
    public void setShape(Shape shape) {
    }

    // override to do nothing. you can't move widgets around in the dock.
    public void setLocation(Point2D location) {
    }

    public void pack() {
        // isn't this implicit? do we need this here?
    }

    public boolean isVisible() {
        return panel.isVisible();
    }

    public Point2D getLocation() {
        return new Point(0,0);
    }

    public JComponent getContent() {
        return content;
    }

    public boolean isBackgroundDraggable() {
        return false;
    }

    public boolean isShaped() {
        return !panel.isOpaque();
    }

    public Shape getShape() {
        return null;
    }

    public boolean isResizable() {
        return false;
    }
}
