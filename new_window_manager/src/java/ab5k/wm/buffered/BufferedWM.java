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
import ab5k.security.ContainerFactory;
import ab5k.security.DefaultContext;
import ab5k.security.DeskletConfig;
import ab5k.security.DeskletManager;
import ab5k.security.DeskletRunner;
import ab5k.security.LifeCycleException;
import ab5k.security.Registry;
import ab5k.util.AnimRepainter;
import ab5k.util.GlobalMouse;
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
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXBoxPanel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.joshy.util.u;

/**
 * An implementation of WindowManager that uses buffered images instead of
 * internal frames a desktop pane to show the desklets on screen.
 * @author joshy
 */
public class BufferedWM extends WindowManager {
    
    static final boolean DEBUG_BORDERS = false;
    static final boolean DEBUG_REPAINT_AREA = false;
    static final boolean SHOW_FRAME_TITLE_BAR = true;
    
    private List<BaseDC> desklets;
    private Map<BaseDC,List<BaseDC>> dialogMap;
    JFrame frame;
    DeskletRenderPanel panel;
    
    private JComponent dock;
    Container hidden;
    private DeskletContainer selectedDesklet = null;
    private Point selectedDeskletOffset;
    
    Core core;
    GlobalMouse globalMouseService = GlobalMouse.getInstance();
    
    private boolean oldAnim = false;
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM(final Core core) {
        this.core = core;
        hidden = new JDialog();
        //hidden.setVisible(true);
        //hidden.setVisible(true);
        RepaintManager.setCurrentManager(new DeskletRepaintManager(this));
        desklets = new ArrayList<BaseDC>();
        dialogMap = new HashMap<BaseDC,List<BaseDC>>();
        
        
        panel = new DeskletRenderPanel(this);
        frame = new JFrame("AB5k");
        if(!SHOW_FRAME_TITLE_BAR) {
            frame.setUndecorated(true);
        }
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,"Center");
        MouseRedispatcher mouse = new MouseRedispatcher(this);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        
        // deal with converting internal to external desklets
        MouseAdapter ma = new InternalToExternalMouseHandler(core);
        panel.addMouseListener(ma);
        panel.addMouseMotionListener(ma);
        
