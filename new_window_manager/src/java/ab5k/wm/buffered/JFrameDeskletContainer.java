/*
 * JFrameDeskletContainer.java
 *
 * Created on March 24, 2007, 1:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Dimension2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class JFrameDeskletContainer implements DeskletContainer {
    
    private BufferedWM wm;
    JFrame frame;
    
    private JComponent content;
    
    /** Creates a new instance of JFrameDeskletContainer */
    public JFrameDeskletContainer(final BufferedWM wm) {
        this.wm = wm;
        this.frame = new JFrame();
        this.frame.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
                u.p("frame moved! " + e);
                if(frame.getLocation().getX() >
                        wm.core.getCollapseWindowAction().getClosedBounds().getX()) {
                    u.p("once more into the dock!");
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            wm.convertExternalToInternalContainer(JFrameDeskletContainer.this);
                        }
                    });
                }
            }
            public void componentResized(ComponentEvent e) {
            }
            public void componentShown(ComponentEvent e) {
            }
        });
    }
    
    public void setContent(JComponent jComponent) {
        this.content = jComponent;
        frame.getContentPane().add(jComponent);
    }
    
    public void setBackgroundDraggable(boolean b) {
    }
    
    public void setShaped(boolean b) {
    }
    
    public void setResizable(boolean b) {
    }
    
    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
    
    public Dimension2D getSize() {
        return frame.getSize();
    }
    
    public void setSize(Dimension2D d) {
        frame.setSize(new Dimension((int)d.getWidth(), (int)d.getHeight()));
    }
    
    JComponent getContent() {
        return content;
    }
    
}
