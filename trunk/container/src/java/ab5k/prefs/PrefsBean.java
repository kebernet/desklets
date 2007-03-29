/*
 * PrefsBean.java
 *
 * Created on February 28, 2007, 7:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.prefs;

import ab5k.*;
import ab5k.security.Registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class PrefsBean {
    private static final File PREFS_FILE = new File(Environment.HOME,"preferences.properties");
        
    public static final String MICRODOCKING = "Main.Closer.Microdocking";
    public static final String DOCKINGSIDE = "Main.MainPanel.DockingSide";
    public static String TRACKINGENABLED = "Main.LoginToServerAction.TrackingEnabled";
    static{
        ConfigurationImportExport.registerExport("global", PREFS_FILE);
    }
    private Properties props = null;

    
    /** Creates a new instance of PrefsBean */
    public PrefsBean(Core main) {
    }
    
    public void loadFromPrefs() {
        u.p("loading from prefs");
        props = new Properties();
        try {
            props.load(new FileInputStream(PREFS_FILE));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveToPrefs() {
        u.p("saving to prefs");
        //u.p("writing to: " + propsFile);
        //u.dumpStack();
        try {
            props.store(new FileOutputStream(PREFS_FILE),
                    "stores settings from the  Preferences dialog");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //u.p("finished writing");
    }
    
    public boolean getBoolean(String prop, boolean fallback) {
        return Boolean.parseBoolean(props.getProperty(prop,""+fallback));
    }

    public String getString(String prop, String fallback) {
        return props.getProperty(prop, fallback);
    }

    public void setProperty(String prop, String value) {
        props.setProperty(prop,value);
        saveToPrefs();
    }

    public void setProperty(String prop, boolean value) {
        props.setProperty(prop, ""+value);
        saveToPrefs();
    }
    
}
