package org.glossitope.container.wm.buffered;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.joshy.util.u;


class MouseRedispatcher implements MouseListener, MouseMotionListener {
    public MouseRedispatcher(BufferedWM bufferedWM) {
        this.bufferedWM = bufferedWM;
    }

    private final BufferedWM bufferedWM;

    Component lastComp;
    private BufferedDeskletContainer lastDC;
    

    
    public void mouseClicked(MouseEvent e) {
        redispatch(e);
    }
    
    public void mouseEntered(MouseEvent e) {
        redispatch(e);
    }
    
    public void mouseExited(MouseEvent e) {
        redispatch(e);
    }
    
    public void mousePressed(MouseEvent e) {
        bufferedWM.raiseWindow(e);
        redispatch(e);
        bufferedWM.getRenderPanel().requestFocus();
    }
    
    public void mouseReleased(MouseEvent e) {
        redispatch(e);
        lastComp = null;
        lastDC = null;
    }
    
    public void mouseDragged(MouseEvent e) {
        redispatchDragged(e);
    }
    
    public void mouseMoved(MouseEvent e) {
        redispatch(e);
    }
    
    
    void redispatch(MouseEvent e) {
        if(!bufferedWM.getProxies().isEmpty()) {
            BufferedDeskletContainer bdc = bufferedWM.findContainer(e.getPoint());
            if(bdc != null) {
                if(e.getID() == e.MOUSE_PRESSED) {
                    u.p("redispatching to: " + bdc.toString() + " " + bdc.getContext().toString());
                }
                lastDC = bdc;
                redispatch(e, bdc);
            }
        }
    }

    // redispatches drag events to the desklet being dragged rather 
    // than to the desklet currently under the mouse
    private void redispatchDragged(MouseEvent e) {
        if(bufferedWM.getProxies().isEmpty()) {
            redispatch(e);
            return;
        }
        
        if(lastDC != null) {
            redispatch(e,lastDC);
        }
    }
    
    private void redispatch(final MouseEvent e, final BufferedDeskletContainer bdc) {
        Point2D pt = bdc.getLocation();
        Point ept = e.getPoint();
        ept.translate((int)-pt.getX(),(int)-pt.getY());
        //e.translatePoint(-pt.x,-pt.y);
        JComponent comp = bdc.getTopComponent();
        comp.setSize(new Dimension((int)bdc.getSize().getWidth(),
                (int)bdc.getSize().getHeight()));
        comp.setLocation(0,0);
        redispatchToLowest(comp,e,ept);
    }
    
    private void showVis(Container c, String tab) {
        if(c == null) {
            u.p(tab + " null");
            return;
        }
        u.p(tab + " " + c.hashCode() + " " + c);
        //u.p(tab + " vis = " + c.isVisible());
        //u.p(tab + " bounds = " + c.getBounds());
        for(Component cc : c.getComponents()) {
            showVis((Container)cc,tab+"  ");
        }
    }
    private void redispatchToLowest(JComponent comp, MouseEvent e, Point ept) {
        
        // translate into the top component space
        //e.translatePoint(comp.getX(),comp.getY());
        ept.translate(comp.getX(), comp.getY());
        /*
        if(e.getID() == e.MOUSE_PRESSED) {
            u.p("it's an action");
            u.p("comp = " + comp);
            u.p("e = " + e);
            u.p("point = " + e.getPoint());
            u.p("ept = " + ept);
            showVis(comp,"");
            
        }*/
        // find the deepest child to send this event to
        Component child = SwingUtilities.getDeepestComponentAt(comp,ept.x,ept.y);
        //Component child = getDeepestComponentAt(comp,ept.x,ept.y);
        if(e.getID() == e.MOUSE_DRAGGED) {
            child = lastComp;
        }
        Point pt2 = SwingUtilities.convertPoint(comp,ept,child);
        //u.p("pt2 = " + pt2);
        //u.p("final child = " + child);
        
        if(child != null) {
            // pass the mouse event back up the stack if this component doesn't
            // care about mouse events. w/o this we'd lose dragging
            
            // go back up the stack
            while(child.getMouseListeners() == null ||
                    child.getMouseListeners() == null ||
                    child.getMouseListeners().length == 0) {
                pt2.translate(child.getX(),child.getY());
                child = child.getParent();
                if(child == null) {
                    //u.p("fell off the end. this is an orphan!");
                    return;
                }
                //u.p("back up to parent " + child);
            }
            MouseEvent e2 = new MouseEvent(child,
                    e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    pt2.x,
                    pt2.y,
                    e.getXOnScreen(),
                    e.getYOnScreen(),
                    e.getClickCount(),
                    e.isPopupTrigger(),
                    e.getButton());
            //u.p("e2 = " + e2);
            lastComp = child;
            child.dispatchEvent(e2);
        }
        //child.dispatchEvent(e);
    }

    public static Component getDeepestComponentAt(Component parent, int x, int y) {
        if (!parent.contains(x, y)) {
            return null;
        }
        if (parent instanceof Container) {        
            Component components[] = ((Container)parent).getComponents();
            for (int i = 0 ; i < components.length ; i++) {
                Component comp = components[i];
                if (comp != null && comp.isVisible()) {
                    Point loc = comp.getLocation();
                    if (comp instanceof Container) {
                        comp = getDeepestComponentAt(comp, x - loc.x, y - loc.y);
                    } else {
                        comp = comp.getComponentAt(x - loc.x, y - loc.y);
                    }
                    if (comp != null && comp.isVisible()) {
                        return comp;
                    }
                }
            }
        }
        return parent;
    }
}