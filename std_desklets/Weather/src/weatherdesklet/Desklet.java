/*
 * Desklet.java
 *
 * Created on August 4, 2006, 8:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package weatherdesklet;

import org.glossitope.desklet.DeskletContext;
import javax.swing.JLabel;
import org.joshy.weather.Weather;
import org.joshy.weather.WeatherFactory;
/**
 *
 * @author cooper
 */
public class Desklet extends org.glossitope.desklet.Desklet{
    
    private WeatherWatcher display;
    JLabel dockLabel;
    private boolean running = false;;
    private String stationID = null;
    
    /** Creates a new instance of Desklet */
    public Desklet() {
        super();
    }
    
    
    public void init() throws Exception {
        DeskletContext context = getContext();
        dockLabel = new JLabel();
        context.getDockingContainer().setContent(dockLabel);
        display = new WeatherWatcher(this);
        context.getContainer().setContent(display);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        context.getContainer().setVisible(true);
        stationID = context.getPreference("STATION_ID","KATL");
    }
    
    Thread thread;
    public void start() throws Exception {
        thread = new Thread(new Runnable() {
            public void run() {
                running = true;
                while( running ){
                    // check the weather
                    updateWeather();
                    // sleep for 5 min
                    try {
                        Thread.currentThread().sleep(5 * 60 * 1000); //sleep for 5 min
                    } catch (InterruptedException ex) {
                        //ex.printStackTrace();
                    }
                }
                getContext().notifyStopped();
            }
            
        });
        thread.start();
    }
    
    private void updateWeather() {
        Weather wth = null;
        try {
            wth = WeatherFactory.newInstance().getWeather(getStationID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        display.setWeather(wth);
    }
    
    public void stop() throws Exception {
        this.running = false;
        thread.interrupt();
    }
    
    public void destroy() throws Exception {
    }
    
    
    public String getStationID() {
        return stationID;
    }
    
    public void setStationID(String stationID) {
        this.stationID = stationID;
        System.out.println("saved station ID"+ stationID);
        getContext().setPreference("STATION_ID",stationID);
    }
    
    
    
}
