package ab5k.wm;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;



// this just lays out components using their preferred size. it does not
// move them around at all.
// it also leaves the parent at whatever size it was set at
class AbsLayout implements LayoutManager {
    
    public void addLayoutComponent(String name, Component comp) {
    }
    
    public void layoutContainer(Container parent) {
        int ncomponents = parent.countComponents();
        for (int i = 0; i < ncomponents; i++) {
            Component comp = parent.getComponent(i);
            int x = comp.getX();
            int y = comp.getY();
            Dimension size = comp.getPreferredSize();
            comp.reshape(x,y,size.width,size.height);
        }
    }
    
    public Dimension minimumLayoutSize(Container parent) {
        return parent.size();
    }
    
    public Dimension preferredLayoutSize(Container parent) {
        return parent.size();
    }
    
    public void removeLayoutComponent(Component comp) {
    }
}