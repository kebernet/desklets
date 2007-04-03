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
import ab5k.desklet.test.DeskletTester;
import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author cooper
 */
public class Desklet extends AbstractDesklet{
    public static void main(String[] args) {
        DeskletTester.start(clockdesklet.Desklet.class);
    }
    
    private ClockDisplay display;
    private boolean running = false;
    final SimpleDateFormat miniFormat = new SimpleDateFormat("hh:mm aa");
    
    private JLabel timeLabel;
    
    /** Creates a new instance of Desklet */
    public Desklet() {
        super();
    }
    
    public void destroy() throws Exception {
    }
    
    private Thread thread;
    public void start() throws Exception {
        thread = new Thread(new Runnable() {
            public void run() {
                running = true;
                while(running) {
                    try {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                timeLabel.setText(miniFormat.format(new Date()));
                                display.setTime();
                            }
                        });
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException ex) {
                        //ex.printStackTrace();
                    }
                }
                context.notifyStopped();
            }
        });
        thread.start();
    }
    
    
    public void stop() throws Exception {
        this.running = false;
        thread.interrupt();
        System.out.println("stop finishing");
    }

    
    public DeskletContext getContext() {
        return this.context;
    }

    public void init(DeskletContext context) throws Exception {
        this.context = context;
        running = true;
        
        timeLabel = new JLabel("00:00 am");
        context.getDockingContainer().setContent(timeLabel);
        
        
        display = new ClockDisplay(this);
        display.setOpaque( false );
        context.getContainer().setContent(this.display);
        context.getContainer().setShaped(true);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(true);
        context.getContainer().setVisible(true);
    }
    
    
    
}
