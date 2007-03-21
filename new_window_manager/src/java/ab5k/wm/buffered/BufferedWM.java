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
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.joshy.util.u;

/**
 * An implementation of WindowManager that uses buffered images instead of
 * internal frames a desktop pane to show the desklets on screen.
 * @author joshy
 */
public class BufferedWM extends WindowManager {
    List<BufferedDeskletContainer> desklets;
    JFrame frame;
    JPanel panel;
    
    private JComponent dock;
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM() {
        desklets = new ArrayList<BufferedDeskletContainer>();
        
        panel = new DeskletRenderPanel();
        frame = new JFrame("AB5k");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,"Center");
        MouseRedispatcher mouse = new MouseRedispatcher(this);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
    }
    
    private BufferedDeskletContainer findContainer(Point pt) {
        for(int i=desklets.size()-1; i>=0; i--) {
            BufferedDeskletContainer bdc = desklets.get(i);
            Point loc = bdc.getLocation();
            Dimension2D size = bdc.getSize();
            Rectangle rect = new Rectangle(loc.x, loc.y, (int)size.getWidth(), (int)size.getHeight());
            if(rect.contains(pt)) {
                return bdc;
            }
        }
        return null;
    }
    
    void raiseWindow(MouseEvent e) {
        BufferedDeskletContainer bdc = findContainer(e.getPoint());
        if(bdc != null) {
            desklets.remove(bdc);
            desklets.add(bdc);
            panel.repaint();
        }
    }
    
    void redispatch(MouseEvent e) {
        if(!desklets.isEmpty()) {
            BufferedDeskletContainer bdc = findContainer(e.getPoint());
            if(bdc != null) {
                Point pt = bdc.getLocation();
                e.translatePoint(-pt.x,-pt.y);
                JComponent comp = bdc.comp;
                comp.dispatchEvent(e);
            }
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
        BufferedDeskletContainer cont = new BufferedDeskletContainer(this);
        cont.setLocation(new Point(0,0));
        return cont;
    }
    
    public void animateCreation(DeskletContainer dc) {
        //panel.add(((BufferedDeskletContainer)dc).comp);
        desklets.add((BufferedDeskletContainer) dc);
        ((BufferedDeskletContainer)dc).setLocation(new Point(100,100));
        Animator anim = new Animator(750);
        anim.addTarget(new PropertySetter(dc,"location",new Point(100,100), new Point(100,100)));
        anim.addTarget(new PropertySetter(dc,"alpha",0f,1f));
        anim.addTarget(new PropertySetter(dc,"rotation",Math.PI,Math.PI*2.0));
        anim.addTarget(new PropertySetter(dc,"scale",0.1,1.3,1.0));
        anim.addTarget(new TimingTarget() {
            public void begin() {  }
            public void end() {  }
            public void repeat() { }
            public void timingEvent(float f) {
                panel.repaint();
            }
        });
        anim.start();
    }
    
    public void animateDestruction(final DeskletContainer dc) {
        Animator anim = new Animator(750);
        //anim.addTarget(new PropertySetter(dc,"location",new Point(0,0), new Point(200,200)));
        anim.addTarget(new PropertySetter(dc,"alpha",1f,0f));
        //anim.addTarget(new PropertySetter(dc,"rotation",0f,(float)Math.PI*2f*5f));
        anim.addTarget(new PropertySetter(dc,"scale",1.0,0.3));
        anim.addTarget(new TimingTarget() {
            public void begin() {            }
            public void end() {            }
            public void repeat() {            }
            public void timingEvent(float f) {                panel.repaint();            }
        });
        anim.addTarget(new TimingTarget() {
            public void begin() {
            }
            public void end() {
                destroyContainer(dc);
            }
            public void repeat() {
            }
            public void timingEvent(float f) {
            }
        });
        anim.start();
    }
    
    public void destroyContainer(DeskletContainer dc) {
        //panel.remove(((BufferedDeskletContainer)dc).comp);
        desklets.remove(dc);
        panel.repaint();
    }
    
    /* === methods to manipulate desklet containers ===== */
    public void setLocation(DeskletContainer container, Point point) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) container;
        bdc.setLocation(point);
        panel.repaint();
    }
    
    public Point getLocation(DeskletContainer deskletContainer) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) deskletContainer;
        return bdc.getLocation();
    }
    
    
    // this just lays out components using their preferred size. it does not
    // move them around at all.
    // it also leaves the parent at whatever size it was set at
    
    private class DeskletRenderPanel extends JPanel {
        
        private CellRendererPane rendererPane;
        
        public DeskletRenderPanel() {
            super();
            rendererPane = new CellRendererPane();
            add(rendererPane);
        }
        
        protected void paintComponent(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, getWidth(), getHeight());
            Graphics2D g2 = (Graphics2D) g;
            g.setColor(Color.RED);
            for (BufferedDeskletContainer bdc : desklets) {
                Dimension size = new Dimension((int) bdc.getSize().getWidth(), (int) bdc.getSize().getHeight());
                Point pt = bdc.getLocation();
                if(bdc.getBuffer() == null) {
                    BufferedImage img = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics gx = img.getGraphics();
                    rendererPane.paintComponent(gx, bdc.comp, this,
                            0,0,  size.width, size.height,
                            true);
                    gx.setColor(Color.RED);
                    gx.drawLine(0,0,size.width,size.height);
                    gx.dispose();
                    bdc.setBuffer(img);
                    rendererPane.removeAll();
                }
                
                Graphics2D g3 = (Graphics2D) g2.create();
                g3.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,bdc.getAlpha()));
                g3.translate(pt.x,pt.y);
                g3.translate(size.width/2,size.height/2);
                g3.rotate(bdc.getRotation(),0,0);
                g3.scale(bdc.getScale(),bdc.getScale());
                g3.translate(-size.width/2,-size.height/2);
                g3.drawImage(bdc.getBuffer(), 0, 0, null);
                g3.drawRect(0, 0, size.width, size.height);
                g3.dispose();
            }
        }
    }



}
