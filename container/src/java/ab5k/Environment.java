/*
 * Environment.java
 *
 * Created on March 28, 2007, 6:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k;

import java.io.File;
import javax.swing.JOptionPane;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public final class Environment {
    // set justCreated before all other initialization might modify it.
    private static boolean justCreated = false;
    public static final File HOME = getUserPreferredHome();
    public static final File REPO = new File(HOME, "repository");
    private static final String AB5K_HOME = ".ab5k";
    
    private static String AB5K_HOME_ENVNAME = "AB5K_HOME";
    private static File getUserPreferredHome() {
        
        // maybe user have already setup their own AB5K_HOME
        File f = new File("" + System.getenv("AB5K_HOME"), AB5K_HOME); // "" - just in case getenv return null
        u.p("app home = " + System.getenv("AB5K_HOME"));
        if (System.getenv("AB5K_HOME") == null || createAppHome(f)) {
            // maybe default home dir is fine
            f = new File(System.getProperty("user.home"), AB5K_HOME);
            u.p("user.home = " + System.getProperty("user.home"));
            u.p("file = " + f + " " + f.getAbsolutePath());
            if (createAppHome(f)) {
                u.p("checking app data now");
                // failed to mkdir in user home - maybe windows with network profile
                f = new File("" + System.getenv("APPDATA"), AB5K_HOME); // "" - just in case getenv return null
                if (System.getenv("APPDATA") == null || createAppHome(f)) {
                    // now we are in real bind ... go and ask user
                    String homeAgain = JOptionPane
                            .showInputDialog(
                                    null,
                                    "There seems to be problem while creating configuration on your system.\n"
                                            + " It may be that your home directory is write protected \nor your system is not setup "
                                            + "in a way understood by ab5k. \nWould you like to specify home directory manualy?\n"
                                            + " (We might need to ask for its location on every start, \n"
                                            + "or you might just set up AB5K_HOME system property pointing to such directory).",
                                    "Home directory setup", JOptionPane.OK_CANCEL_OPTION);
                    f = new File(homeAgain, AB5K_HOME);
                    justCreated = f.mkdirs();
                    if (!justCreated && (!f.exists() || !f.isDirectory())) {
                        // can't create - ignore or ask again, that is the question ...
                    }
                }
            }
        }
        return f;
    }
    
    private static boolean createAppHome(File f) {
        justCreated = f.mkdirs();
        return !justCreated && (!f.exists() || !f.isDirectory());
    }
    
    public static boolean wasHomeDirCreated() {
        return HOME.exists() && HOME.isDirectory() && justCreated ;
    }
    
    /** Creates a new instance of Environment */
    private Environment() {    }
    
}
