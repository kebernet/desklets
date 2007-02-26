/*
 * AbstractDesklet.java
 *
 * Created on August 4, 2006, 7:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.desklet;

import java.awt.Container;
import java.net.URI;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author cooper
 */
public abstract class AbstractDesklet implements Desklet {
    
    protected DeskletContext context;
    
    /** Creates a new instance of AbstractDesklet */
    public AbstractDesklet() {
    }
    
    
    
    /*private static class RunnableContext implements DeskletContext {
        
        private JPanel container = new JPanel();
        private JPanel config = new JPanel();
        private JDialog dialog = new JDialog();
        private JPanel dockingContainer = new JPanel();
        private Preferences prefs;
        
        public RunnableContext( Class clazz ){
             prefs = Preferences.userNodeForPackage( clazz );
        }
        
        public String setPreference(String name, String value) {
            String ret = (String) prefs.get( name, null );
            prefs.put( name, value );
            try{
            prefs.flush();
            } catch(Exception e){
                e.printStackTrace();
            }
            return ret;
        }
        
        public String getPreference(String name, String defaultValue) {
            return prefs.get( name, defaultValue );
        }
        
        public Container getDialog() {
            return dialog;
        }
        
        public void notifyStopped(){
            ;
        }
        
        public DeskletContainer getContainer() {
            return container;
        }
        
        public Container getConfigurationContainer() {
            return config;
        }
        
        public void closeRequest() {
        }

        public void showURL(URI uri) {
        }

        public Container getTopLevel() {
            return container;
        }

        public Container getDockingContainer() {
            return dockingContainer;
        }

        public void setShutdownWhenIdle(boolean shutdownWhenIdle) {
        }
        
    }*/

}
