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
import ab5k.util.AnimRepainter;
import ab5k.util.GlobalMouse;
import ab5k.util.GraphicsUtil;
import ab5k.wm.WindowManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.RepaintManager;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.swingx.JXBoxPanel;
import org.jdesktop.swingx.JXInsets;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
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
    static final boolean SHOW_FRAME_TITLE_BAR = false;
    static final boolean TRANSPARENT_DOCK = false;
    
    private List<BaseDC> desklets;
    private Map<BaseDC,List<BaseDC>> dialogMap;
    JFrame frame;
    Component panel;
    
    private JComponent dock;
    Container hidden;
    DeskletContainer selectedDesklet = null;
    Point selectedDeskletOffset;
    boolean isManageMode = false;
    
    Core core;
    GlobalMouse globalMouseService = GlobalMouse.getInstance();
    
    private boolean oldAnim = false;
    
    
    /** Creates a new instance of BufferedWM */
    public BufferedWM(final Core core) {
        this.core = core;
        desklets = new ArrayList<BaseDC>();
        dialogMap = new HashMap<BaseDC,List<BaseDC>>();
    }
    
    public void init() {
        RepaintManager.setCurrentManager(new DeskletRepaintManager(this));
        hidden = new JDialog();
        panel = createRenderPanel();
        frame = new JFrame("AB5k");
        if(!SHOW_FRAME_TITLE_BAR) {
            frame.setUndecorated(true);
        }
        if(TRANSPARENT_DOCK) {
            frame.setBackground(new Color(0,0,0,0));
        }
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,"Center");
        
        MouseRedispatcher mouse = new MouseRedispatcher(this);
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        
        // deal with converting internal to external desklets
        MouseAdapter ma = new InternalToExternalMouseHandler(core,this);
        panel.addMouseListener(ma);
        panel.addMouseMotionListener(ma);
        setupManageButtons();
        globalMouseService = new CustomGlobalMouseService(this);
        //setupPopupHacking();
        
        // set up the background
        try {
            ImagePainter im2 = new ImagePainter(getClass().getResource("/backgrounds/di-sails-blue.png"));
            im2.setHorizontalAlignment(ImagePainter.HorizontalAlignment.LEFT);
            im2.setInsets(new JXInsets(0,-180,0,0));
            if(panel instanceof JXPanel) {
                ((JXPanel)panel).setBackgroundPainter(new CompoundPainter(im2));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // setup the animations
        setDeskletCreationTransition(new StdCreationAnimation());
        setDeskletDestructionTransition(new StdDestructionAnimation());
    }
    
    protected Component createRenderPanel() {
        return new DeskletRenderPanel(this);
    }
    
    void setupManageButtons() {
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
                    isManageMode = true;
                } else {
                    mpa.hideManagePanel();
                    mpa.moveDeskletsToOriginalPositions(manageButton);
                    isManageMode = false;
                }
            }
        });
        buttonPanel.add(manageButton);
        final JButton getMore = new JButton("Get More Widgets");
        getMore.setOpaque(false);
        buttonPanel.add(getMore);
        
        if(panel instanceof Container) {
            ((Container)panel).add(buttonPanel);
        }
        panel.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentResized(ComponentEvent e) {
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
    
    public void convertInternalToExternalContainer(DeskletContainer dc) {
        if(dc == null) return;
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
        newContainer.setVisible(true);
    }
    
    public void convertExternalToInternalContainer(DeskletContainer dc) {
        if(dc == null) return;
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
    }
    
    
    public void setDeskletCreationTransition(TransitionAnimation anim) {
        this.creationTransition = anim;
    }
    
    public void showContainer(DeskletContainer dc) {
        
        final BaseDC bdc = (BaseDC) dc;
        getDesklets().add(bdc);
        if(bdc instanceof BufferedDeskletContainer) {
            hidden.add(((BufferedDeskletContainer)dc).comp);
        }
        Animator anim = creationTransition.createAnimation(new TransitionEvent(this,null,(BufferedDeskletContainer)dc));
        if(panel instanceof JComponent) {
            anim.addTarget(new AnimRepainter((JComponent)panel));
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
        if(panel instanceof JComponent) {
            anim.addTarget(new AnimRepainter((JComponent)panel));
        }
        anim.start();
    }
    
    private void realdestroyContainer(DeskletContainer dc) {
        //panel.remove(((BufferedDeskletContainer)dc).comp);
        getDesklets().remove(dc);
        panel.repaint();
    }
    
    /* === methods to manipulate desklet containers ===== */
    
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
    
    
    private TransitionAnimation creationTransition;
    private TransitionAnimation destructionTransition;
    
    public DeskletContainer createExternalContainer(DefaultContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
