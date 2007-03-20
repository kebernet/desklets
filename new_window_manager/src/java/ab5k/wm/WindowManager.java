package ab5k.wm;

import ab5k.DesktopBackground;
import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JInternalFrame;

public abstract class WindowManager {
    
    abstract public DeskletContainer createInternalContainer(DefaultContext context);
    /*
    abstract public DeskletContainer createExternalContainer();
     */
    
    abstract public void animateCreation(DeskletContainer dc);
    
    // do we need both this and destroyContainer?
    abstract public void animateDestruction(DeskletContainer dc);
    
    abstract public void destroyContainer(DeskletContainer dc);
    /*
    public DeskletContainer convertInternalToExternalContainer(DeskletContainer dc) {
        return null;
    }
    
    public DeskletContainer convertExternalToInternalContainer(DeskletContainer dc) {
        return null;
    }
    
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

    public abstract void setLocation(DeskletContainer ifc, Point point);

    public abstract Point getLocation(DeskletContainer deskletContainer);
    
}