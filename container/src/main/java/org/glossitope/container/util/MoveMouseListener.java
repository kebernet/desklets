package org.glossitope.container.util;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.glossitope.desklet.DeskletContainer;
import org.glossitope.container.wm.WindowManager;
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
    Point2D getScreenLocation() {
        return dc.getLocation();
    }
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
        this.start_loc = GraphicsUtil.toPoint(getScreenLocation());
        JComponent target = dc.getContent();
        if( target.getComponents() != null && target.getComponents().length >0 ){
            // use discouraged -&gt; see docs
            target.getComponents()[0].requestFocus();
        }
    }

    public void jumpstartDrag() {
        this.start_drag = GraphicsUtil.toPoint(getScreenLocation());
        this.start_loc = GraphicsUtil.toPoint(getScreenLocation());
    }

    public void mouseReleased(MouseEvent e) {
        JComponent target = dc.getContent();
        if(target != null) {
            target.requestFocusInWindow();
        }
    }

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

