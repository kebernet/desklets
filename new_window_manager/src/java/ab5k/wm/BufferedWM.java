/*
 * BufferedWM.java
 *
 * Created on March 20, 2007, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm;

import ab5k.DesktopBackground;
import ab5k.MainPanel;
import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

/**
 * An implementation of WindowManager that uses buffered images instead of
 * internal frames a desktop pane to show the desklets on screen.
 * @author joshy
 */
public class BufferedWM extends WindowManager {
    Map<DefaultContext, BufferedImage> imageMap;
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM() {
        imageMap = new HashMap<DefaultContext,BufferedImage>();
    }

    public DeskletContainer createInternalContainer(DefaultContext context) {
        return null;
    }

    public void animateCreation(DeskletContainer dc) {
    }

    public void animateDestruction(DeskletContainer dc) {
    }

    public void destroyContainer(DeskletContainer dc) {
    }

    public void setDesktopBackground(DesktopBackground bg) {
    }

    public Dimension getContainerSize() {
        return null;
    }

    public void setLocation(DeskletContainer ifc, Point point) {
    }

    public Point getLocation(DeskletContainer deskletContainer) {
        return null;
    }

    public Object getTopLevel() {
        return null;
    }

    public void setDockComponent(JComponent dock) {
    }

    public void setDockingSide(MainPanel.DockingSide side) {
    }
    
}
