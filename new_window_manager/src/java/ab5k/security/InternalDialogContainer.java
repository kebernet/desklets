/*
 * InternalDialogContainer.java
 *
 * Created on March 27, 2007, 7:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.security;

import ab5k.desklet.DeskletContainer;
import ab5k.wm.WindowManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 *
 * @author joshy
 */
public class InternalDialogContainer extends DeskletContainer {

    public JInternalFrame dialog;
    private WindowManager wm;
    private JComponent content;

    private InternalFrameContainer parent;

    private boolean packed = false;
    
    /** Creates a new instance of InternalDialogContainer */
    public InternalDialogContainer(String title, WindowManager wm, InternalFrameContainer parent) {
        this.wm = wm;
        dialog = new JInternalFrame();
        this.parent = parent;
    }

    public void setContent(JComponent content) {
        if(this.content != null) {
            dialog.remove(this.content);
        }
        
        this.content = content;
        dialog.setLayout(new BorderLayout());
        dialog.add(this.content, "Center");
        dialog.pack();
    }

    public void setBackgroundDraggable(boolean backgroundDraggable) {
    }

    public void setShaped(boolean shaped) {
    }

    public void setShape(Shape shape) {
    }

    public void setResizable(boolean resizable) {
    }

    public void setVisible(boolean visible) {
        if(!packed) {
            pack();
            centerOverParent();
        }
        dialog.setVisible(visible);
    }

    public boolean isVisible() {
        return dialog.isVisible();
    }

    public void setSize(Dimension2D size) {
        dialog.setSize(new Dimension((int)size.getWidth(), (int)size.getHeight()));
    }

    public Dimension2D getSize() {
        return dialog.getSize();
    }

    public void setLocation(Point2D location) {
        dialog.setLocation(new Point((int)location.getX(), (int)location.getY()));
    }

    public Point2D getLocation() {
        return dialog.getLocation();
    }

    public void pack() {
        dialog.pack();
        packed = true;
    }

    private void centerOverParent() {
        double x = parent.getLocation().getX() + parent.getSize().getWidth()/2 - getSize().getWidth()/2;
        double y = parent.getLocation().getY() + parent.getSize().getHeight()/2 - getSize().getHeight()/2;
        setLocation(new Point2D.Double(x,y));
    }
    
}
