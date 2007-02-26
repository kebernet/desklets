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
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.CompoundPainter;

/**
 *
 * @author joshy
 */
public class EyeballDesklet extends AbstractDesklet {
    JXPanel panel;
    /** Creates a new instance of EyeballDesklet */
    public EyeballDesklet() {
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        panel = new JXPanel();
        panel.setPreferredSize(new Dimension(300,300));
        panel.setBackgroundPainter(new CompoundPainter(
                new CompoundPainter(true, new EyeballPainter()),
                new PupilPainter()));
        //context.getContainer().setLayout(new BorderLayout());
        context.getContainer().setContent(panel);
        context.getContainer().setVisible(true);
    }
    
    private boolean go = false;
    public void start() throws Exception {
        new Thread(new Runnable() {
            public void run() {
                go = true;
                Point pt = MouseInfo.getPointerInfo().getLocation();
                while(go) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Point pt2 = MouseInfo.getPointerInfo().getLocation();
                    if(!pt.equals(pt2)) {
                        panel.repaint();
                    }
                    pt = pt2;
                }
                context.notifyStopped();
            }
        }).start();
    }
    
    public void stop() throws Exception {
        go = false;
    }
    
    public void destroy() throws Exception {
    }


    
}
