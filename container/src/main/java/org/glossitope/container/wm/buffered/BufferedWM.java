/*
 * BufferedWM.java
 *
 * Created on March 20, 2007, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.wm.buffered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.swingx.JXBoxPanel;
import org.jdesktop.swingx.JXInsets;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.RectanglePainter;

import org.glossitope.container.Core;
import org.glossitope.container.DesktopBackground;
import org.glossitope.container.Environment;
import org.glossitope.container.MainPanel;
import org.glossitope.desklet.DeskletContainer;
import org.glossitope.container.security.ContainerFactory;
import org.glossitope.container.security.DefaultContext;
import org.glossitope.container.security.DeskletConfig;
import org.glossitope.container.security.DeskletManager;
import org.glossitope.container.security.DeskletRunner;
import org.glossitope.container.security.LifeCycleException;
import org.glossitope.container.util.AnimRepainter;
import org.glossitope.container.util.GlobalMouse;
import org.glossitope.container.util.GraphicsUtil;
import org.glossitope.container.wm.WindowManager;
import org.glossitope.container.wm.buffered.animations.StdCreationAnimation;
import org.glossitope.container.wm.buffered.animations.StdDestructionAnimation;
import org.glossitope.container.wm.buffered.animations.TransitionAnimation;
import org.glossitope.container.wm.buffered.animations.TransitionEvent;
import org.glossitope.container.wm.buffered.manage.ManagePanelAnimations;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyListener;
import java.net.URI;
import org.joshy.util.u;

/**
 * An implementation of WindowManager that uses buffered images instead of
 * internal frames a desktop pane to show the desklets on screen.
 * @author joshy
 */
public class BufferedWM extends WindowManager {
    
    static final boolean DEBUG_BORDERS = false;
    static final boolean DEBUG_REPAINT_AREA = false;
    static final boolean SHOW_FRAME_TITLE_BAR = Environment.showFrameTitleBar;
    static final boolean TRANSPARENT_DOCK = false;
    
    private List<DeskletProxy> proxies;
    private Component renderPanel;
    private JComponent dock;
    private boolean manageMode = false;
    private TransitionAnimation creationTransition;
    private TransitionAnimation destructionTransition;
    private GlobalMouse globalMouseService = GlobalMouse.getInstance();
    private boolean oldAnim = false;
    
    
    JFrame frame;
    Core core;
    Container hidden;
    BufferedDeskletContainer selectedDesklet = null;
    Point selectedDeskletOffset;
    
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM(final Core core) {
        this.core = core;
        //dialogMap = new HashMap<BaseDC,List<BaseDC>>();
        setProxies(new ArrayList<DeskletProxy>());
    }
    
    public void init() {
        RepaintManager.setCurrentManager(new DeskletRepaintManager(this));
        hidden = new JDialog();
        renderPanel = createRenderPanel();
        frame = new JFrame("Glossitope");
        if(!SHOW_FRAME_TITLE_BAR) {
            frame.setUndecorated(true);
        }
        if(TRANSPARENT_DOCK) {
            frame.setBackground(new Color(0,0,0,0));
        }
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(getRenderPanel(), "Center");
        
        final MouseRedispatcher mouse = new MouseRedispatcher(this);
        getRenderPanel().addMouseListener(mouse);
        getRenderPanel().addMouseMotionListener(mouse);
        
        // deal with converting internal to external desklets
        MouseAdapter ma = new InternalToExternalMouseHandler(core,this);
        getRenderPanel().addMouseListener(ma);
        getRenderPanel().addMouseMotionListener(ma);
        setupManageButtons();
        globalMouseService = new CustomGlobalMouseService(this);
        //setupPopupHacking();
        
        // set up the background
        try {
            ImagePainter im2 = new ImagePainter(getClass().getResource("/skins/backgrounds/di-sails-blue.png"));
            im2.setHorizontalAlignment(ImagePainter.HorizontalAlignment.LEFT);
            im2.setInsets(new JXInsets(0,-180,0,0));
            if(getRenderPanel() instanceof JXPanel) {
                ((JXPanel)getRenderPanel()).setBackgroundPainter(new CompoundPainter(im2));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // setup the animations
        setDeskletCreationTransition(new StdCreationAnimation());
        setDeskletDestructionTransition(new StdDestructionAnimation());
        
        // setup the keyboard hacks
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
                public boolean dispatchKeyEvent(KeyEvent e) {
                    if(e.getComponent() == mouse.lastComp) {
                        return false;
                    }
                    if(mouse.lastComp != null) {
                        //u.p("last comp = " + mouse.lastComp);
                        //u.p("old ke = " + e);
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(mouse.lastComp, e);
                        /*
                        KeyEvent ke = new KeyEvent(mouse.lastComp, e.getID(), e.getWhen(),
                                e.getModifiers(), e.getKeyCode(), e.getKeyChar(),
                                e.getKeyLocation());
                        e.consume();*/
                        /*
                        mouse.lastComp.dispatchEvent(ke);
                        for(KeyListener kl : mouse.lastComp.getKeyListeners()) {
                            kl.keyTyped(ke);
                        }
                        u.p("new ke = " + ke);*/
                        return true;
                    }
                    return false;
                }
        });
    }
    
