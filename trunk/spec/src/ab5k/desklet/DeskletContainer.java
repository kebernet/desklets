/*
 * DeskletContainer.java
 *
 * Created on February 22, 2007, 5:22 PM

 */

package ab5k.desklet;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import javax.swing.JComponent;

/**
 *
 * @author cooper
 */
public interface DeskletContainer {
    
    public void setContent( JComponent component );
    public void setBackgroundDraggable( boolean backgroundDraggable );
    public void setShaped( boolean shaped );
    public void setResizable( boolean resizable );
    public void setVisible(boolean visible );
    public Dimension2D getSize();
    public void setSize(Dimension2D size);
}
