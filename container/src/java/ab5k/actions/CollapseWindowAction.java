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
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class CollapseWindowAction extends AbstractAction {
    Core main;
    
    /** Creates a new instance of CollapseWindowAction */
    public CollapseWindowAction(Core main) {
        this.main = main;
    }
    
    
    public Rectangle getClosedBounds() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle rect = gc.getBounds();
        Insets insets = tk.getScreenInsets(gc);
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
            u.p("current width = " + main.getFrame().getWidth());
            if(main.getFrame().getWidth() == main.getMainPanel().getDockWidth()) {
                u.p("still closed");
                closedBounds = new Rectangle(
                        0 + insets.left, // all the way right minus insets and width of panel
                        top_edge, // top
                        main.getMainPanel().getDockWidth(),
                        height);
            } else {
                closedBounds = new Rectangle(
                        -rect.width + insets.left*2 + insets.right + main.getMainPanel().getDockWidth(), // all the way right minus insets and width of panel
                        top_edge, // top
                        main.getMainPanel().getDockWidth(),
                        height);
            }
            
        }
        return closedBounds;
    }
    
    
    public Rectangle getOpenBounds() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle rect = gc.getBounds();
        //u.p("rect = " + rect);
        Insets insets = tk.getScreenInsets(gc);
        //u.p("insets = " + insets);
        //Dimension dim = tk.getScreenSize();
        //u.p("size = " + dim);
        int top_edge = insets.top;
        int height = rect.height - insets.top - insets.bottom;
        
        Rectangle openBounds = new Rectangle(insets.left, top_edge,
                rect.width - insets.left - insets.right, height);
        return openBounds;
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
        Rectangle opened = getOpenBounds();
        main.getFrame().setSize(opened.getSize());
        Animator anim = new Animator(500);
        anim.addTarget(new PropertySetter(this.main.getFrame(),"location",current.getLocation(),opened.getLocation()));
        if(current.getHeight() != opened.getHeight()) {
            anim.addTarget(new PropertySetter(this.main.getFrame(),"size",current.getSize(),opened.getSize()));
        }
        main.getCloser().setWindowClosed(false);
        if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Right) {
            this.putValue(Action.NAME, ">>");
        } else {
            this.putValue(Action.NAME, "<<");
        }
        anim.start();
    }
    
    public void doCollapse() {
        Rectangle current = this.main.getFrame().getBounds();
        Rectangle closed = getClosedBounds();
        
        Animator anim = new Animator(500);
        anim.addTarget(new PropertySetter(this.main.getFrame(),"location",current.getLocation(),closed.getLocation()));
        if(current.getHeight() != closed.getHeight()) {
            anim.addTarget(new PropertySetter(this.main.getFrame(),"size",current.getSize(),closed.getSize()));
        }
        
        main.getCloser().setWindowClosed(true);
        if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Right) {
            this.putValue(Action.NAME, "<<");
        } else {
            this.putValue(Action.NAME, ">>");
        }
        anim.start();
    }
    
    public Rectangle getStartupPosition() {
        u.p("returning a startup pos of: " + getClosedBounds());
        return getClosedBounds();
    }
    
    
}
