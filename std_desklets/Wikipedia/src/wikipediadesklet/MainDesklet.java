/*
 * MainDesklet.java
 *
 * Created on February 9, 2007, 9:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wikipediadesklet;


import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContext;

/**
 *
 * @author joshy
 */
public class MainDesklet extends Desklet {
    MainForm form;
    /** Creates a new instance of MainDesklet */
    public MainDesklet() {
    }

    public void init() throws Exception {
        DeskletContext context = getContext();
        this.form = new MainForm();
        form.setOpaque(false);
        form.context = context;
        context.getContainer().setContent( form );
        //context.getContainer().setShaped(true);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setVisible(true);
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        getContext().notifyStopped();
    }

    public void destroy() throws Exception {
    }
    
}
