/*
 * JDialogDeskletContainer.java
 *
 * Created on March 27, 2007, 7:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.security.DefaultContext;
import javax.swing.JDialog;

/**
 *
 * @author joshy
 */
public class JDialogDeskletContainer extends JFramePeer {
    JFramePeer parent;
    JDialog dialog;
    
    /** Creates a new instance of JDialogDeskletContainer */
    public JDialogDeskletContainer(BufferedDeskletContainer bdc) {
        super(bdc);
        this.parent = parent;
        this.frame = null;
        this.dialog = new JDialog(parent.frame);
        this.root = this.dialog;
        this.window = this.dialog;
    }
    
}
