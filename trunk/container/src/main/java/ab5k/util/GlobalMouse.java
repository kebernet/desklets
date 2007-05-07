/*
 * GlobalMouse.java
 *
 * Created on March 30, 2007, 2:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.util;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author joshy
 */
public class GlobalMouse extends ab5k.desklet.services.GlobalMouse{
    
    private static GlobalMouse globalMouse;
    
    
    public static GlobalMouse getInstance() {
        if(globalMouse == null) {
            globalMouse = new GlobalMouse();
        }
        return globalMouse;
    }
    
    /** Creates a new instance of GlobalMouse */
    protected GlobalMouse() {
        listeners = new HashMap<JComponent, Set<MouseInputListener>>();
        startThread();
    }
    
    private Map<JComponent, Set<MouseInputListener>> listeners;
    private Thread thread;
    
    public void addMouseListener(MouseInputListener ml, JComponent comp) {
        if(!listeners.containsKey(comp)) {
            listeners.put(comp, new HashSet<MouseInputListener>());
        }
        listeners.get(comp).add(ml);
    }
    
    public void removeMouseListener(MouseInputListener ml, JComponent comp) {
        if(!listeners.containsKey(comp)) {
            listeners.get(comp).remove(ml);
        }
    }
    
    private void fireMouseListeners(Point pt) {
        long now = new Date().getTime();
        for(JComponent comp : listeners.keySet()) {
            Point localPoint = convertPointToComponent(comp,pt);
            MouseEvent evt = new MouseEvent(comp,-1,now,0,localPoint.x, localPoint.y, pt.x, pt.y,
                    0, false, 0);
            for(MouseInputListener l : listeners.get(comp)) {
                l.mouseMoved(evt);
            }
        }
    }
    
    private boolean go = false;
    private void startThread() {
        thread = new Thread(new Runnable() {
            public void run() {
                Point pt = null;//MouseInfo.getPointerInfo().getLocation();
                while(go) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        
                        Point pt2 = MouseInfo.getPointerInfo().getLocation();
                        if(!pt2.equals(pt)) {
                            fireMouseListeners(pt2);
                        }
                        pt = pt2;
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        /*
                        try {
                            Point pt2 = com.apple.eawt.Application.getMouseLocationOnScreen();
                            if(!pt2.equals(pt)) {
                                fireMouseListeners(pt2);
                            }
                            pt = pt2;
                        } catch (Throwable ex2) {
                            ex2.printStackTrace();
                        }*/
                    }
                }
            }
        },"GlobalMouse");
        go = true;
        thread.start();
    }
    
    protected Point convertPointToComponent(JComponent comp, Point pt) {
        Point pt2 = new Point(pt);
        SwingUtilities.convertPointFromScreen(pt2,comp);
        return pt2;
    }
    
    
}
