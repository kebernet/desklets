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
    public static final String MICRODOCKING = "Main.Closer.Microdocking";
    public static final String DOCKINGSIDE = "Main.MainPanel.DockingSide";
    public static String TRACKINGENABLED = "Main.LoginToServerAction.TrackingEnabled";
    private Properties props = null;

    
    /** Creates a new instance of PrefsBean */
    public PrefsBean(Main main) {
    }
    public void loadFromPrefs() {
        u.p("loading from prefs");
        File propsFile = new File(Main.HOME_DIR,"preferences.properties");
        props = new Properties();
        try {
            props.load(new FileInputStream(propsFile));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    private void saveToPrefs() {
        u.p("saving to prefs");
        File propsFile = new File(Main.HOME_DIR,"preferences.properties");
        //u.p("writing to: " + propsFile);
        //u.dumpStack();
        try {
            props.store(new FileOutputStream(propsFile),
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
