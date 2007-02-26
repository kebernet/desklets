/*
 * Main.java
 *
 * Created on February 13, 2007, 12:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package calendar;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;

/**
 *
 * @author joshy
 */
public class CalendarDesklet extends AbstractDesklet {
    
    /** Creates a new instance of Main */
    public CalendarDesklet() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DeskletTester.start(CalendarDesklet.class);
    }

    public void init(DeskletContext context) throws Exception {
        this.context = context;
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        
        CalendarForm form = new CalendarForm();
        context.getContainer().setContent(form);
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
