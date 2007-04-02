/*
 * EyeballDesklet.java
 *
 * Created on February 10, 2007, 3:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eyeball;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.services.GlobalMouse;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.CompoundPainter;

/**
 *
 * @author joshy
 */
public class EyeballDesklet extends AbstractDesklet {
    private JXPanel panel;
    private PupilPainter pupil;
    
    /** Creates a new instance of EyeballDesklet */
    public EyeballDesklet() {
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        panel = new JXPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(300,300));
        pupil = new PupilPainter();
        
        
        CompoundPainter c1 = new CompoundPainter(new EyeballPainter());
        
        pupil.setCacheable(false);
        CompoundPainter c2 = new CompoundPainter(c1,pupil);
        c2.setCacheable(false);
        panel.setBackgroundPainter(c2);
        
        context.getContainer().setContent(panel);
        context.getContainer().setVisible(true);
    }
    
    public void start() throws Exception {
        if(context.serviceAvailable(GlobalMouse.class)) {
            GlobalMouse mouse = (GlobalMouse) context.getService(GlobalMouse.class);
            mouse.addMouseListener(new MouseInputListener() {
                public void mouseClicked(MouseEvent e) {
                }
                public void mouseDragged(MouseEvent e) {
                }
                public void mouseEntered(MouseEvent e) {
                }
                public void mouseExited(MouseEvent e) {
                }
                public void mouseMoved(MouseEvent e) {
                    pupil.setMouseLocation(e.getPoint());
                    panel.repaint();
                }
                public void mousePressed(MouseEvent e) {
                }
                public void mouseReleased(MouseEvent e) {
                }
            },panel);
        }
    }
    
    public void stop() throws Exception {
        context.notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
    
    
}
