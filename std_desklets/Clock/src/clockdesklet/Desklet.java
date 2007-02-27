/*
 * Desklet.java
 *
 * Created on August 4, 2006, 8:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package clockdesklet;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.SwingUtilities;

/**
 *
 * @author cooper
 */
public class Desklet extends AbstractDesklet{
    
    private ClockDisplay display;
    private boolean running = false;
    final SimpleDateFormat format = new SimpleDateFormat("hh:mm");
    final SimpleDateFormat ampm = new SimpleDateFormat("aa");
    
    /** Creates a new instance of Desklet */
    public Desklet() {
        super();
    }
    
    public void destroy() throws Exception {
    }
    
    public void start() throws Exception {
        new Thread(new Runnable() {
            public void run() {
                while(running) {
                    try {
                        Thread.sleep(1000);
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                display.currentTime.setText(format.format(new Date()));
                                if(ampm.format(new Date()).equals("am")) {
                                    display.amLabel.setForeground(Color.RED);
                                    display.pmLabel.setForeground(Color.RED.darker().darker());
                                } else {
                                    display.amLabel.setForeground(Color.RED.darker().darker());
                                    display.pmLabel.setForeground(Color.RED);
                                }
                            }
                        });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("notifying of stoppage");
                context.notifyStopped();
            }
        }).start();
    }
    
    
    public void stop() throws Exception {
        this.running = false;
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        running = true;
        display = new ClockDisplay();
        display.setOpaque( false );
        context.getContainer().setContent(this.display);
        display.currentTime.setText(format.format(new Date()));
        context.getContainer().setShaped(true);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setVisible(true);
    }
    
    
    
}
