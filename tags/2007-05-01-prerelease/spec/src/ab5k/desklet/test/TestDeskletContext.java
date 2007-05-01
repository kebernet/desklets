package ab5k.desklet.test;

import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

class TestDeskletContext extends DeskletContext {
    
    private DeskletContainer frame;
    private DeskletContainer dockingFrame;
    private DeskletContainer config;
    private Map<String, String> prefs;
    
    public TestDeskletContext(JFrame frame, JFrame dockingFrame) {
        super();
        prefs = new HashMap<String,String>();
        this.frame = new TestContainer(frame);
        this.dockingFrame = new TestContainer( dockingFrame );
        this.config = new TestContainer(new JFrame("Configuration"));
    }
    
    public void closeRequest() {
    }
    
    public DeskletContainer getConfigurationContainer() {
        return config;
    }
    
    public DeskletContainer getContainer() {
        return frame;
    }
    
    public DeskletContainer getDialog() {
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

    // the test env doesn't support any services (yet)
    public Object getService(Class serviceClass) {
        return null;
    }

    public boolean serviceAvailable(Class serviceClass) {
        return false;
    }
    
    private static class TestContainer extends DeskletContainer {
        
        JFrame frame;
        private JComponent content;
        
        TestContainer( JFrame frame ){
            this.frame = frame;
        }
        
        public void setContent(JComponent component) {
            this.content = component;
            frame.getContentPane().removeAll();
            frame.getContentPane().add(content);
        }
        
        public void setSize(Dimension2D size) {
            frame.setSize(new Dimension((int)size.getWidth(),(int)size.getHeight()));
        }
        
        public void setVisible(boolean visible) {
            if(visible && !packed) {
                pack();
            }
            frame.setVisible(visible);
        }
        
        public void setShaped(boolean shaped) {
        }
        
        public void setResizable(boolean resizable) {
            frame.setResizable(false);
        }
        
        public void setBackgroundDraggable(boolean backgroundDraggable) {
        }
        
        public Dimension2D getSize() {
            return frame.size();
        }
        
        public void setShape(Shape shape) {
        }
        
        public void setLocation(Point2D location) {
            frame.setLocation((Point) location);
        }

        public void pack() {
            frame.pack();
            packed = true;
        }

        public boolean isVisible() {
            return frame.isVisible();
        }

        public Point2D getLocation() {
            return frame.getLocation();
        }

        public JComponent getContent() {
            return content;
        }

        public boolean isBackgroundDraggable() {
            return false;
        }

        public boolean isShaped() {
            return false;
        }

        public Shape getShape() {
            return null;
        }

        public boolean isResizable() {
            if(frame instanceof JFrame) {
                return ((JFrame)frame).isResizable();
            }
            return false;
        }
        private boolean packed;
        
    }
}
