/*
 * Desklet.java
 *
 * Created on August 4, 2006, 8:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package weatherdesklet;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import org.joshy.util.u;
import org.joshy.weather.Weather;
import org.joshy.weather.WeatherFactory;
/**
 *
 * @author cooper
 */
public class Desklet extends AbstractDesklet{
    
    private WeatherWatcher display;
    private boolean running = false;;
    /** Creates a new instance of Desklet */
    public Desklet() {
        super();
    }
    
    public void destroy() throws Exception {
    }
    
    Thread thread;
    public void start() throws Exception {
        thread = new Thread(new Runnable() {
            public void run() {
                running = true;
                while( running ){
                    try {
                        Weather wth = WeatherFactory.newInstance().getWeather("KATL");
                        display.setWeather(wth);
                        u.p("got weather: " + wth);
                        Thread.currentThread().sleep(30 * 60 * 1000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    context.notifyStopped();
                }
            }
        });
        thread.start();
    }
    
    public void stop() throws Exception {
        this.running = false;
        thread.interrupt();
    }
    
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        display = new WeatherWatcher();
        context.getContainer().setContent(display);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(false);
        context.getContainer().setVisible(true);
    }
    
    
    
}