    protected Component createRenderPanel() {
        DeskletRenderPanel panel = new DeskletRenderPanel(this);
        panel.setFocusable(true);
        return panel;
    }
    
    void setupManageButtons() {
        final JXBoxPanel buttonPanel = new JXBoxPanel();
        buttonPanel.setBackgroundPainter(new RectanglePainter(3,3,3,3, 20,20, true,
                new Color(0x9eeb06), 3, Color.WHITE));
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
                    setManageMode(true);
                } else {
                    mpa.hideManagePanel();
                    mpa.moveDeskletsToOriginalPositions(manageButton);
                    setManageMode(false);
                }
            }
        });
        buttonPanel.add(manageButton);
        final JButton getMore = new JButton("Get More Widgets");
        getMore.setOpaque(false);
        getMore.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop dk = java.awt.Desktop.getDesktop();
                    dk.browse(new java.net.URI("http://www.glossitope.org/site/org.ab5k.web.Gallery/"));
                } catch (Exception ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
        });
        buttonPanel.add(getMore);
        
        if(getRenderPanel() instanceof Container) {
            ((Container)getRenderPanel()).add(buttonPanel);
        }
        getRenderPanel().addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentResized(ComponentEvent e) {
                buttonPanel.setLocation(getRenderPanel().getWidth()-buttonPanel.getWidth(),0);
            }
            public void componentShown(ComponentEvent e) {
            }
        });
    }
    
    public void stop(DeskletContainer dc) {
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
    
    public void start(final DeskletConfig d) {
        new Thread(new Runnable() {
            public void run() {
                DeskletManager manager = DeskletManager.getInstance();
                try {
                    manager.startDesklet( d.getUUID() );
                } catch (LifeCycleException ex) {
                    ex.printStackTrace();
                }
            }
        },"BufferedWM:start").start();
    }
    
    BufferedDeskletContainer findContainer(Point pt) {
        for(int i=getProxies().size() -1; i>=0; i--) {
            DeskletProxy proxy = getProxies().get(i);
            //dialogs of a desklet should always be above the desklet itself, so search them first
            if(findContainer(pt,proxy.configContainer)) {
                return proxy.configContainer;
            }
            if(findContainer(pt,proxy.contentContainer)) {
                return proxy.contentContainer;
            }
        }
        return null;
    }
    
    private boolean findContainer(Point pt, BufferedDeskletContainer dc) {
        if(dc.isVisible()) {
            if(dc.getPeer() instanceof Buffered2DPeer) {
                Point loc = GraphicsUtil.toPoint(dc.getLocation());
                Dimension2D size = dc.getSize();
                Rectangle rect = new Rectangle(loc.x, loc.y, (int)size.getWidth(), (int)size.getHeight());
                if(rect.contains(pt)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    void raiseWindow(MouseEvent e) {
        BufferedDeskletContainer bdc = findContainer(e.getPoint());
        if(bdc != null) {
            // raise the container
            proxies.remove(bdc.getProxy());
            proxies.add(bdc.getProxy());
            // raise it's dialogs. josh: it's a map! how do we affect the order?
            selectedDesklet = bdc;
            selectedDeskletOffset = new Point(
                    (int)(e.getPoint().getX() - selectedDesklet.getLocation().getX()),
                    (int)(e.getPoint().getY() - selectedDesklet.getLocation().getY()));
            getRenderPanel().repaint();
        }
    }
    
    
    /* ===== methods dealing with the overall conatiner === */
    public Object getTopLevel() {
        return frame;
    }
    
    public Dimension getContainerSize() {
        return getRenderPanel().getSize();
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
        DeskletProxy proxy = new DeskletProxy(context, this);
        proxy.contentContainer.setPeer(new Buffered2DPeer(proxy.contentContainer));
        int x = Integer.parseInt(context.getPreference(ContainerFactory.LOCATION_X,"300"));
        int y = Integer.parseInt(context.getPreference(ContainerFactory.LOCATION_Y,"300"));
        proxy.contentContainer.setLocation(new Point(x,y));
        context.services.put(org.glossitope.desklet.services.GlobalMouse.class, globalMouseService);
        getProxies().add(proxy);
        return proxy.contentContainer;
    }
    
    public void convertInternalToExternalContainer(DeskletContainer dc) {
        if(dc == null) return;
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
        DefaultContext context = bdc.getContext();
        Point pt = GraphicsUtil.toPoint(bdc.getLocation());
        SwingUtilities.convertPointToScreen(pt,renderPanel);
        
        // create a new container
        JFramePeer peer = new JFramePeer(bdc);
        peer.setContent(bdc.getContent());
        bdc.setPeer(peer);
        bdc.pack();
        bdc.setLocation(pt);
        bdc.setVisible(true);
    }
    
    public void convertExternalToInternalContainer(DeskletContainer dc) {
        if(dc == null) return;

        BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
        DefaultContext context = bdc.getContext();
        Point pt = GraphicsUtil.toPoint(bdc.getLocation());
        Buffered2DPeer peer = new Buffered2DPeer(bdc);
        bdc.setContent(bdc.getContent());
        //peer.setContent(bdc.getContent());
        bdc.setPeer(peer);
        bdc.pack();
        bdc.setLocation(pt);
        bdc.setVisible(true);
    }
    
    
    public void setDeskletCreationTransition(TransitionAnimation anim) {
        this.creationTransition = anim;
    }
    
    public void showContainer(DeskletContainer dc) {
        final BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
        bdc.setVisible(true);
        /*
        getDesklets().add(bdc);
        if(bdc instanceof BufferedDeskletContainer) {
            hidden.add(((BufferedDeskletContainer)dc).comp);
        }*/
        
        Animator anim = creationTransition.createAnimation(new TransitionEvent(this,null,(BufferedDeskletContainer)dc));
        if(getRenderPanel() instanceof JComponent) {
            anim.addTarget(new AnimRepainter((JComponent)getRenderPanel()));
        }
        anim.start();
    }
    
    public void setDeskletDestructionTransition(TransitionAnimation anim) {
        this.destructionTransition = anim;
    }
    
    public void destroyContainer(final DeskletContainer dc) {
        Animator anim = destructionTransition.createAnimation(new TransitionEvent(this,null,(BufferedDeskletContainer)dc));
        anim.addTarget(new TimingTargetAdapter() {
            public void end() {
                realdestroyContainer(dc);
            }
        });
        if(getRenderPanel() instanceof JComponent) {
            anim.addTarget(new AnimRepainter((JComponent)getRenderPanel()));
        }
        anim.start();
    }
    
    private void realdestroyContainer(DeskletContainer dc) {
        BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
        //panel.remove(((BufferedDeskletContainer)dc).comp);
        getProxies().remove(bdc.getProxy());
        bdc.setVisible(false);
        getRenderPanel().repaint();
    }
    
    /* === methods to manipulate desklet containers ===== */
    
    public DeskletContainer createDialog(DeskletContainer deskletContainer) {
        try {
            BufferedDeskletContainer bdc = (BufferedDeskletContainer) deskletContainer;
            DeskletProxy proxy = bdc.getProxy();
            if(bdc.getPeer() instanceof Buffered2DPeer) {
                proxy.configContainer.setPeer(new Buffered2DPeer(proxy.configContainer,true));
                return proxy.configContainer;
            }
            if(bdc.getPeer() instanceof JFramePeer) {
                JFramePeer dialog = new JFramePeer(proxy.configContainer, true);
                proxy.configContainer.setPeer(dialog);
                dialog.setContent(proxy.configContainer.getTopComponent());
                return proxy.configContainer;
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        throw new IllegalArgumentException(
                "The Buffered Window Manager cannot accept DeskletContainer's of type: "
                + deskletContainer.getClass().getName());
    }
    /*
    private void addDialog(BaseDC dc, BaseDC dialog) {
        if(!dialogMap.containsKey(dc)) {
            dialogMap.put(dc,new ArrayList<BaseDC>());
        }
        dialogMap.get(dc).add(dialog);
    }*/
    /*
    private void setupPopupHacking() {
        PopupFactory.setSharedInstance(new PopupFactory() {
            public Popup getPopup(final Component owner, final Component contents, int x, int y) throws IllegalArgumentException {
                return new BufferedPopup(BufferedWM.this, owner, contents);
            }
        });
    }*/
    
    Component getTopParent(Component comp, Class targetClass) {
        if(targetClass.isAssignableFrom(comp.getClass())) {
            return comp;
        }
        return getTopParent(comp.getParent(),targetClass);
    }
    
    
    
    public DeskletContainer createExternalContainer(DefaultContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<DeskletProxy> getProxies() {
        return proxies;
    }
    
    public void setProxies(List<DeskletProxy> proxies) {
        this.proxies = proxies;
    }
    
    public boolean isManageMode() {
        return manageMode;
    }
    
    public void setManageMode(boolean manageMode) {
        this.manageMode = manageMode;
    }
    
    public Component getRenderPanel() {
        return renderPanel;
    }
}
