/*
 * DefaultContext.java
 *
 * Created on August 13, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.glossitope.container.security;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glossitope.container.Core;
import org.glossitope.desklet.DeskletContainer;
import org.glossitope.desklet.DeskletContext;


/**
 *
 * @author cooper
 */
public class DefaultContext extends DeskletContext {
    private static final Logger LOG = Logger.getLogger("org.glossitope");
    private BrowserHandler browserHandler;
    private DeskletContainer configDisplay;
    private DeskletContainer dialogDisplay;
    private DeskletConfig config;
    private DeskletContainer container;
    private DeskletContainer dockingDisplay;
    private File props;
    private Properties prefs;
    private boolean shutdownWhenIdle = true;
    private boolean stopped = false;
    public Map<Class,Object> services;

    private Core core;

    /** Creates a new instance of DefaultContext */
    public DefaultContext(Core core, DeskletConfig config) {
        this.core = core;
        this.config = config;
        services = new HashMap<Class, Object>();

        try {
            File metainf = new File(config.getHomeDir(), "META-INF");
            metainf.mkdirs();
            props = new File(metainf, "preferences.properties");
            prefs = new Properties();

            if(props.exists()) {
                FileInputStream fis = new FileInputStream(props);

                try {
                    prefs.load(fis);
                } finally {
                    fis.close();
                }
            }
        } catch(IOException ioe) {
            LOG.log(Level.WARNING, "Exception reading preferences.", ioe);
        }
    }

    public void closeRequest() {
        DeskletManager manager = DeskletManager.getInstance();
        manager.shutdownDesklet(config.getUUID());
    }

    void flushPreferences() throws IOException {
        Point2D pt = container.getLocation();
        prefs.setProperty(ContainerFactory.LOCATION_X, Integer.toString( (int) pt.getX() ) );
        prefs.setProperty(ContainerFactory.LOCATION_Y, Integer.toString( (int) pt.getY()) );
        FileOutputStream fos = new FileOutputStream(props);
        try {
            prefs.store(fos, null);
            fos.flush();
        } finally {
            fos.close();
        }
    }

    public DeskletConfig getConfig() {
        return this.config;
    }

    public DeskletContainer getConfigurationContainer() {
        this.configDisplay = (this.configDisplay == null) ? 
            ContainerFactory.getInstance().createConfigContainer(this)
            : this.configDisplay;
        return this.configDisplay;
    }

    public DeskletContainer getContainer() {
        this.container = (this.container == null)
        ? ContainerFactory.getInstance()
                                          .createInternalFrameContainer(this)
        : this.container;
       return this.container;
    }

    
    public DeskletContainer getDialog() {
        return null;
    }

    public DeskletContainer getDockingContainer() {
        return (this.dockingDisplay == null)
        ? (this.dockingDisplay = ContainerFactory.getInstance()
                                                 .createDockContainer(this))
        : this.dockingDisplay;
    }

    public String getPreference(String name, String defaultValue) {
        return prefs.getProperty(name, defaultValue);
    }

    boolean hasContainer() {
        return container != null;
    }

    boolean hasDock() {
        return this.dockingDisplay != null;
    }

    public boolean isShutdownWhenIdle() {
        return shutdownWhenIdle;
    }

    boolean isStopped() {
        return stopped;
    }

    public void notifyStopped() {
        this.setStopped(true);
    }

    void setBrowserHandler(BrowserHandler handler) {
        this.browserHandler = handler;
    }

    public String setPreference(String name, String value) {
        return (String) this.prefs.setProperty(name, value);
    }

    public void setShutdownWhenIdle(boolean shutdownWhenIdle) {
        this.shutdownWhenIdle = shutdownWhenIdle;
    }

    void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void showURL(URI uri) {
        this.browserHandler.showURL(uri);
    }

    public File getWorkingDirectory() {
        return config.getHomeDir();
    }
    
    public void setContainer(DeskletContainer container) {
        this.container = container;
    }

    public Object getService(Class serviceClass) {
        return services.get(serviceClass);
    }

    public boolean serviceAvailable(Class serviceClass) {
        return services.containsKey(serviceClass);
    }
}
