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
import ab5k.security.DeskletConfig;
import ab5k.security.DeskletManager;
import ab5k.security.DeskletRunner;
import ab5k.security.LifeCycleException;
import ab5k.security.Registry;
import ab5k.wm.DeskletRenderPanel;
import com.totsp.util.BeanArrayList;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.CellRendererPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
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
    
    static final boolean DEBUG_BORDERS = false;
    static final boolean DEBUG_REPAINT_AREA = false;
    
    List<BufferedDeskletContainer> desklets;
    JFrame frame;
    JPanel panel;
    
    private JComponent dock;
    Container hidden;
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM() {
        hidden = new JDialog();
        //hidden.setVisible(true);
        RepaintManager.setCurrentManager(new DeskletRepaintManager(this));
        desklets = new ArrayList<BufferedDeskletContainer>();
        
        panel = new DeskletRenderPanel(this);
        frame = new JFrame("AB5k");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,"Center");
        MouseRedispatcher mouse = new MouseRedispatcher(this);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        
        
        JButton btn = new JButton("stop");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BeanArrayList<DeskletRunner> runners = DeskletManager.getInstance().getRunners();
                if(runners.size() > 0) {
                    DeskletRunner d = runners.get(0);
                    stop(d);
                }
            }
        });
        panel.add(btn);
        
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Registry registry = Registry.getInstance();
                BeanArrayList<DeskletConfig> configs = registry.getDeskletConfigs();
                if(configs.size() > 0) {
                    DeskletConfig d = configs.get(0);
                    start(d);
                }
            }
        });
        panel.add(startButton);
    }
    
    private void stop(final DeskletRunner d) {
        new Thread(new Runnable() {
            public void run() {
                DeskletManager manager = DeskletManager.getInstance();
                manager.shutdownDesklet( d.getConfig().getUUID() );
            }
        }).start();
    }
    
    private void start(final DeskletConfig d) {
        new Thread(new Runnable() {
            public void run() {
                DeskletManager manager = DeskletManager.getInstance();
                try {
                    manager.startDesklet( d.getUUID() );
                } catch (LifeCycleException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
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
                comp.setSize(new Dimension((int)bdc.getSize().getWidth(),
                        (int)bdc.getSize().getHeight()));
                comp.setLocation(0,0);
                redispatchToLowest(comp,e);
            }
        }
    }
    
    private void redispatchToLowest(JComponent comp, MouseEvent e) {
        //u.p("comp = " + comp.getClass());
        //u.p("e = " + e);
        
        // translate into the top component space
        e.translatePoint(comp.getX(),comp.getY());
        //u.p("e = " + e);
        //u.p("point = " + e.getPoint());
        
        // find the deepest child to send this event to
        Component child = SwingUtilities.getDeepestComponentAt(comp,e.getPoint().x,e.getPoint().y);
        Point pt2 = SwingUtilities.convertPoint(comp,e.getPoint(),child);
        //u.p("pt2 = " + pt2);
        //u.p("final child = " + child);
        
        if(child != null) {
            // pass the mouse event back up the stack if this component doesn't
            // care about mouse events. w/o this we'd lose dragging
            // go back up the stack
            while(child.getMouseListeners() == null ||
                    child.getMouseListeners().length == 0) {
                pt2.translate(child.getX(),child.getY());
                child = child.getParent();
                //u.p("back up to parent " + child);
            }
            MouseEvent e2 = new MouseEvent(child,
                    e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    pt2.x,
                    pt2.y,
                    e.getXOnScreen(),
                    e.getYOnScreen(),
                    e.getClickCount(),
                    e.isPopupTrigger(),
                    e.getButton());
            //u.p("e2 = " + e2);
            child.dispatchEvent(e2);
        }
        //child.dispatchEvent(e);
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
        hidden.add(((BufferedDeskletContainer)dc).comp);
        ((BufferedDeskletContainer)dc).setLocation(new Point(100,100));
        Animator anim = new Animator(750);
        anim.addTarget(new PropertySetter(dc,"location",new Point(100,100), new Point(100,100)));
        anim.addTarget(new PropertySetter(dc,"alpha",0f,1f));
        anim.addTarget(new PropertySetter(dc,"rotation",Math.PI,Math.PI*2.0));
        anim.addTarget(new PropertySetter(dc,"scale",0.1,1.0));
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
    
    
}
