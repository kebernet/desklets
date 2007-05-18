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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import org.joshy.util.u;

import org.glossitope.container.util.GraphicsUtil;
import org.glossitope.container.util.MoveMouseListener;
import org.glossitope.container.util.PlafUtil;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.WindowUtils;

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
    private JXPanel top;
    
    /** Creates a new instance of JFrameDeskletContainer */
    public JFramePeer(final BufferedDeskletContainer bdc) {
        this(bdc,false);
    }
    public JFramePeer(final BufferedDeskletContainer bdc, boolean isDialog) {
        super(bdc);
        this.top = new JXPanel();
        this.top.setBackgroundPainter(new Painter() {
            
            public void paint(Graphics2D arg0, Object arg1, int arg2, int arg3) {
            }
        });
        this.top.setOpaque(false);
        
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
                        //u.p("once more into the dock!");
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
        if(PlafUtil.isMacOSX()) {
            JPanel contentPane = new JPanel();
            contentPane.setBackground(Color.BLUE);
            contentPane.setOpaque(false);
            this.root.setContentPane(contentPane);
        }
        this.root.getContentPane().add(top);
    }
    
    public void setContent(JComponent content) {
        if(this.content != null) {
            //root.getContentPane().remove(content);
        }
        this.content = content;
        if(this.content != null) {
            this.top.add(content);
            //root.getContentPane().add(content);
        }
    }
    
    private boolean backgroundDraggable = false;
    public void setBackgroundDraggable(boolean b) {
        this.backgroundDraggable = b;
        
        JFrameMoveMouseListener ml = new JFrameMoveMouseListener();
        this.top.addMouseListener(ml);
        this.top.addMouseMotionListener(ml);
    }
    
    private class JFrameMoveMouseListener implements MouseListener, MouseMotionListener {
        Point start_drag;
        Point start_loc;
        
        public void mouseClicked(MouseEvent arg0) {
        }
        
        public void mousePressed(MouseEvent arg0) {
            u.p("mouse pressed: " + arg0);
            start_drag = arg0.getPoint();
            u.p("start drag = " + start_drag);
            SwingUtilities.convertPointToScreen(start_drag, frame);
            u.p("start drag = " + start_drag);
            //this.start_loc = this.getFrame(this.target).getLocation();
            start_loc = GraphicsUtil.toPoint(frame.getLocation());
            u.p("start loc = " + start_loc);
        }
        
        public void mouseReleased(MouseEvent arg0) {
        }
        
        public void mouseEntered(MouseEvent arg0) {
        }
        
        public void mouseExited(MouseEvent arg0) {
        }
        
        public void mouseDragged(MouseEvent e) {
            u.p("mouse dragged: " + e);
            Point current = e.getPoint();
            SwingUtilities.convertPointToScreen(current, frame);
            Point offset = new Point(
                    (int)current.getX()-(int)start_drag.getX(),
                    (int)current.getY()-(int)start_drag.getY());
            Point new_location = new Point(
                    (int)(this.start_loc.getX()+offset.getX()),
                    (int)(this.start_loc.getY()+offset.getY()));
            frame.setLocation(new_location);
        }
        
        public void mouseMoved(MouseEvent arg0) {
        }
        
    }
    
    public boolean isBackgroundDraggable() {
        return this.backgroundDraggable;
    }
    
    
    public void setShaped(boolean shaped) {
        if(this.shaped != shaped) {
            //if(isDialog) {
            if(shaped) {
                if(PlafUtil.isMacOSX()) {
                    frame.setUndecorated(true);
                    frame.setBackground(new Color(0,0,0,0));
                    //com.sun.jna.examples.WindowUtils.setWindowTransparent(frame, true);
                } else {
                    com.sun.jna.examples.WindowUtils.setWindowTransparent(frame, true);
                }
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
