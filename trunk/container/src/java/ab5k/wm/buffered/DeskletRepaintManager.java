package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.wm.buffered.DeskletToplevel;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import org.joshy.util.u;


class DeskletRepaintManager extends RepaintManager {
    BufferedWM wm;
    DeskletRepaintManager(BufferedWM wm) {
        this.wm = wm;
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
            
            ((Buffered2DPeer)bdc.getPeer()).setDirty(true);
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
                if(proxy.contentContainer.getPeer() instanceof Buffered2DPeer) {
                    if(proxy.contentContainer.getTopComponent() == invalidComponent) {
                        return proxy.contentContainer;
                    }
                }
            }
            return null;
        }
        
        if(invalidComponent.getParent() == null) return null;
        
        return findDesklet(invalidComponent.getParent());
    }
    
}