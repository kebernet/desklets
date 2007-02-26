/*
 * CollapseWindowAction.java
 *
 * Created on February 6, 2007, 6:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.actions;

import ab5k.Main;
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
    Main main;
    private boolean windowClosed = false;
    
    /** Creates a new instance of CollapseWindowAction */
    public CollapseWindowAction(Main main) {
        this.main = main;
    }
    
    
    private Rectangle getClosedBounds() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle rect = gc.getBounds();
        Insets insets = tk.getScreenInsets(gc);
        int top_edge = insets.top;
        int height = rect.height - insets.top - insets.bottom;
        
        Rectangle closedBounds = null;
        if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Right) {
            closedBounds = new Rectangle(
                    rect.width-insets.right-main.getMainPanel().getDockWidth(), // all the way right minus insets and width of panel
                    top_edge, // top
                    main.getMainPanel().getDockWidth(),
                    height);
        } else {
            closedBounds = new Rectangle(
                    -rect.width + insets.left + insets.right + main.getMainPanel().getDockWidth(), // all the way right minus insets and width of panel
                    top_edge, // top
                    main.getMainPanel().getDockWidth(),
                    height);
            
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
        if(isWindowClosed()) {
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
        setWindowClosed(false);
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
        
        setWindowClosed(true);
        if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Right) {
            this.putValue(Action.NAME, "<<");
        } else {
            this.putValue(Action.NAME, ">>");
        }
        anim.start();
    }
    
    public Rectangle getStartupPosition() {
        return getClosedBounds();
    }
    
    public boolean isWindowClosed() {
        return windowClosed;
    }
    
    public void setWindowClosed(boolean windowClosed) {
        this.windowClosed = windowClosed;
    }
    
}
