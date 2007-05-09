/*
 * JFrameDeskletContainer.java
 *
 * Created on March 24, 2007, 1:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.wm.buffered;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import org.joshy.util.u;

import org.glossitope.container.util.GraphicsUtil;

/**
 *
 * @author joshy
 */
public class JFrameDeskletContainer extends DCPeer {
    JFrame frame;
    RootPaneContainer root;
    Window window;
    JComponent content;
    
    private boolean packed = false;

    private boolean shaped = false;
    
    /** Creates a new instance of JFrameDeskletContainer */
    public JFrameDeskletContainer(final BufferedDeskletContainer bdc) {
        super(bdc);
        this.frame = new JFrame();
        this.root = this.frame;
        this.window = this.frame;
        this.frame.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
                u.p("frame moved! " + e);
                if(frame.getLocation().getX() >
                        bdc.wm.core.getCollapseWindowAction().getClosedBounds().getX()) {
                    u.p("once more into the dock!");
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            //bdc.wm.convertExternalToInternalContainer(JFrameDeskletContainer.this);
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
    
    public void setContent(JComponent jComponent) {
        this.content = jComponent;
        root.getContentPane().add(jComponent);
    }
    
    public void setBackgroundDraggable(boolean b) {
    }
    public boolean isBackgroundDraggable() {
        return false;
    }
    
    
    public void setShaped(boolean shaped) {
        if(this.shaped != shaped) {
            if(shaped) {
                frame.setUndecorated(true);
                frame.setBackground(new Color(0,0,0,0));
            } else {
                frame.setBackground(new Color(255,255,255,255));
            }
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
