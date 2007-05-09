package org.glossitope.container.wm;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.glossitope.container.MainPanel;
import org.glossitope.desklet.DeskletContainer;
import org.glossitope.container.security.DefaultContext;

/** 
 * The currently registered WindowManager is in charge of managing the desklets
 * on screen components, creating the top level window for all of org.glossitope, handling
 * special effects and transitions. [more??]
 */
public abstract class WindowManager<T> {

    public void init() {
        
    }
    /**
     * Gets the real toplevel window. In some implementations this will be a 
     * JFrame or java.awt.Window. In other implementations it may be something
     * completely different or null if access to the top level is not allowed. Always
     * check the return value before you cast it.
     * @return 
     */
    abstract public Object getTopLevel();
    
    abstract public DeskletContainer createInternalContainer(DefaultContext context);
    
    abstract public DeskletContainer createExternalContainer(DefaultContext context);
    
    abstract public void showContainer(DeskletContainer dc);
    abstract public void destroyContainer(DeskletContainer dc);
    
    
    abstract public DeskletContainer createDialog(DeskletContainer deskletContainer);
    
    
    public void convertInternalToExternalContainer(DeskletContainer dc) {
    }
    
    public void convertExternalToInternalContainer(DeskletContainer dc) {
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
    
    
    /*
    public abstract void setDesktopBackground(DesktopBackground bg);
     */

    public abstract Dimension getContainerSize();

    public abstract void setDockComponent(JComponent dock);
    
    public abstract void setDockingSide(MainPanel.DockingSide side);

    
}