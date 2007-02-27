/*
 * MainDesklet.java
 *
 * Created on February 9, 2007, 9:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wikipediadesklet;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import java.awt.BorderLayout;

/**
 *
 * @author joshy
 */
public class MainDesklet extends AbstractDesklet {
    MainForm form;
    /** Creates a new instance of MainDesklet */
    public MainDesklet() {
    }

    public void init(DeskletContext context) throws Exception {
        this.context = context;
        this.form = new MainForm();
        form.setOpaque(false);
        form.context = context;
        context.getContainer().setContent( form );
        context.getContainer().setShaped(true);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(true);
        context.getContainer().setVisible(true);
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        context.notifyStopped();
    }

    public void destroy() throws Exception {
    }
    
}
