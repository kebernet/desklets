package ab5k.desklet.test;

import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import java.awt.Container;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;

class TestDeskletContext implements DeskletContext {
    
    private DeskletContainer frame;
    private DeskletContainer dockingFrame;
    private Map<String, String> prefs;
    
    public TestDeskletContext(JFrame frame, JFrame dockingFrame) {
        super();
        prefs = new HashMap<String,String>();
        this.frame = new TestContainer(frame);
        this.dockingFrame = new TestContainer( dockingFrame );
    }
    
    public void closeRequest() {
    }
    
    public Container getConfigurationContainer() {
        return null;
    }
    
    public DeskletContainer getContainer() {
        return frame;
    }
    
    public Container getDialog() {
        return null;
    }
    
    public String getPreference(String name, String defaultValue) {
        if(prefs.containsKey(name)) {
            return prefs.get(name);
        }
        return defaultValue;
    }
    
    public void notifyStopped() {
    }
    
    public String setPreference(String name, String value) {
        prefs.put(name,value);
        return value;
    }
    
    public void showURL(URI uri) {
    }
    
    
    public DeskletContainer getDockingContainer() {
        return dockingFrame;
    }
    
    public void setShutdownWhenIdle(boolean shutdownWhenIdle) {
    }
    
    public File getWorkingDirectory() {
        // TODO
        return null;
    }    
    
    private static class TestContainer implements DeskletContainer {
        
        JFrame frame;
        
        TestContainer( JFrame frame ){
            this.frame = frame;
        }
        
        public void setContent(JComponent component) {
            frame.add( component );
        }
        
        public void setSize(Dimension2D size) {
        }
        
        public void setVisible(boolean visible) {
        }
        
        public void setShaped(boolean shaped) {
        }
        
        public void setResizable(boolean resizable) {
        }
        
        public void setBackgroundDraggable(boolean backgroundDraggable) {
        }
        
        public Dimension2D getSize() {
            return frame.size();
        }
        
    }
}