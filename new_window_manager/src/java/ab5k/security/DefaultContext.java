/*
 * DefaultContext.java
 *
 * Created on August 13, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ab5k.security;

import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;

import java.awt.Container;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URI;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author cooper
 */
public class DefaultContext implements DeskletContext {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private BrowserHandler browserHandler;
    private Container configDisplay;
    private Container dialogDisplay;
    private DeskletConfig config;
    private DeskletContainer container;
    private DeskletContainer dockingDisplay;
    private File props;
    private Properties prefs;
    private boolean shutdownWhenIdle = true;
    private boolean stopped = false;

    /** Creates a new instance of DefaultContext */
    public DefaultContext(DeskletConfig config) {
        this.config = config;

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
        FileOutputStream fos = new FileOutputStream(props);
        InternalFrameContainer ifc = (InternalFrameContainer) this.container;
        prefs.setProperty(ContainerFactory.LOCATION_X, Integer.toString( (int) ifc.iframe.getLocation().getX() ) );
        prefs.setProperty(ContainerFactory.LOCATION_Y, Integer.toString( (int) ifc.iframe.getLocation().getY()) );
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

    public Container getConfigurationContainer() {
        return this.configDisplay;
    }

    public DeskletContainer getContainer() {
        this.container = (this.container == null)
        ? ContainerFactory.getInstance()
                                          .createInternalFrameContainer(this)
        : this.container;
       return this.container;
    }

    public Container getDialog() {
        return this.dialogDisplay;
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
}
