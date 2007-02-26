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
    
    private DeskletContainer container;
    private Properties prefs;
    private Container rootDisplay;
    private Container dialogDisplay;
    private Container configDisplay;
    private DeskletContainer dockingDisplay;
    private DeskletConfig config;
    private File props;
    private boolean stopped = false;
    private boolean shutdownWhenIdle = true;
    
    /** Creates a new instance of DefaultContext */
    public DefaultContext(DeskletConfig config) {
        this.config = config;
        try{
            File metainf = new File( config.getHomeDir() , "META-INF" );
            metainf.mkdirs();
            props = new File( metainf, "preferences.properties" );
            prefs = new Properties();
            if( props.exists()){
                FileInputStream fis = new FileInputStream( props );
                try{
                    prefs.load( fis );
                } finally {
                    fis.close();
                }
            }
        } catch(IOException ioe){
            LOG.log( Level.WARNING, "Exception reading preferences.", ioe );
        }
    }
    
    public void closeRequest() {
        DeskletManager manager = DeskletManager.getInstance();
        manager.shutdownDesklet( config.getUUID() );
    }
    
    public String setPreference(String name, String value) {
        return (String) this.prefs.setProperty(name, value);
    }
    
    public String getPreference(String name, String defaultValue) {
        return prefs.getProperty( name, defaultValue );
    }
    
    public Container getDialog() {
        return this.dialogDisplay;
    }
    
   
    
    public Container getConfigurationContainer() {
        return this.configDisplay;
    }
    
    public void notifyStopped(){
        this.setStopped( true );
    }
    
    void flushPreferences() throws IOException{
        FileOutputStream fos = new FileOutputStream( props );
        try{
            prefs.store( fos, null );
            fos.flush();
        } finally {
            fos.close();
        }
    }
    
    DeskletConfig getConfig(){
        return this.config;
    }
    
    boolean isStopped() {
        return stopped;
    }
    
    void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
    
    private BrowserHandler browserHandler;
    void setBrowserHandler(BrowserHandler handler) {
        this.browserHandler = handler;
    }
    
    public void showURL(URI uri) {
        this.browserHandler.showURL(uri);
    }
    
    
    public boolean isShutdownWhenIdle() {
        return shutdownWhenIdle;
    }
    
    public void setShutdownWhenIdle(boolean shutdownWhenIdle) {
        this.shutdownWhenIdle = shutdownWhenIdle;
    }

    public DeskletContainer getDockingContainer() {
        return this.dockingDisplay == null ?
            this.dockingDisplay = ContainerFactory
                .getInstance()
                .createDockContainer( this )
                : this.dockingDisplay;
    }

    public DeskletContainer getContainer() {
        return this.container == null ?
            this.container = ContainerFactory
                .getInstance()
                .createInternalFrameContainer( this )
                : this.container;
    }
    
    boolean hasContainer(){
        return container != null;
    }
    
    boolean hasDock(){
        return this.dockingDisplay != null;
    }
}
