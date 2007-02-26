/*
 * LoadDeskletAction.java
 *
 * Created on February 20, 2007, 8:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.actions;

import ab5k.Main;
import ab5k.security.DeskletConfig;
import ab5k.security.DeskletManager;
import ab5k.security.LifeCycleException;
import ab5k.security.Registry;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jdom.JDOMException;

/**
 *
 * @author joshy
 */
public class LoadDeskletAction {
    Main main;
    /** Creates a new instance of LoadDeskletAction */
    public LoadDeskletAction(Main main) {
        this.main = main;
    }
    
    public void load(URL url) throws MalformedURLException, IOException, JDOMException, LifeCycleException {
        DeskletManager manager = DeskletManager.getInstance();
        Registry r = Registry.getInstance();
        DeskletConfig config = r.installDesklet(url);
        manager.startDesklet( config.getUUID() );
        
    }
    
}
