/*
 * InternalToExternalMouseHandler.java
 *
 * Created on April 7, 2007, 3:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.Core;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author joshy
 */
class InternalToExternalMouseHandler extends MouseAdapter {
    boolean wasDragging = false;
    
    private Core core;
    
    public InternalToExternalMouseHandler(Core core, BufferedWM wm) {
        super();
        this.core = core;
        this.wm = wm;
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseMoved(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
        wasDragging = false;
    }
    
    
    public void mouseDragged(MouseEvent e) {
        wasDragging = true;
        if (e.getPoint().getX() < 20) {
            if(!core.getCloser().isWindowClosed()) {
                core.getCollapseWindowAction().doCollapse();
                wm.convertInternalToExternalContainer(wm.selectedDesklet);
            }
        }
        
        if(core.getCloser().isWindowClosed() &&
                wm.selectedDesklet.getPeer() instanceof JFramePeer) {
            Point pt = e.getPoint();
            SwingUtilities.convertPointToScreen(pt,wm.getRenderPanel());
            wm.selectedDesklet.setLocation(new Point(pt.x - wm.selectedDeskletOffset.x,
                    pt.y - wm.selectedDeskletOffset.y));
            
        }
        
        if(wm.selectedDesklet != null && wm.selectedDesklet.getLocation().getX() < 0) {
            showDeskletInGlasspane();
        } else {
            hideDeskletInGlasspane();
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        hideDeskletInGlasspane();
        if (e.getPoint().getX() < 0 && wasDragging) {
        }
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    JPanel intExtGlasspane = new JPanel() {
        public void paintComponent(Graphics g) {
            //u.p("painting");
            if(wm.selectedDesklet != null) {
                if(wm.selectedDesklet instanceof BufferedDeskletContainer) {
                    BufferedDeskletContainer bdc = (BufferedDeskletContainer) wm.selectedDesklet;
                    
                    Point pt = wm.getRenderPanel().getLocation();
                    pt.translate((int)bdc.getLocation().getX(),
                            (int)bdc.getLocation().getY());
                    g.drawImage(((Buffered2DPeer)bdc.getPeer()).getBuffer(),
                            (int)pt.getX(),
                            (int)pt.getY(), null);
                }
            }
        }
    };
    
    private void showDeskletInGlasspane() {
        if(wm.frame.getGlassPane() != intExtGlasspane) {
            intExtGlasspane.setOpaque(false);
            wm.frame.setGlassPane(intExtGlasspane);
            intExtGlasspane.setVisible(true);
        }
        intExtGlasspane.setVisible(true);
        intExtGlasspane.repaint();
    }
    
    private void hideDeskletInGlasspane() {
        intExtGlasspane.setVisible(false);
    }
    private BufferedWM wm;
}
