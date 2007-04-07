/*
 * GlobalMouse.java
 *
 * Created on March 30, 2007, 2:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.desklet.services;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author joshy
 */
public abstract class GlobalMouse {
    
    public abstract void addMouseListener(MouseInputListener listener, JComponent comp);
}
