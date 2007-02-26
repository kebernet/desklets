/*
 * DockContainer.java
 *
 * Created on February 22, 2007, 7:33 PM
 */

package ab5k.security;

import ab5k.desklet.DeskletContainer;
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
    boolean shaped = true;
    JPanel panel = new JPanel(){
        protected void paintComponent(Graphics g) {
            if( !shaped ){
                super.paintComponent( g );
            }
        }
    };
    JComponent content;
    /** Creates a new instance of DockContainer */
    public DockContainer() {
        super();
    }

    public void setContent(JComponent component) {
        if( content != null ){
            panel.remove( content );
        }
        content = component;
        panel.add( content );
    }

    public void setSize(Dimension2D size) {
        panel.setSize( new Dimension( (int)size.getWidth(), (int)size.getHeight() ) );
    }

    public void setVisible(boolean visible) {
        panel.setVisible(visible);
    }

    public void setShaped(boolean shaped) {
        this.shaped = shaped;
    }

    public void setResizable(boolean resizable) {
        //ignored?
    }

    public void setBackgroundDraggable(boolean backgroundDraggable) {
    }

    public int hashCode() {
        int retValue;
        
        retValue = super.hashCode();
        return retValue;
    }

    public Dimension2D getSize() {
        return panel.getSize();
    }
    
}
