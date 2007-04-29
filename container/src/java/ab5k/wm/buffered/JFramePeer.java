/*
 * JFrameDeskletContainer.java
 *
 * Created on March 24, 2007, 1:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import ab5k.util.GraphicsUtil;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class JFramePeer extends DCPeer {
    JFrame frame;
    JDialog dialog;
    RootPaneContainer root;
    Window window;
    
    private boolean packed = false;
    
    private boolean shaped = false;
    private boolean isDialog = false;
    private JComponent content;
    
    /** Creates a new instance of JFrameDeskletContainer */
    public JFramePeer(final BufferedDeskletContainer bdc) {
        this(bdc,false);
    }
    public JFramePeer(final BufferedDeskletContainer bdc, boolean isDialog) {
        super(bdc);
        this.isDialog = isDialog;
        if(isDialog) {
            JFrame parent = ((JFramePeer)bdc.getProxy().contentContainer.getPeer()).frame;
            this.dialog = new JDialog(parent);
            this.root = this.dialog;
            this.window = this.dialog;
        } else {
            this.frame = new JFrame();
            this.root = this.frame;
            this.window = frame;
            this.frame.addComponentListener(new ComponentListener() {
                public void componentHidden(ComponentEvent e) {
                }
                public void componentMoved(ComponentEvent e) {
                    //u.p("frame moved! " + e);
                    if(frame.getLocation().getX() >
                            bdc.wm.core.getCollapseWindowAction().getClosedBounds().getX()) {
                        u.p("once more into the dock!");
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                //bdc.wm.convertExternalToInternalContainer(JFramePeer.this);
                            }
                        });
                    }
                }
                public void componentResized(ComponentEvent e) {
                }
                public void componentShown(ComponentEvent e) {
                }
            });
        }
    }
    
    public void setContent(JComponent content) {
        if(this.content != null) {
            root.getContentPane().remove(content);
        }
        this.content = content;
        if(this.content != null) {
            root.getContentPane().add(content);
        }
    }
    
    public void setBackgroundDraggable(boolean b) {
    }
    public boolean isBackgroundDraggable() {
        return false;
    }
    
    
    public void setShaped(boolean shaped) {
        if(this.shaped != shaped) {
            //if(isDialog) {
                if(shaped) {
                    frame.setUndecorated(true);
                    frame.setBackground(new Color(0,0,0,0));
                } else {
                    frame.setBackground(new Color(255,255,255,255));
                }
                this.shaped = shaped;
            //}
        }
    }
    public boolean isShaped() {
        return this.shaped;
    }
    
    public void setResizable(boolean b) {
    }
    
    public void setVisible(boolean visible) {
        if(!packed) {
            window.pack();
            packed=true;
        }
        window.setVisible(visible);
    }
    
    public Dimension2D getSize() {
        return window.getSize();
    }
    
    public void setSize(Dimension2D d) {
        window.setSize(new Dimension((int)d.getWidth(), (int)d.getHeight()));
    }
    
    
    public Point2D getLocation() {
        return window.getLocation();
    }
    
    public void setLocation(Point2D point) {
        window.setLocation(GraphicsUtil.toPoint(point));
    }
    
    // do nothing for now
    public void setShape(Shape shape) {
    }
    
    public void pack() {
        window.pack();
    }
    
    public boolean isVisible() {
        return window.isVisible();
    }
    
}
