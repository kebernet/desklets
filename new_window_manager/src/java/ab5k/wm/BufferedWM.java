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
import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author joshy
 */
public class BufferedWM extends WindowManager {
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM() {
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
    
}
