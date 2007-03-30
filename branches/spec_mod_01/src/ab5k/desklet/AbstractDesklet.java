/*
 * AbstractDesklet.java
 *
 * Created on August 4, 2006, 7:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.desklet;

import java.awt.Container;
import java.net.URI;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author cooper
 */
public abstract class AbstractDesklet implements Desklet {
    
    protected DeskletContext context;
    
    /** Creates a new instance of AbstractDesklet */
    public AbstractDesklet() {
    }
    
}
