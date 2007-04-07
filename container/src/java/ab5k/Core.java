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
import ab5k.backgrounds.GradientBackground;
import ab5k.backgrounds.RadarSweep;
import ab5k.backgrounds.TronWorld;
import ab5k.prefs.PrefsBean;
import ab5k.security.ContainerFactory;
import ab5k.security.DeskletManager;
import ab5k.security.LifeCycleException;
import ab5k.util.PlafUtil;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class Core {
    
    //unused? delete? public Map iframes = new HashMap();
    private DeskletManager deskletManager;
    private ContainerFactory containerFactory;
    private BackgroundManager backgroundManager;
    public PreferencesPanel prefs;
    
    public MainPanel mainPanel;
    
    public JFrame frame;
    
    private PrefsBean prefsBean;
    
    /** Creates a new instance of Main */
    public Core() {
        this.collapseWindowAction = new CollapseWindowAction(this);
    }
    
    
    void init() {
        setBackgroundManager(new BackgroundManager(this));
        setupBackgrounds();
        setupMacSupport();
        
        containerFactory = ContainerFactory.getInstance();
        containerFactory.init( getDesktop(), mainPanel.getDockPanel() );
        deskletManager = DeskletManager.getInstance();
        
        try{
            deskletManager.startUp( this );
        } catch(LifeCycleException e){
            e.printStackTrace();
        }
        setupMoreMacSupport();
        setupPrefs();
        preinstallStandardDesklets();
    }
    
    private void preinstallStandardDesklets() {
        if(Main.isFirstRun()) {
            String[] options = { "Yes", "No" };
            int value = JOptionPane.showOptionDialog(this.mainPanel, "This seems to be your first run of AB5k. Would you like to install some default desklets from the web?",
                    "First Run", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            switch(value){
                case 0:
                    
                    new Thread(new Runnable() {
                        public void run() {
                            u.p("this is the first run so we must auto-install the standard desklets");
                            preinstall("http://www.ab5k.org/downloads/daily/pre-install/WeatherDesklet.desklet");
                            preinstall("http://www.ab5k.org/downloads/daily/pre-install/ROMEDesket.desklet");
                            preinstall("http://www.ab5k.org/downloads/daily/pre-install/ColorChooserDesklet.desklet");
                            preinstall("http://www.ab5k.org/downloads/daily/pre-install/ClockDesklet.desklet");
                            preinstall("http://www.ab5k.org/downloads/daily/pre-install/Calendar.desklet");
                            preinstall("http://www.ab5k.org/downloads/daily/pre-install/Eyeball.desklet");
                        }
                    }).start();
            }
        }
    }
    
    private void setupPrefs() {
        
        prefsBean = new PrefsBean(this);
        getPrefsBean().loadFromPrefs();
        u.p("done loading prefs");
        mainPanel.setDockingSide(MainPanel.DockingSide.valueOf(
                getPrefsBean().getString(PrefsBean.DOCKINGSIDE,
                MainPanel.DockingSide.Right.toString())));
        getCloser().setMicrodocking(getPrefsBean().getBoolean(PrefsBean.MICRODOCKING,false));
        getLoginToServerAction().setShouldLogin(getPrefsBean().getBoolean(PrefsBean.TRACKINGENABLED,true));
        u.p("done with setup");
    }
    
    // todo:  do we still need to do this?
    private void setupMoreMacSupport() {
        
        if(PlafUtil.isMacOSX()) {
            quitAction.putValue(Action.NAME,"Quit");
            preferencesAction.putValue(Action.NAME,"Preferences");
        } else {
            quitAction.putValue(Action.NAME,"Exit");
            preferencesAction.putValue(Action.NAME,"Options");
        }
    }
    
    private void setupMacSupport() {
        
        try {
            MacSupport.setupMacSupport(this);
        } catch (Throwable thr) {
            u.p("setting up mac support failed");
            thr.printStackTrace();
        }
    }
    
    private void preinstall(String url) {
        try {
            u.p("preinstalling: " + url);
            getLoadDeskletAction().load(new URL(url));
        } catch (Exception ex) {
            u.p(ex);
        }
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
    
    public void handleError(final String title, final String message, final Throwable thr) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ErrorInfo info = new ErrorInfo(title, message, null, "???", thr, Level.ALL, null);
                JXErrorPane.showDialog(getFrame(),info);
            }
        });
        u.p("prob: " + title + " " + message);
        u.p(thr);
    }
    
    /* ======== get accessors ================ */
    
    public DeskletManager getDeskletManager() {
        return deskletManager;
    }
    
    public JDesktopPane getDesktop() {
        return getMainPanel().desktop;
    }
    
    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }
    
    private void setBackgroundManager(BackgroundManager backgroundManager) {
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
    
    
    public MainPanel getMainPanel() {
        return mainPanel;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    
    private LoadDeskletAction loadDeskletAction = new LoadDeskletAction(this);
    public LoadDeskletAction getLoadDeskletAction() {
        return loadDeskletAction;
    }
    
    
    private Closer closer = new Closer(this);
    public Closer getCloser() {
        return closer;
    }
    
    public PrefsBean getPrefsBean() {
        return prefsBean;
    }
    
    public void showURL(URI uri) {
        getCollapseWindowAction().doCollapse();
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}