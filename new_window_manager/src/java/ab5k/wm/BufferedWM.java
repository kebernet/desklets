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
import ab5k.util.MoveMouseListener;
import com.sun.xml.rngom.parse.compact.CompactParseable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import org.joshy.util.u;

/**
 * An implementation of WindowManager that uses buffered images instead of
 * internal frames a desktop pane to show the desklets on screen.
 * @author joshy
 */
public class BufferedWM extends WindowManager {
    List<BufferedDeskletContainer> desklets;
    Map<BufferedDeskletContainer,Point> locations;
    //Map<DefaultContext, BufferedImage> imageMap;
    JFrame frame;
    JPanel panel;
    
    private JComponent dock;
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM() {
        //imageMap = new HashMap<DefaultContext,BufferedImage>();
        desklets = new ArrayList<BufferedDeskletContainer>();
        locations = new HashMap<BufferedDeskletContainer,Point>();
        
        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(Color.BLUE);
                g.fillRect(0,0,getWidth(),getHeight());
                Graphics2D g2 = (Graphics2D)g;
                g.setColor(Color.RED);
                for(BufferedDeskletContainer dc : desklets) {
                    Dimension2D size = dc.getSize();
                    Graphics gx = g2.create();
                    
                    Point pt = locations.get(dc);
                    gx.translate(pt.x,pt.y);
                    u.p("painting: " + dc.comp);
                    this.add(dc.comp);
                    dc.comp.paint(gx);
                    gx.dispose();
                    
                    this.remove(dc.comp);
                }
            }
        };
        //panel.setLayout(new AbsLayout());
        frame = new JFrame("AB5k");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,"Center");
        panel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                redispatch(e);
            }
            public void mouseEntered(MouseEvent e) {
                redispatch(e);
            }
            public void mouseExited(MouseEvent e) {
                redispatch(e);
            }
            public void mousePressed(MouseEvent e) {
                u.p("pressed");
                redispatch(e);
            }
            public void mouseReleased(MouseEvent e) {
                redispatch(e);
            }
        });
        panel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                redispatch(e);
            }
            public void mouseMoved(MouseEvent e) {
                redispatch(e);
            }
        });
    }
    private void redispatch(MouseEvent e) {
        if(!desklets.isEmpty()) {
            BufferedDeskletContainer bdc = desklets.get(0);
            Point pt = locations.get(bdc);
            e.translatePoint(-pt.x,-pt.y);
            JComponent comp = bdc.comp;
            comp.dispatchEvent(e);
        }
    }
    
    
    /* ===== methods dealing with the overall conatiner === */
    public Object getTopLevel() {
        return frame;
    }
    
    public Dimension getContainerSize() {
        return panel.getSize();
    }
    
    public void setDesktopBackground(DesktopBackground bg) {
    }
    
    public void setDockComponent(JComponent dock) {
        this.dock = dock;
        frame.getContentPane().add(dock,"West");
    }
    
    public void setDockingSide(MainPanel.DockingSide side) {
    }
    
    
    
    /* ====== the desklet container lifecycle ===== */
    public DeskletContainer createInternalContainer(DefaultContext context) {
        BufferedDeskletContainer cont = new BufferedDeskletContainer();
        locations.put(cont,new Point(0,0));
        return cont;
    }
    
    public void animateCreation(DeskletContainer dc) {
        //panel.add(((BufferedDeskletContainer)dc).comp);
        desklets.add((BufferedWM.BufferedDeskletContainer) dc);
    }
    
    public void animateDestruction(DeskletContainer dc) {
    }
    
    public void destroyContainer(DeskletContainer dc) {
        //panel.remove(((BufferedDeskletContainer)dc).comp);
        desklets.remove(dc);
        locations.remove(dc);
    }
    
    /* === methods to manipulate desklet containers ===== */
    public void setLocation(DeskletContainer container, Point point) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) container;
        //bdc.comp.setLocation(point);
        locations.put(bdc,point);
        u.p("set location to: " + point);
        panel.repaint();
    }
    
    public Point getLocation(DeskletContainer deskletContainer) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) deskletContainer;
        return locations.get(bdc);
        //return bdc.comp.getLocation();
    }
    
    private class BufferedDeskletContainer implements DeskletContainer {
        JComponent comp;
        
        private boolean draggable = false;
        MoveMouseListener mml;
        
        private JComponent content;
        
        BufferedDeskletContainer() {
            new JTable();
            new DefaultTableCellRenderer();
            comp = new JPanel() {
                protected void paintComponent(Graphics g) {
                    g.setColor(Color.BLUE);
                    g.fillRect(0,0,getWidth(),getHeight());
                    super.paintComponent(g);
                }
            };
            comp.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                }
                public void mouseEntered(MouseEvent e) {
                }
                public void mouseExited(MouseEvent e) {
                }
                public void mousePressed(MouseEvent e) {
                    u.p("comp mouse pressed");
                }
                public void mouseReleased(MouseEvent e) {
                }
            });
            comp.setBorder(BorderFactory.createLineBorder(Color.RED));
            comp.setLayout(new BorderLayout());
            mml = new MoveMouseListener(this,BufferedWM.this);
            setBackgroundDraggable(false);
        }
        
        public Dimension2D getSize() {
            return comp.getSize();
        }
        
        
        // must rewrite this to use a global mouse since the desklet won't
        // get the mosue events anymore
        public void setBackgroundDraggable(boolean backgroundDraggable) {
            if(backgroundDraggable == this.draggable) {
                return;
            }
            u.p("set to draggable: " + backgroundDraggable);
            if(backgroundDraggable) {
                comp.addMouseListener(mml);
                comp.addMouseMotionListener(mml);
            } else {
                comp.removeMouseListener(mml);
                comp.removeMouseMotionListener(mml);
            }
        }
        
        public void setContent(JComponent content) {
            if(this.content != null) {
                comp.remove(this.content);
            }
            this.content = content;
            comp.add(content,"Center");
            comp.setSize(comp.getLayout().preferredLayoutSize(comp));
            //comp.getLayout().layoutContainer(comp);
            //comp.validate();
        }
        
        public void setResizable(boolean b) {
        }
        
        public void setShaped(boolean b) {
        }
        
        public void setSize(Dimension2D dimension2D) {
            comp.setSize((Dimension) dimension2D);
        }
        
        public void setVisible(boolean b) {
        }
    }
    
    
    // this just lays out components using their preferred size. it does not
    // move them around at all.
    // it also leaves the parent at whatever size it was set at
    private class AbsLayout implements LayoutManager {
        
        public void addLayoutComponent(String name, Component comp) {
        }
        
        public void layoutContainer(Container parent) {
            int ncomponents = parent.countComponents();
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                int x = comp.getX();
                int y = comp.getY();
                Dimension size = comp.getPreferredSize();
                comp.reshape(x,y,size.width,size.height);
            }
        }
        
        public Dimension minimumLayoutSize(Container parent) {
            return parent.size();
        }
        
        public Dimension preferredLayoutSize(Container parent) {
            return parent.size();
        }
        
        public void removeLayoutComponent(Component comp) {
        }
    }
    
}
