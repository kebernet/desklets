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
 */
public interface DeskletContext {
     
    
    /** the container that desklets should add their own components to*/
    public DeskletContainer getContainer();
    
    /** the container that desklets should put their docking versions in */
    public DeskletContainer getDockingContainer();
    
    
    public Container getConfigurationContainer();
    
    public Container getDialog();
    
    public String setPreference( String name, String value );
    
    public String getPreference( String name, String defaultValue );
    
    public void closeRequest();
    
    public void notifyStopped();
    
    public void showURL(URI uri);
    
    public void setShutdownWhenIdle(boolean shutdownWhenIdle);
    
    public File getWorkingDirectory();
}
