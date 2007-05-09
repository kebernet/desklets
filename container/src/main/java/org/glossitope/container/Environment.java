/*
 * Environment.java
 *
 * Created on March 28, 2007, 6:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container;

import java.io.File;
import javax.swing.JOptionPane;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public final class Environment {
    // if autograntall is set to anything then make it true
    public static final boolean autoGrantAll = 
            "true".equalsIgnoreCase(System.getProperty("org.glossitope.test.autoGrantAll")) ?
                true : false;

    public static final boolean useOptionPanePermissions = 
            "true".equalsIgnoreCase(System.getProperty("org.glossitope.test.useOptionPanePermissions")) ?
                true : false;
            
    public static final boolean allowMultipleInstances = 
            "true".equalsIgnoreCase(System.getProperty("org.glossitope.test.allowMultipleInstances")) ?
                true : false;
    
    public static final boolean showFrameTitleBar = 
            "true".equalsIgnoreCase(System.getProperty("org.glossitope.test.showFrameTitleBar")) ?
                true : false;
    
    private static final String GLOSSITOPE_HOME = 
            System.getProperty("org.glossitope.test.althomedirname") == null ? ".org.glossitope" :
            System.getProperty("org.glossitope.test.althomedirname");
    private static boolean justCreated = false;
    public static final File HOME = getUserPreferredHome();
    public static final File REPO = new File(HOME, "repository");
    
    private static String GLOSSITOPE_HOME_ENVNAME = "org.glossitope_HOME";
    private static File getUserPreferredHome() {
        
        // maybe user have already setup their own org.glossitope_HOME
        File f = new File("" + System.getenv("GLOSSITOPE_HOME"), GLOSSITOPE_HOME); // "" - just in case getenv return null
        u.p("app home = " + System.getenv("GLOSSITOPE_HOME"));
        if (System.getenv("org.glossitope_HOME") == null || createAppHome(f)) {
            // maybe default home dir is fine
            f = new File(System.getProperty("user.home"), GLOSSITOPE_HOME);
            u.p("user.home = " + System.getProperty("user.home"));
            u.p("file = " + f.getAbsolutePath());
            if (createAppHome(f)) {
                u.p("checking app data now");
                // failed to mkdir in user home - maybe windows with network profile
                f = new File("" + System.getenv("APPDATA"), GLOSSITOPE_HOME); // "" - just in case getenv return null
                if (System.getenv("APPDATA") == null || createAppHome(f)) {
                    // now we are in real bind ... go and ask user
                    String homeAgain = JOptionPane
                            .showInputDialog(
                                    null,
                                    "There seems to be problem while creating configuration on your system.\n"
                                            + " It may be that your home directory is write protected \nor your system is not setup "
                                            + "in a way understood by org.glossitope. \nWould you like to specify home directory manualy?\n"
                                            + " (We might need to ask for its location on every start, \n"
                                            + "or you might just set up org.glossitope_HOME system property pointing to such directory).",
                                    "Home directory setup", JOptionPane.OK_CANCEL_OPTION);
                    f = new File(homeAgain, GLOSSITOPE_HOME);
                    justCreated = f.mkdirs();
                    if (!justCreated && (!f.exists() || !f.isDirectory())) {
                        // can't create - ignore or ask again, that is the question ...
                    }
                }
            }
        }
        return f;
    }
    
    // returns true if the creation failed and we should try other options
    private static boolean createAppHome(File f) {
        u.p("creating: " + f.getAbsolutePath());
        justCreated = f.mkdirs();
        u.p("Just created = " + justCreated);
        // if created then don't continue
        if(justCreated) return false;
        
        return !justCreated && (!f.exists() || !f.isDirectory());
    }
    
    public static boolean wasHomeDirCreated() {
        u.p("home exists = " + HOME.exists());
        u.p("is dir = " + HOME.isDirectory());
        u.p("jsut created = " + justCreated);
        return HOME.exists() && HOME.isDirectory() && justCreated ;
    }
    
    /** Creates a new instance of Environment */
    private Environment() {    }
    
}
