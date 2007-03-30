package ab5k.desklet.test;

import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;

class TestDeskletContext extends DeskletContext {
    
    private DeskletContainer frame;
    private JFrame jframe;
    private DeskletContainer dockingFrame;
    private DeskletContainer configFrame;
    private Map<String, String> prefs;
    
    public TestDeskletContext(JFrame frame, JFrame dockingFrame) {
        super();
        prefs = new HashMap<String,String>();
        this.frame = new TestContainer(frame);
        this.jframe = frame;
        this.dockingFrame = new TestContainer( dockingFrame );
    }
    
    public void closeRequest() {
    }
    
    public DeskletContainer getConfigurationContainer() {
        if(configFrame == null) {
            JDialog dialog = new JDialog(jframe);
            this.configFrame = new TestContainer(dialog);
        }
        return configFrame;
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
    
    private static class TestContainer extends DeskletContainer {
        
        RootPaneContainer frame;
        Window window;
        boolean packed = false;
        private JComponent content;
        
        TestContainer( RootPaneContainer frame ){
            this.frame = frame;
            this.window = (Window)frame;
        }
        
        public void setContent(JComponent component) {
            this.content = component;
            frame.getContentPane().removeAll();
            frame.getContentPane().add(content);
        }
        
        public void setSize(Dimension2D size) {
            window.setSize(new Dimension((int)size.getWidth(),(int)size.getHeight()));
        }
        
        public void setVisible(boolean visible) {
            if(!packed) {
                window.pack();
                packed = true;
            }
            window.setVisible(visible);
        }
        
        public void setShaped(boolean shaped) {
        }
        
        public void setResizable(boolean resizable) {
            if(frame instanceof JFrame) {
                ((JFrame)frame).setResizable(false);
            }
        }
        
        public void setBackgroundDraggable(boolean backgroundDraggable) {
        }
        
        public Dimension2D getSize() {
            return window.getSize();
        }
        
        public void setShape(Shape shape) {
        }
        
        public void setLocation(Point2D location) {
            window.setLocation((Point) location);
        }

        public void pack() {
            window.pack();
            packed = true;
        }

        public boolean isVisible() {
            return window.isVisible();
        }

        public Point2D getLocation() {
            return window.getLocation();
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
        
    }
}