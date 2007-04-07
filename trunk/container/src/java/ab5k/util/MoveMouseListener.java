package ab5k.util;

import ab5k.desklet.DeskletContainer;
import ab5k.wm.WindowManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;
import org.joshy.util.u;

public class MoveMouseListener implements MouseListener, MouseMotionListener {
    //JComponent target;
    DeskletContainer dc;
    Point start_drag;
    Point start_loc;

    private WindowManager wm;

    public MoveMouseListener(DeskletContainer dc, WindowManager wm) {
        this.dc = dc;
        this.wm = wm;
        //this.target = target;
    }
/*
    public static JInternalFrame getFrame(Container target) {
        if(target instanceof JInternalFrame) {
            return (JInternalFrame)target;
        }
        return getFrame(target.getParent());
    }
*/
    Point getScreenLocation(MouseEvent e) {
        Point cursor = e.getPoint();
        Point2D target_location = dc.getLocation();
        return new Point(
            (int)(target_location.getX()+cursor.getX()),
            (int)(target_location.getY()+cursor.getY()));
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        this.start_drag = this.getScreenLocation(e);
        //this.start_loc = this.getFrame(this.target).getLocation();
        this.start_loc = GraphicsUtil.toPoint(dc.getLocation());
        JComponent target = dc.getContent();
        if( target.getComponents() != null && target.getComponents().length >0 ){
            target.getComponents()[0].requestFocus();
        }
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        Point current = this.getScreenLocation(e);
        Point offset = new Point(
            (int)current.getX()-(int)start_drag.getX(),
            (int)current.getY()-(int)start_drag.getY());
        //JInternalFrame frame = this.getFrame(target);
        Point new_location = new Point(
            (int)(this.start_loc.getX()+offset.getX()),
            (int)(this.start_loc.getY()+offset.getY()));
        //frame.setLocation(new_location);
        dc.setLocation(new_location);
    }
    public void mouseMoved(MouseEvent e) {}
}

