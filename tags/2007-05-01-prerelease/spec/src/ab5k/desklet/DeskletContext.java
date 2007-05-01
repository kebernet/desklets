/*
 * DeskletContext.java
 *
 * Created on August 13, 2006, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.desklet;

import java.awt.Container;
import java.io.File;
import java.net.URI;

/**
 *
 * @author cooper
 * @author joshua@marinacci.org
 */
public abstract class DeskletContext {
     
    
    /** the container that desklets should add their own components to*/
    public abstract DeskletContainer getContainer();
    
    /** the container that desklets should put their docking versions in */
    public abstract  DeskletContainer getDockingContainer();
    
    
    /** The container that desklets should put their setup screen in.
     returns the same container each time.*/
    public abstract  DeskletContainer getConfigurationContainer();
    
    /** Get a new dialog container. Returns a new container each time. */
    public abstract  DeskletContainer getDialog();
    
    public abstract  String setPreference( String name, String value );
    
    public abstract  String getPreference( String name, String defaultValue );
    
    // lifecycle support
    public abstract  void notifyStopped();
    public abstract  void setShutdownWhenIdle(boolean shutdownWhenIdle);
    public abstract  void closeRequest();
    
    // external stuff support. should this move to a service?
    public abstract  void showURL(URI uri);
    public abstract  File getWorkingDirectory();
    
    // service support
    public abstract Object getService(Class serviceClass);
    public abstract boolean serviceAvailable(Class serviceClass);
}
