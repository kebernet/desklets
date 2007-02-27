/*
 * Main.java
 *
 * Created on May 30, 2006, 4:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k;

import ab5k.actions.Closer;
import ab5k.actions.CollapseWindowAction;
import ab5k.actions.LoadDeskletAction;
import ab5k.actions.LoginToServerAction;
import ab5k.actions.MacSupport;
import ab5k.actions.QuitAction;
import ab5k.actions.ShowManageDialogAction;
import ab5k.actions.ShowPreferencesAction;
import ab5k.actions.ShowSplashscreen;
import ab5k.actions.SingleLaunchSupport;
import ab5k.backgrounds.GradientBackground;
import ab5k.backgrounds.RadarSweep;
import ab5k.backgrounds.TronWorld;
import ab5k.security.ContainerFactory;
import ab5k.security.DeskletManager;
import ab5k.security.LifeCycleException;
import ab5k.security.SecurityPolicy;
import ab5k.util.PlafUtil;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.security.Policy;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jdesktop.swingx.StackLayout;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class Main {
    private static final boolean trayEnabled = false;
    private static final boolean doFadeStartup = false;
    public Map iframes = new HashMap();
    private DeskletManager deskletManager;
    private ContainerFactory containerFactory;
    private BackgroundManager backgroundManager;
    public PreferencesPanel prefs;
    
    private MainPanel mainPanel;
    
    private JFrame frame;
    
    
    /** Creates a new instance of Main */
    public Main() {
        this.collapseWindowAction = new CollapseWindowAction(this);
    }
    
    
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        setBackgroundManager(new BackgroundManager(this));
        if(!doFadeStartup) {
            setupBackgrounds();
        }
        try {
            MacSupport.setupMacSupport(this);
        } catch (Throwable thr) {
            u.p("setting up mac support failed");
            thr.printStackTrace();
        }
        containerFactory = ContainerFactory.getInstance();
        containerFactory.init( getDesktop(), mainPanel.getDockPanel() );
        //setupSystemTray();
        deskletManager = DeskletManager.getInstance();
        
        try{
            deskletManager.startUp( this );
        } catch(LifeCycleException e){
            e.printStackTrace();
        }
        if(PlafUtil.isMacOSX()) {
        }
        
        quitAction.putValue(Action.NAME,"Quit");
        preferencesAction.putValue(Action.NAME,"Preferences");
    }
    
    private void setupBackgrounds() {
        
        getBackgroundManager().addBackground(new GradientBackground());
        getBackgroundManager().addBackground(new RadarSweep());
        //getBackgroundManager().addBackground(new NasaBackground());
        getBackgroundManager().addBackground(new TronWorld());
        getBackgroundManager().setDesktopBackground(
                (DesktopBackground)getBackgroundManager().getBackgrounds().get(0));
        
    }
    
    public void handleException(Exception ex) {
        u.p(ex);
    }
    
    public DeskletManager getDeskletManager() {
        return deskletManager;
    }
    
    public JDesktopPane getDesktop() {
        return getMainPanel().desktop;
    }
    
    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }
    
    public void setBackgroundManager(BackgroundManager backgroundManager) {
        this.backgroundManager = backgroundManager;
    }
    
    
    public Action quitAction = new QuitAction(this);
    public Action getQuitAction() {
        return quitAction;
    }
    
    private CollapseWindowAction collapseWindowAction;
    public CollapseWindowAction getCollapseWindowAction() {
        return collapseWindowAction;
    }
    
    public Action getShowManageDialogAction() {
        return new ShowManageDialogAction(this);
    }
    
    public Action preferencesAction = new ShowPreferencesAction(this);
    public Action getPreferencesAction() {
        return preferencesAction;
    }
    
    private LoginToServerAction loginToServerAction = new LoginToServerAction();
    public LoginToServerAction getLoginToServerAction() {
        return loginToServerAction;
    }
    
    public static void main(String ... args) {
        final Main main = new Main();
        if(SingleLaunchSupport.setupSingleLaunchSupport(main,args)){
            u.p("already running. sent the args and skipping out early");
            
            try {
                MacSupport.setupMacSupport(main);
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
            //System.exit(0);
        }
        
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        Policy.setPolicy( new SecurityPolicy() );
        System.setSecurityManager( new SecurityManager() );
        
        u.p("args");
        u.p(args);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ShowSplashscreen().show();
                    final JFrame frame = new JFrame("AB5k");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.addWindowListener(new WindowAdapter() {
                        public void windowClosed(WindowEvent e) {
                            main.getQuitAction().actionPerformed(new ActionEvent(this,-1,"quit"));
                        }
                    });
                    frame.setUndecorated(true);
                    frame.setAlwaysOnTop(true);
                    
                    MainPanel panel = new MainPanel(main);
                    main.mainPanel = panel;
                    main.frame = frame;
                    main.init();
                    
                    frame.setLayout(new StackLayout());
                    frame.add(main.getMainPanel());
                    frame.pack();
                    frame.setBounds(main.getCollapseWindowAction().getStartupPosition());
                    main.getCloser().setWindowClosed(true);
                    frame.setVisible(true);
                    
                    main.getLoginToServerAction().loginToServer();
                } catch (Exception ex) {
                    u.p(ex);
                }
            }
        });
        
    }
    
    public MainPanel getMainPanel() {
        return mainPanel;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public void showURL(URI uri) {
        getCollapseWindowAction().doCollapse();
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private LoadDeskletAction loadDeskletAction = new LoadDeskletAction(this);
    public LoadDeskletAction getLoadDeskletAction() {
        return loadDeskletAction;
    }
    
    
    private Closer closer = new Closer(this);
    public Closer getCloser() {
        return closer;
    }
}
