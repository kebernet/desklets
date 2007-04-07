/*
 * Main.java
 *
 * Created on March 11, 2007, 12:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k;

import ab5k.actions.MacSupport;
import ab5k.actions.ShowSplashscreen;
import ab5k.actions.SingleLaunchSupport;
import ab5k.security.Registry;
import ab5k.security.SecurityPolicy;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.security.Policy;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdesktop.swingx.StackLayout;
import org.joshy.util.u;

/**
 *
 * @author joshua@marinacci.org
 */
public abstract class Main {
    private Main() { }
    
    // indicates if this is the first time AB5k has been run
    // currently based on the presence of the ~/.ab5k dir
    private static boolean firstRun = false;
    
    /** This is the startup main method for all of AB5k.
     * It ensures that everything is intializes in the correct
     * order.
     */
    public static void main(final String ... args) {
        
        // do all of the pre-startup tasks
        setupLookAndFeel();
        setupFirstRunFlag();
        
        // create the core
        final Core core = new Core();
        setupMacRelaunch(core, args);
        
        //todo: turn off dynamic layout. why do we do this???
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        setupSecurityManager();
        setupRegistry(core);
        
        u.p("main(args)");
        u.p(args);
        
        // do the real startup on the EDT
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                doStartup(args, core);
            }
        });
        
    }
    // do the rest of startup on the EDT
    private static void doStartup(final String args[], final Core main) {
        try {
            new ShowSplashscreen().show();
            setupMainFrame(main);
            doServerLogin(main);
            startCommandlineDesklets(main, args);
        } catch (Exception ex) {
            u.p(ex);
        }
    }
    
    // install and start any desklets specified on the commandline.
    // this is used when the user double clicks on a desklet file.
    private static void startCommandlineDesklets(final Core main, final String args[]) {
        for(String file : args) {
            try {
                main.getLoadDeskletAction().load(new File(file).toURI().toURL());
            } catch (Throwable th) {
                u.p(th);
            }
        }
    }
    
    // log into the server. We don't care if this fails for any reason
    // do on another thread so we don't block
    private static void doServerLogin(final Core main) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    main.getLoginToServerAction().loginToServer();
                } catch (Throwable thr) {
                    u.p(thr);
                }
            }
        }).start();
    }

    
    // the main panel, attach it to Core, then do the last config of the frame
    private static void setupMainFrame(final Core core) throws HeadlessException, SecurityException {
        MainPanel panel = new MainPanel(core);
        core.mainPanel = panel;
        core.init();
        core.getWindowManager().setDockComponent(panel);
        
        JFrame frame = core.getFrame();
        //frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                core.getQuitAction().actionPerformed(new ActionEvent(this,-1,"quit"));
            }
        });
        frame.setAlwaysOnTop(true);
        frame.pack();
        
        frame.setBounds(core.getCollapseWindowAction().getStartupPosition());
        core.getCloser().setWindowClosed(true);
        frame.setVisible(true);
    }
    
    // force the creation of the registry and attach to core
    private static void setupRegistry(final Core main) {
        Registry reg = Registry.getInstance();
        reg.setMain(main);
    }
    
    // install the security manager
    private static void setupSecurityManager() {
        Policy.setPolicy( new SecurityPolicy() );
        System.setSecurityManager( new SecurityManager() );
    }
    
    private static void setupMacRelaunch(final Core main, final String args[]) {
        // do relaunching if this is a second instance, include commandline args
        if(SingleLaunchSupport.setupSingleLaunchSupport(main,args)){
            u.p("already running. sent the args and skipping out early");
            try {
                MacSupport.setupMacSupport(main);
            } catch (NoClassDefFoundError ex) {
                // if not on a mac then no class def may be thrown
                u.p("mac support not found. must not be mac. just bailing");
                System.exit(0);
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }
    }
    
    private static void setupFirstRunFlag() {
        // check the existence of the .ab5k dir first
        firstRun = Environment.wasHomeDirCreated();
        u.p("is first run = " + Main.isFirstRun());
    }
    
    private static void setupLookAndFeel() {
        // set up the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean isFirstRun() {
        return firstRun;
    }
}
