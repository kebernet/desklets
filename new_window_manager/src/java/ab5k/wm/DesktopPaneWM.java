/*
 * DesktopPaneWM.java
 *
 * Created on March 20, 2007, 12:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm;

import ab5k.CustomDesktopPane;
import ab5k.DesktopBackground;
import ab5k.MainPanel;
import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import ab5k.security.InternalFrameContainer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author joshy
 */
public class DesktopPaneWM extends WindowManager {
    
    private HashMap<JPanel, JInternalFrame> iframes = new HashMap<JPanel, JInternalFrame>();
    private JDesktopPane desktop;
    private JFrame frame;

    private JComponent dock;
    
    /** Creates a new instance of DesktopPaneWM */
    public DesktopPaneWM() {
        this.desktop = new CustomDesktopPane();
        frame = new JFrame("AB5k");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(desktop,"Center");
    }
    
    
    /* ===== the desklet container lifecycle ======= */
    
    public DeskletContainer createInternalContainer(DefaultContext context) {
        InternalFrameContainer ifc = new InternalFrameContainer(context.getConfig()
                .getName(),this);
        iframes.put(ifc.panel, ifc.iframe);
        return ifc;
    }
    
    public void animateCreation(DeskletContainer dc) {
        final InternalFrameContainer ifc = (InternalFrameContainer) dc;
        desktop.add(ifc.iframe);
    }
    
    public void animateDestruction(DeskletContainer dc) {
        Dimension dim = getContainerSize();
        final InternalFrameContainer ifc = (InternalFrameContainer) dc;
        Animator an = PropertySetter.createAnimator(500, ifc.iframe,
                "location", ifc.iframe.getLocation(),
                new Point(-dim.width, dim.height));
        
        an.addTarget(new TimingTarget() {
            public void begin() {          }
            public void end() {
                //wm.removeDesklet(ifc.iframe);
                desktop.remove(ifc.iframe);
                iframes.remove(ifc.panel);
            }
            public void repeat() {    }
            public void timingEvent(float fraction) {        }
        });
        
        an.start();
    }
    
    public void destroyContainer(DeskletContainer dc) {
        
    }
    
    public void setLocation(DeskletContainer ifc, Point point) {
        ((InternalFrameContainer)ifc).iframe.setLocation(point);
    }
    
    public Point getLocation(DeskletContainer deskletContainer) {
        final InternalFrameContainer ifc = (InternalFrameContainer) deskletContainer;
        Point location = ifc.iframe.getLocation();
        return location;
    }

    public void setDesktopBackground(DesktopBackground bg) {
        ((CustomDesktopPane)desktop).setDesktopBackground(bg);
    }

    public Dimension getContainerSize() {
        return desktop.getSize();
    }

    public Object getTopLevel() {
        return frame;
    }

    public void setDockComponent(JComponent dock) {
        this.dock = dock;
        frame.getContentPane().add(dock,"West");
    }

    public void setDockingSide(MainPanel.DockingSide side) {
        if(dock == null) return;
        frame.remove(dock);
        if(side == MainPanel.DockingSide.Left) {
            frame.getContentPane().add(dock,"East");
        }
        if(side == MainPanel.DockingSide.Right) {
            frame.getContentPane().add(dock,"West");
        }
        frame.getContentPane().validate();
        dock.validate();
    }
    
}
