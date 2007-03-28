package ab5k.wm;

import ab5k.DesktopBackground;
import ab5k.MainPanel;
import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/** 
 * The currently registered WindowManager is in charge of managing the desklets
 * on screen components, creating the top level window for all of ab5k, handling
 * special effects and transitions. [more??]
 */
public abstract class WindowManager {
    
    abstract public Object getTopLevel();
    
    abstract public DeskletContainer createInternalContainer(DefaultContext context);
    /*
    abstract public DeskletContainer createExternalContainer(DefaultContext context);
    */
    
    abstract public void animateCreation(DeskletContainer dc);
    
    // do we need both this and destroyContainer?
    abstract public void animateDestruction(DeskletContainer dc);
    
    abstract public void destroyContainer(DeskletContainer dc);
    
    abstract public DeskletContainer createDialog(DeskletContainer deskletContainer);
    
    
    public DeskletContainer convertInternalToExternalContainer(DeskletContainer dc) {
        return null;
    }
    
    public DeskletContainer convertExternalToInternalContainer(DeskletContainer dc) {
        return null;
    }
    
    /*
    public boolean isBufferingSupported() {
        return false;
    }
    
    
    public BufferedImage getBufferedImage(DeskletContainer dc) {
        return null;
    }
    */
    
    /*
    public void repaint(DeskletContainer dc) {
        
    }
    
    public void repaint(DeskletContainer dc, Graphics2D g) {
        
    }*/
    
    
    
    /*
    // general setup
    public void init() {
        
    }
    
    // install the custom repaint manager or anything else screen / graphics configuration
    // related
    public void configureScreen() {
        
    }*/
    
    
    
    public abstract void setDesktopBackground(DesktopBackground bg);

    public abstract Dimension getContainerSize();

    public abstract void setLocation(DeskletContainer ifc, Point2D point);

    public abstract Point2D getLocation(DeskletContainer deskletContainer);

    public abstract void setDockComponent(JComponent dock);
    
    public abstract void setDockingSide(MainPanel.DockingSide side);

    
}