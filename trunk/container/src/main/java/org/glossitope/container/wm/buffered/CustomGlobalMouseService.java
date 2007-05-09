/*
 * CustomGlobalMouseService.java
 *
 * Created on April 7, 2007, 3:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.wm.buffered;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.glossitope.container.util.GlobalMouse;

/**
 *
 * @author joshy
 */

class CustomGlobalMouseService extends GlobalMouse {
    public CustomGlobalMouseService(BufferedWM wm) {
        this.wm = wm;
    }
    
    protected Point convertPointToComponent(JComponent comp, Point pt) {
        Component topParent = getTopParent(comp);
        
        // if this is a component in a buffered desklet container.
        if(topParent == wm.hidden || topParent == null || topParent == wm.hidden) {
            DeskletToplevel top = (DeskletToplevel)wm.getTopParent(comp,DeskletToplevel.class);
            BufferedDeskletContainer bdc = top.getContainer();
            Point pt2 = new Point(pt);
            SwingUtilities.convertPointFromScreen(pt2, wm.getRenderPanel());
            pt2.translate(-(int)bdc.getLocation().getX(), -(int)bdc.getLocation().getY());
            Point pt3 = convertPointFromParentToChild(top,comp, pt2);
            return pt3;
        } else {
            return super.convertPointToComponent(comp,pt);
        }
    }
    
    private Component getTopParent(Component comp) {
        return SwingUtilities.getWindowAncestor(comp);
    }
    
    
    // convert from the child point to the parent point. the child must be a real child of this parent
    private Point convertPointFromParentToChild(Component parent, Component child, Point pt) {
        if(parent == child) {
            return pt;
        }
        Point pt2 = new Point(pt);
        pt2.translate(child.getLocation().x,child.getLocation().y);
        return convertPointFromParentToChild(parent, child.getParent(), pt2);
    }
    private BufferedWM wm;
}
