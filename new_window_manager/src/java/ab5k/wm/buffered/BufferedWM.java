/*
 * BufferedWM.java
 *
 * Created on March 20, 2007, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.Core;
import ab5k.DesktopBackground;
import ab5k.MainPanel;
import ab5k.desklet.DeskletContainer;
import ab5k.security.DefaultContext;
import ab5k.security.DeskletConfig;
import ab5k.security.DeskletManager;
import ab5k.security.DeskletRunner;
import ab5k.security.LifeCycleException;
import ab5k.security.Registry;
import ab5k.util.GraphicsUtil;
import ab5k.wm.WindowManager;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.CellRendererPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
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
    
    private List<BaseDC> desklets;
    private Map<BaseDC,List<BaseDC>> dialogMap;
    JFrame frame;
    DeskletRenderPanel panel;
    
    private JComponent dock;
    Container hidden;
    private DeskletContainer selectedDesklet = null;
    
    Core core;
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM(final Core core) {
        this.core = core;
        hidden = new JDialog();
        //hidden.setVisible(true);
        RepaintManager.setCurrentManager(new DeskletRepaintManager(this));
        desklets = new ArrayList<BaseDC>();
        dialogMap = new HashMap<BaseDC,List<BaseDC>>();
        
        
        panel = new DeskletRenderPanel(this);
        frame = new JFrame("AB5k");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,"Center");
        MouseRedispatcher mouse = new MouseRedispatcher(this);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        
        // deal with converting internal to external desklets
        MouseAdapter ma = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseMoved(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                wasDragging = false;
            }
            boolean wasDragging = false;
            public void mouseDragged(MouseEvent e) {
                wasDragging = true;
                if(e.getPoint().getX() < -20) {
                    u.p("outside!");
                    u.p("must close");
                    core.getCollapseWindowAction().doCollapse();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(e.getPoint().getX() < 0 && wasDragging) {
                    u.p("dropped outside!");
                    convertInternalToExternalContainer(selectedDesklet);
                }
            }
            public void mouseExited(MouseEvent e) {
            }
        };
        panel.addMouseListener(ma);
        panel.addMouseMotionListener(ma);
        
        JPanel buttonPanel = new JPanel();
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
        //buttonPanel.add(btn);
        
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
        //buttonPanel.add(startButton);
        final ManagePanelAnimations mpa = new ManagePanelAnimations(this);
        
        final JToggleButton manageButton = new JToggleButton("Manage");
        manageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(manageButton.isSelected()) {
                    mpa.showManagePanel();
                    mpa.moveDeskletsToColumns();
                } else {
                    mpa.hideManagePanel();
                    mpa.moveDeskletsToOriginalPositions();
                }
            }
        });
        buttonPanel.add(manageButton);
        
        panel.add(buttonPanel);
        buttonPanel.setLocation(500/*panel.getWidth()-buttonPanel.getWidth()*/,0);
    }
    
    void stop(DeskletContainer dc) {
        if(dc instanceof BufferedDeskletContainer) {
            DeskletManager manager = DeskletManager.getInstance();
            manager.shutdownDesklet(
                    ((BufferedDeskletContainer)dc).getContext().getConfig().getUUID());
        }
    }
    void stop(final DeskletRunner d) {
        new Thread(new Runnable() {
            public void run() {
                DeskletManager manager = DeskletManager.getInstance();
                manager.shutdownDesklet( d.getConfig().getUUID() );
            }
        }).start();
    }
    
    void start(final DeskletConfig d) {
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
    
    BufferedDeskletContainer findContainer(Point pt) {
        for(int i=getWindows().size()-1; i>=0; i--) {
            DeskletContainer dc = getWindows().get(i);
            if(dc instanceof BufferedDeskletContainer) {
                BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
                
                Point loc = GraphicsUtil.toPoint(bdc.getLocation());
                Dimension2D size = bdc.getSize();
                Rectangle rect = new Rectangle(loc.x, loc.y, (int)size.getWidth(), (int)size.getHeight());
                if(rect.contains(pt)) {
                    return bdc;
                }
            }
        }
        return null;
    }
    
    void raiseWindow(MouseEvent e) {
        BufferedDeskletContainer bdc = findContainer(e.getPoint());
        if(bdc != null) {
            getWindows().remove(bdc);
            getWindows().add(bdc);
            selectedDesklet = bdc;
            panel.repaint();
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
        BufferedDeskletContainer cont = new BufferedDeskletContainer(this,context);
        cont.setLocation(new Point(0,0));
        return cont;
    }
    
    public DeskletContainer convertInternalToExternalContainer(DeskletContainer dc) {
        if(dc == null) return null;
        BaseDC bdc = (BaseDC) dc;
        getDesklets().remove(bdc);
        JFrameDeskletContainer cont = new JFrameDeskletContainer(this, bdc.getContext());
        cont.setContent(bdc.getContent());
        getDesklets().add(cont);
        cont.frame.pack();
        cont.setVisible(true);
        return cont;
    }
    
    public DeskletContainer convertExternalToInternalContainer(DeskletContainer dc) {
        if(dc == null) return null;
        BaseDC bdc = (BaseDC) dc;
        getDesklets().remove(bdc);
        JFrameDeskletContainer jdc = (JFrameDeskletContainer) dc;
        jdc.frame.getContentPane().remove(jdc.getContent());
        
        BufferedDeskletContainer cont = new BufferedDeskletContainer(this, bdc.getContext());
        cont.setContent(jdc.getContent());
        getDesklets().add(cont);
        hidden.add(cont.comp);
        cont.setLocation(new Point(100,100));
        panel.repaint();
        
        jdc.setVisible(false);
        jdc.frame.dispose();
        core.getCollapseWindowAction().doExpand();
        return cont;
    }
    
    public void animateCreation(DeskletContainer dc) {
        BaseDC bdc = (BaseDC) dc;
        getDesklets().add(bdc);
        if(bdc instanceof BufferedDeskletContainer) {
            hidden.add(((BufferedDeskletContainer)dc).comp);
        }
        bdc.setLocation(new Point(100,100));
        Animator anim = new Animator(750);
        anim.addTarget(new PropertySetter(dc,"location",new Point(100,100), new Point(100,100)));
        anim.addTarget(new PropertySetter(dc,"alpha",0f,1f));
        anim.addTarget(new PropertySetter(dc,"rotation",Math.PI,Math.PI*2.0));
        anim.addTarget(new PropertySetter(dc,"scale",0.1,1.0));
        anim.addTarget(new AnimRepainter(panel));
        anim.start();
    }
    
    public void animateDestruction(final DeskletContainer dc) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
        Animator anim = new Animator(500);
        //anim.addTarget(new PropertySetter(dc,"location",new Point(0,0), new Point(200,200)));
        anim.addTarget(new PropertySetter(dc,"alpha",bdc.getAlpha(),0f));
        //anim.addTarget(new PropertySetter(dc,"rotation",0f,(float)Math.PI*2f*5f));
        anim.addTarget(new PropertySetter(dc,"scale",bdc.getScale(),0.3));
        anim.addTarget(new AnimRepainter(panel));
        /*josh: dead code? new TimingTarget() {
            public void begin() {            }
            public void end() {            }
            public void repeat() {            }
            public void timingEvent(float f) {                panel.repaint();            }
        });*/
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
        getDesklets().remove(dc);
        panel.repaint();
    }
    
    /* === methods to manipulate desklet containers ===== */
    public void setLocation(DeskletContainer container, Point2D point) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) container;
        bdc.setLocation(point);
        panel.repaint();
    }
    
    public Point2D getLocation(DeskletContainer deskletContainer) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) deskletContainer;
        return bdc.getLocation();
    }

    public DeskletContainer createDialog(DeskletContainer deskletContainer) {
        u.p("creating a dialog");
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) deskletContainer;
        BufferedDeskletContainer dialog = new BufferedDeskletContainer(this,bdc.getContext());
        dialog.setLocation(new Point2D.Double(
                bdc.getLocation().getX()+bdc.getSize().getWidth()/2,
                bdc.getLocation().getY()+bdc.getSize().getHeight()/2));
        addDialog(bdc,dialog);
        panel.repaint();
        return dialog;
    }

    public List<BaseDC> getDesklets() {
        return desklets;
    }

    public void setDesklets(List<BaseDC> desklets) {
        this.desklets = desklets;
    }

    
    private void addDialog(BaseDC dc, BaseDC dialog) {
        if(!dialogMap.containsKey(dc)) {
            dialogMap.put(dc,new ArrayList<BaseDC>());
        }
        dialogMap.get(dc).add(dialog);
    }
            
    public List<BaseDC> getWindows() {
        List<BaseDC> windows = new ArrayList<BaseDC>();
        windows.addAll(desklets);
        for(List<BaseDC> dcs : dialogMap.values()) {
            windows.addAll(dcs);
        }
        return windows;
    }

    List<BaseDC> getDialogs(BufferedDeskletContainer dc) {
        if(!dialogMap.containsKey(dc)) {
            dialogMap.put(dc,new ArrayList<BaseDC>());
        }
        return dialogMap.get(dc);
    }

}
