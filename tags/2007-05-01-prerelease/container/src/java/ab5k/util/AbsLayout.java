package ab5k.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import org.joshy.util.u;



// this just lays out components using their preferred size. it does not



// this just lays out components using their preferred size. it does not
// move them around at all.
// it also leaves the parent at whatever size it was set at
public class AbsLayout implements LayoutManager {
    
    public void addLayoutComponent(String name, Component comp) {
        u.p("adding: " + name);
    }
    
    public void layoutContainer(Container parent) {
        int ncomponents = parent.getComponentCount();
        for (int i = 0; i < ncomponents; i++) {
            Component comp = parent.getComponent(i);
            int x = comp.getX();
            int y = comp.getY();
            Dimension size = comp.getPreferredSize();
            comp.setBounds(x,y,size.width,size.height);
        }
    }
    
    public Dimension minimumLayoutSize(Container parent) {
        return parent.getSize();
    }
    
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getSize();
    }
    
    public void removeLayoutComponent(Component comp) {
    }
}