        setupManageButtons();
        globalMouseService = new CustomGlobalMouseService();
        //setupPopupHacking();
    }
    
    public void setupManageButtons() {
        final JXBoxPanel buttonPanel = new JXBoxPanel();
        buttonPanel.setBackgroundPainter(new RectanglePainter(3,3,3,3, 20,20, true,
                Color.ORANGE, 3, Color.BLACK));
        buttonPanel.setPadding(new Insets(5,5,5,5));
        buttonPanel.setOpaque(false);
        final ManagePanelAnimations mpa = new ManagePanelAnimations(this);
        
        final JToggleButton manageButton = new JToggleButton("Manage Widgets");
        manageButton.setOpaque(false);
        manageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(manageButton.isSelected()) {
                    mpa.showManagePanel();
                    mpa.moveDeskletsToColumns(manageButton);
                } else {
                    mpa.hideManagePanel();
                    mpa.moveDeskletsToOriginalPositions(manageButton);
                }
            }
        });
        buttonPanel.add(manageButton);
        final JButton getMore = new JButton("Get More Widgets");
        getMore.setOpaque(false);
        buttonPanel.add(getMore);
        
        panel.add(buttonPanel);
        panel.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentResized(ComponentEvent e) {
                u.p("resized");
                buttonPanel.setLocation(panel.getWidth()-buttonPanel.getWidth(),0);
            }
            public void componentShown(ComponentEvent e) {
            }
        });
    }
    
    void stop(DeskletContainer dc) {
        if(dc instanceof BufferedDeskletContainer) {
            DeskletManager manager = DeskletManager.getInstance();
            BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
            DeskletRunner runner = manager.getDeskletRunner(bdc.getContext());
            manager.stopRunner(runner);
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
            if(dc.isVisible()) {
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
        }
        return null;
    }
    
    void raiseWindow(MouseEvent e) {
        BufferedDeskletContainer bdc = findContainer(e.getPoint());
        if(bdc != null) {
            // raise the container
            desklets.remove(bdc);
            desklets.add(bdc);
            // raise it's dialogs. josh: it's a map! how do we affect the order?
            selectedDesklet = bdc;
            selectedDeskletOffset = new Point(
                    (int)(e.getPoint().getX() - selectedDesklet.getLocation().getX()),
                    (int)(e.getPoint().getY() - selectedDesklet.getLocation().getY()));
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
        int x = Integer.parseInt(context.getPreference(ContainerFactory.LOCATION_X,"300"));
        int y = Integer.parseInt(context.getPreference(ContainerFactory.LOCATION_Y,"300"));
        cont.setLocation(new Point(x,y));
        context.services.put(ab5k.desklet.services.GlobalMouse.class, globalMouseService);
        return cont;
    }
    
    public DeskletContainer convertInternalToExternalContainer(DeskletContainer dc) {
        if(dc == null) return null;
        BaseDC bdc = (BaseDC) dc;
        DefaultContext context = bdc.getContext();
        
        // remove all the old stuff
        if(bdc instanceof BufferedDeskletContainer) {
            hidden.remove(((BufferedDeskletContainer)bdc).comp);
        }
        getDesklets().remove(bdc);
        
        // create a new container
        JFrameDeskletContainer newContainer = new JFrameDeskletContainer(this, context);
        
        // copy the settings
        newContainer.setContent(bdc.getContent());
        newContainer.setShaped(bdc.isShaped());
        
        getDesklets().add(newContainer);
        newContainer.pack();
        newContainer.setVisible(true);
        context.setContainer(newContainer);
        return newContainer;
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
        //bdc.setLocation(new Point(100,100));
        Animator anim = new Animator(750);
        anim.addTarget(new PropertySetter(dc,"location",bdc.getLocation(), bdc.getLocation()));
        anim.addTarget(new PropertySetter(dc,"alpha",0f,1f));
        anim.addTarget(new PropertySetter(dc,"rotation",Math.PI,Math.PI*2.0));
        anim.addTarget(new PropertySetter(dc,"scale",0.1,1.0));
        anim.addTarget(new AnimRepainter(panel));
        anim.start();
    }
    
    public void animateDestruction(final DeskletContainer dc) {
        final BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
        if(oldAnim) {
            Animator anim = new Animator(500);
            //anim.addTarget(new PropertySetter(dc,"location",new Point(0,0), new Point(200,200)));
            anim.addTarget(new PropertySetter(dc,"alpha",bdc.getAlpha(),0f));
            //anim.addTarget(new PropertySetter(dc,"rotation",0f,(float)Math.PI*2f*5f));
            anim.addTarget(new PropertySetter(dc,"scale",bdc.getScale(),0.3));
            anim.addTarget(new AnimRepainter(panel));
            anim.addTarget(new TimingTargetAdapter() {
                public void end() {
                    destroyContainer(dc);
                }
            });
            anim.start();
        } else {
            
            Animator anim = new Animator(1000);
            anim.addTarget(new AnimRepainter(panel));
            anim.addTarget(new TimingTargetAdapter() {
                public void end() {
                    destroyContainer(dc);
                }
            });
            int num = 5;
            BSurface[][] surfaces = new BSurface[num][num];
            int w = (int)dc.getSize().getWidth()/num;
            int h = (int)dc.getSize().getHeight()/num;
            
            Point2D pt = bdc.getLocation();
            for(int i=0; i<num; i++) {
                surfaces[i] = new BSurface[num];
                for(int j = 0; j<num; j++) {
                    BSurface s  = new BSurface();
                    /*
                    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = img.createGraphics();
                    g.drawImage(bdc.getBuffer(),0,0,w,h, 
                            0+i*w, 0+j*h, 0+i*w+w, 0+j*h+h, null);
                    g.setColor(Color.GREEN);
                    g.drawRect(0,0,w-1,h-1);
                    g.dispose();
                    //s.setImg(img);*/
                    s.setSize(new Dimension(w,h));//img.getWidth(), img.getHeight()));
                    
                    s.subRect = new Rectangle(0+i*w, 0+j*h, w, h);
                    
                    surfaces[i][j] = s;
                    bdc.surfaces.add(s);
                    int distx = i-num/2;
                    int disty = j-num/2;
                    anim.addTarget(new PropertySetter(s,"location",
                            new Point2D.Double(
                                pt.getX()+i*w, 
                                pt.getY()+j*h), 
                            new Point2D.Double(
                                pt.getX()+i*w + 100*distx,
                                pt.getY()+j*h + 100*disty))); 
                    anim.addTarget(new PropertySetter(s,"alpha",1f,0.7f,0f));
                    anim.addTarget(new PropertySetter(s,"rotation",0.0,
                            ((Math.random()*2)-1)*
                            Math.PI*2));
                }
            }
            
            anim.addTarget(new TimingTarget() {
                public void begin() {
                    bdc.setVisible(false);
                    bdc.showSurfaces = true;
                }
                public void end() {
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            });
            Animator astart = new Animator(250);
            astart.addTarget(new AnimRepainter(panel));
            astart.addTarget(new PropertySetter(bdc,"scale",bdc.getScale(), 1.0));
            astart.addTarget(new StartAnimAfter(anim));
            astart.addTarget(new TimingTarget() {
                public void begin() {
                }
                public void end() {
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            });
            astart.start();
        }
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
        try {
            if(deskletContainer instanceof BufferedDeskletContainer) {
                BufferedDeskletContainer bdc = (BufferedDeskletContainer) deskletContainer;
                BufferedDeskletContainer dialog = new BufferedDialogContainer(this,bdc.getContext(),bdc);
                addDialog(bdc,dialog);
                hidden.add(dialog.comp);
                panel.repaint();
                return dialog;
            }
            if(deskletContainer instanceof JFrameDeskletContainer) {
                JFrameDeskletContainer dc = (JFrameDeskletContainer) deskletContainer;
                JDialogDeskletContainer dialog = new JDialogDeskletContainer(this,dc.getContext(),dc);
                addDialog(dc,dialog);
                return dialog;
            }
        } catch (Throwable thr) {
            u.p(thr);
        }
        throw new IllegalArgumentException(
                "The Buffered Window Manager cannot accept DeskletContainer's of type: "
                + deskletContainer.getClass().getName());
    }
    
    public List<BaseDC> getDesklets() {
        return desklets;
    }
    
    void setDesklets(List<BaseDC> desklets) {
        this.desklets = desklets;
    }
    
    
    private void addDialog(BaseDC dc, BaseDC dialog) {
        if(!dialogMap.containsKey(dc)) {
            dialogMap.put(dc,new ArrayList<BaseDC>());
        }
        dialogMap.get(dc).add(dialog);
    }
    
    List<BaseDC> getWindows() {
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
    
    private void setupPopupHacking() {
        PopupFactory.setSharedInstance(new PopupFactory() {
            public Popup getPopup(final Component owner, final Component contents, int x, int y) throws IllegalArgumentException {
                return new BufferedPopup(BufferedWM.this, owner, contents);
            }
        });
    }
    
    Component getTopParent(Component comp, Class targetClass) {
        if(targetClass.isAssignableFrom(comp.getClass())) {
            return comp;
        }
        return getTopParent(comp.getParent(),targetClass);
    }
    
    private class InternalToExternalMouseHandler extends MouseAdapter {
        boolean wasDragging = false;
        
        private Core core;
        
        public InternalToExternalMouseHandler(Core core) {
            super();
            this.core = core;
        }
        
        public void mouseClicked(MouseEvent e) {
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseMoved(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
            wasDragging = false;
        }
        
        
        public void mouseDragged(MouseEvent e) {
            wasDragging = true;
            if (e.getPoint().getX() < 20) {
                if(!core.getCloser().isWindowClosed()) {
                    core.getCollapseWindowAction().doCollapse();
                    selectedDesklet = convertInternalToExternalContainer(selectedDesklet);
                }
            }
            
            if(core.getCloser().isWindowClosed() &&
                    selectedDesklet instanceof JFrameDeskletContainer) {
                Point pt = e.getPoint();
                SwingUtilities.convertPointToScreen(pt,panel);
                selectedDesklet.setLocation(new Point(pt.x - selectedDeskletOffset.x,
                        pt.y - selectedDeskletOffset.y));
                
            }
            
            if(selectedDesklet != null && selectedDesklet.getLocation().getX() < 0) {
                showDeskletInGlasspane();
            } else {
                hideDeskletInGlasspane();
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            hideDeskletInGlasspane();
            if (e.getPoint().getX() < 0 && wasDragging) {
            }
        }
        
        public void mouseExited(MouseEvent e) {
        }
        
        JPanel intExtGlasspane = new JPanel() {
            public void paintComponent(Graphics g) {
                //u.p("painting");
                if(selectedDesklet != null) {
                    if(selectedDesklet instanceof BufferedDeskletContainer) {
                        BufferedDeskletContainer bdc = (BufferedDeskletContainer) selectedDesklet;
                        Point pt = panel.getLocation();
                        pt.translate((int)bdc.getLocation().getX(),
                                (int)bdc.getLocation().getY());
                        g.drawImage(bdc.getBuffer(),
                                (int)pt.getX(),
                                (int)pt.getY(), null);
                    }
                }
            }
        };
        
        private void showDeskletInGlasspane() {
            if(frame.getGlassPane() != intExtGlasspane) {
                intExtGlasspane.setOpaque(false);
                frame.setGlassPane(intExtGlasspane);
                intExtGlasspane.setVisible(true);
            }
            intExtGlasspane.setVisible(true);
            intExtGlasspane.repaint();
        }
        
        private void hideDeskletInGlasspane() {
            intExtGlasspane.setVisible(false);
        }
    }
    
    private class CustomGlobalMouseService extends GlobalMouse {
        
        protected Point convertPointToComponent(JComponent comp, Point pt) {
            Component topParent = getTopParent(comp);
            
            // if this is a component in a buffered desklet container.
            if(topParent == hidden) {
                DeskletToplevel top = (DeskletToplevel)BufferedWM.this.getTopParent(comp,DeskletToplevel.class);
                BufferedDeskletContainer bdc = top.getContainer();
                Point pt2 = new Point(pt);
                SwingUtilities.convertPointFromScreen(pt2,BufferedWM.this.panel);
                pt2.translate(-(int)bdc.getLocation().getX(), -(int)bdc.getLocation().getY());
                Point pt3 = convertPointFromParentToChild(top,comp, pt2);
                return pt3;
            } else {
                return super.convertPointToComponent(comp,pt);
            }
        }
        
        private Component getTopParent(Component comp) {
            return SwingUtilities.getWindowAncestor(comp);
        }
        
        
        // convert from the child point to the parent point. the child must be a real child of this parent
        private Point convertPointFromParentToChild(Component parent, Component child, Point pt) {
            if(parent == child) {
                return pt;
            }
            Point pt2 = new Point(pt);
            pt2.translate(child.getLocation().x,child.getLocation().y);
            return convertPointFromParentToChild(parent, child.getParent(), pt2);
        }
    }
    
    
    
}
