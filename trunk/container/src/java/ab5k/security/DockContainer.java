/*
 * DockContainer.java
 *
 * Created on February 22, 2007, 7:33 PM
 */
package ab5k.security;

import ab5k.desklet.DeskletContainer;

import org.jdesktop.swingx.JXPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;

import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 *
 * @author cooper
 */
public class DockContainer implements DeskletContainer {
    JComponent content;
    JXPanel panel = new JXPanel(); /*{
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
}
