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
public class JDialogDeskletContainer extends JFrameDeskletContainer {
    JFrameDeskletContainer parent;
    JDialog dialog;
    
    /** Creates a new instance of JDialogDeskletContainer */
    public JDialogDeskletContainer(final BufferedWM wm, DefaultContext context,
            JFrameDeskletContainer parent) {
        super(wm,context);
        this.parent = parent;
        this.frame = null;
        this.dialog = new JDialog(parent.frame);
        this.root = this.dialog;
        this.window = this.dialog;
    }
    
}
