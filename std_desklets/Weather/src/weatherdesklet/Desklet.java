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
import javax.swing.JLabel;
import org.joshy.util.u;
import org.joshy.weather.Weather;
import org.joshy.weather.WeatherFactory;
/**
 *
 * @author cooper
 */
public class Desklet extends AbstractDesklet{
    
    private WeatherWatcher display;
    private JLabel dockLabel;
    private boolean running = false;;
    private String stationID = null;
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
                        Weather wth = WeatherFactory.newInstance().getWeather(getStationID());
                        display.setWeather(wth);
                        u.p("got weather: " + wth);
                        dockLabel.setText(wth.getTempF()+" " + wth.getWeather());
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
        dockLabel = new JLabel();
        context.getDockingContainer().setContent(dockLabel);
        display = new WeatherWatcher(this);
        context.getContainer().setContent(display);
        context.getContainer().setBackgroundDraggable(false);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        context.getContainer().setVisible(true);
        stationID = context.getPreference("STATION_ID","KATL");
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
        System.out.println("saved station ID"+ stationID);
        context.setPreference("STATION_ID",stationID);
    }
    
    
    
}
