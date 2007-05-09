/*
 * BaseAction.java
 *
 * Created on March 28, 2007, 4:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.actions;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.AbstractAction;

/**
 *
 * @author joshy
 */
public abstract class BaseAction extends AbstractAction {
    
    /** Creates a new instance of BaseAction */
    public BaseAction() {
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
    
}
