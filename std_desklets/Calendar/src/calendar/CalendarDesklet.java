/*
 * Main.java
 *
 * Created on February 13, 2007, 12:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package calendar;

import org.glossitope.desklet.Desklet;
import org.glossitope.desklet.DeskletContext;
import org.glossitope.desklet.test.DeskletTester;
import javax.swing.JLabel;
import org.joda.time.DateTime;

/**
 *
 * @author joshy
 */
public class CalendarDesklet extends Desklet {
    
    private CalendarForm form;
    private JLabel dockLabel;
    
    /** Creates a new instance of Main */
    public CalendarDesklet() {
    }
    
    public void init() throws Exception {
        form = new CalendarForm();
        dockLabel = new JLabel();
        
        DeskletContext context = getContext();
        context.getContainer().setContent(form);
        
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        context.getContainer().setVisible(true);
        
        context.getDockingContainer().setContent(dockLabel);
        
    }
    
    public void start() throws Exception {
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    form.updateDate();
                    DateTime date = form.getDate();
                    dockLabel.setText(date.dayOfWeek().getAsText()+" "+
                            date.monthOfYear().getAsShortText() + "/" +
                            date.dayOfMonth().getAsShortText() + "/" +
                            date.year().getAsShortText());
                    // update every minute
                    try {
                        Thread.sleep(1000*60);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public void stop() throws Exception {
        getContext().notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DeskletTester.start(CalendarDesklet.class);
    }
    
}
