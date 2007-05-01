/*
 * CCDesklet.java
 *
 * Created on February 16, 2007, 10:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package colorchooser;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;
import javax.swing.JColorChooser;
import org.jdesktop.swingx.color.EyeDropperColorChooserPanel;

/**
 *
 * @author joshy
 */
public class CCDesklet extends AbstractDesklet {
    
    /** Creates a new instance of CCDesklet */
    public CCDesklet() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DeskletTester.start(CCDesklet.class);
    }

    public void init(DeskletContext context) throws Exception {
        EyeDropperColorChooserPanel panel = new EyeDropperColorChooserPanel();
        JColorChooser chooser = new JColorChooser();
        chooser.addChooserPanel(panel);
        this.context = context;
        context.getContainer().setContent(panel);
        context.getContainer().setResizable(false);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setVisible(true);
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        this.context.notifyStopped();
    }

    public void destroy() throws Exception {
    }
    
}
