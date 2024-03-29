/*
 * LoadDeskletAction.java
 *
 * Created on February 20, 2007, 8:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.actions;

import org.glossitope.container.Core;
import org.glossitope.container.security.DeskletConfig;
import org.glossitope.container.security.DeskletManager;
import org.glossitope.container.security.LifeCycleException;
import org.glossitope.container.security.Registry;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jdom.JDOMException;

/**
 *
 * @author joshy
 */
public class LoadDeskletAction {
    Core main;
    /** Creates a new instance of LoadDeskletAction */
    public LoadDeskletAction(Core main) {
        this.main = main;
    }
    
    public void load(URL url) throws MalformedURLException, IOException, JDOMException, LifeCycleException {
        DeskletManager manager = DeskletManager.getInstance();
        Registry r = Registry.getInstance();
        DeskletConfig config = r.installDesklet(url);
        manager.startDesklet( config.getUUID() );
        
    }
    
}
