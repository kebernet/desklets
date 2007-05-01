/*
 * CollapseWindowAction.java
 *
 * Created on February 6, 2007, 6:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.actions;

import ab5k.Core;
import ab5k.MainPanel;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import javax.swing.Action;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author joshy
 * @author rah003
 */
public class CollapseWindowAction extends BaseAction {
    Core main;
    
    /** Creates a new instance of CollapseWindowAction */
    public CollapseWindowAction(Core main) {
        this.main = main;
    }
    
    
    public Rectangle getClosedBounds() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle rect = gc.getBounds();
        //System.out.println("gc bounds:" + rect);
        Insets insets = tk.getScreenInsets(gc);
        //System.out.println("gc insets:" + insets);
        int top_edge = insets.top;
        int height = rect.height - insets.top - insets.bottom;
        
        Rectangle closedBounds = null;
        if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Right) {
            closedBounds = new Rectangle(
                    rect.width - insets.right - main.getMainPanel().getDockWidth(), // all the way right minus insets and width of panel
                    top_edge, // top
                    main.getMainPanel().getDockWidth(),
                    height);
        } else {
            //u.p("current width = " + main.getFrame().getWidth());
            if(main.getFrame().getWidth() == main.getMainPanel().getDockWidth()) {
                //u.p("still closed");
                closedBounds = new Rectangle(
                        0 + insets.left, // all the way right minus insets and width of panel
                        top_edge, // top
                        main.getMainPanel().getDockWidth(),
                        height);
            } else {
                closedBounds = new Rectangle(
                        // all the way right minus insets and width of panel, 
                        // but not below zero in case there's one more screen on the left
                        Math.max(0,-rect.width + insets.left*2 + insets.right + main.getMainPanel().getDockWidth()), 
                        top_edge, // top
                        main.getMainPanel().getDockWidth(),
                        height);
            }
            
        }
        //System.out.println("Returning closed bounds: " + closedBounds);
        return closedBounds;
    }
    
    
        
    public void actionPerformed(ActionEvent e) {
        if(main.getCloser().isWindowClosed()) {
            doExpand();
        } else {
            doCollapse();
        }
    }
    
    public void doExpand() {
        Rectangle current = this.main.getFrame().getBounds();
        final Rectangle opened = getOpenBounds();
        Animator anim = new Animator(1000);
        anim.setAcceleration(.3f);
        anim.setDeceleration(.3f);
        
        // FYI: change size before location on expand to prevent flickering.
        //anim.addTarget(new PropertySetter(this.main.getFrame(), "size", current.getSize(), opened.getSize()));
        anim.addTarget(new PropertySetter(this.main.getFrame(), "location", 
                current.getLocation(), opened.getLocation()));
        // just set the size instead of resizing during the animation
        anim.addTarget(new TimingTargetAdapter() {
            public void begin() {
                main.getFrame().setSize(opened.getSize());
            }
        });
        main.getCloser().setWindowClosed(false);
        anim.start();
    }
    
    public void doCollapse() {
        Rectangle current = this.main.getFrame().getBounds();
        final Rectangle closed = getClosedBounds();
        Animator anim = new Animator(1000);
        anim.setAcceleration(.3f);
        anim.setDeceleration(.6f);
        final Point2D location = current.getLocation().equals(closed.getLocation()) ? new Point2D.Double(closed.getLocation()
                .getX()
                + getClosedBounds().width - current.width, closed.getLocation().getY()) : (Point2D) closed.getLocation();
        // FYI: change location before size on colapse to prevent flickering.
        anim.addTarget(new PropertySetter(this.main.getFrame(),"location",current.getLocation(),location));
        
        //anim.addTarget(new PropertySetter(this.main.getFrame(),"size",current.getSize(),closed.getSize()));
        // instead of resizing along the way we just set to small at the end
        anim.addTarget(new TimingTargetAdapter() {
            public void begin() {
                main.getCloser().setWindowClosed(true);
                // skip microdocking while moving
                if (main.getCloser().isMicrodocking()) {
                    main.getCloser().setOnTheMove(true);
                }
            }
            public void end() {
                main.getFrame().setSize(closed.getSize());
                if (location.getX() < 0) {
                    main.getFrame().setLocation(0, (int) location.getY());
                }
                Closer closer = main.getCloser();
                if (closer.isMicrodocking()) {
                    closer.setOnTheMove(false);
                } 
            }
        });
        anim.start();
    }
    
    public Rectangle getStartupPosition() {
        //u.p("returning a startup pos of: " + getClosedBounds());
        return getClosedBounds();
    }
    
    
}
