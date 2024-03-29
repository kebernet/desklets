package org.glossitope.container.wm.buffered;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.RepaintManager;


class DeskletRepaintManager extends RepaintManager {
    BufferedWM wm;
    DeskletRepaintManager(BufferedWM wm) {
        this.wm = wm;
        //this.setDoubleBufferingEnabled(false);
    }
    
    public void addDirtyRegion(JComponent comp, int x, int y, int w, int h) {
        //u.p("adding as dirty: " + comp.getClass().getName() + " " + x + "," + y + " " + w + "x" + h);
        
        // find the top of this desklet's heirarchy so we can repaint the whole thing
        BufferedDeskletContainer bdc = findDesklet(comp);
        if(bdc != null) {
            // check that we don't add it twice
            if(comp != bdc.getTopComponent()) {
                super.addDirtyRegion(bdc.getTopComponent(), 0, 0, bdc.getTopComponent().getWidth(), bdc.getTopComponent().getHeight());
            }
            
            ((BufferedPeer)bdc.getPeer()).setDirty(true);
            super.addDirtyRegion((JComponent)wm.getRenderPanel(),
                    (int)bdc.getLocation().getX(), (int)bdc.getLocation().getY(),
                    (int)bdc.getSize().getWidth(), (int)bdc.getSize().getHeight());
        }
        
        super.addDirtyRegion(comp, x, y, w, h);
    }
    
    private BufferedDeskletContainer findDesklet(Component invalidComponent) {
        // if at the top most comp
        if(invalidComponent instanceof DeskletToplevel) {
            for(DeskletProxy proxy : wm.getProxies()) {
                if(foundDesklet(proxy.contentContainer,invalidComponent)) {
                    return proxy.contentContainer;
                }
                if(foundDesklet(proxy.configContainer,invalidComponent)) {
                    return proxy.configContainer;
                }
            }
            return null;
        }
        
        if(invalidComponent.getParent() == null) return null;
        
        return findDesklet(invalidComponent.getParent());
    }
    
    private boolean foundDesklet(BufferedDeskletContainer c, Component invalidComponent) {
        if(c.getPeer() instanceof BufferedPeer) {
            if(c.getTopComponent() == invalidComponent) {
                return true;
            }
        }
        return false;
    }
    
    
}