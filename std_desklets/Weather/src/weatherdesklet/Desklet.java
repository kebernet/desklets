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
    JLabel dockLabel;
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
                    // check the weather
                    updateWeather();
                    // sleep for 30 min
                    try {
                        Thread.currentThread().sleep(5 * 60 * 1000); //sleep for 5 min
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                u.p("stopping");
                context.notifyStopped();
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
        u.p("got weather: " + wth);
        display.setWeather(wth);
        /*
        if(wth == null) {
            dockLabel.setText("-99 unknown");
            dockLabel.setIcon(display.icons16.get(Weather.UNKNOWN));
        } else {
            dockLabel.setText(wth.getTempF()+" " + wth.getWeather());
            dockLabel.setIcon(display.icons16.get(wth.getType()));
        }*/
    }
    
    public void stop() throws Exception {
        this.running = false;
        thread.interrupt();
    }
    
    public DeskletContext getContext() {
        return this.context;
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        dockLabel = new JLabel();
        context.getDockingContainer().setContent(dockLabel);
        display = new WeatherWatcher(this);
        context.getContainer().setContent(display);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setResizable(false);
        context.getContainer().setShaped(true);
        context.getContainer().setVisible(true);
        
        stationID = context.getPreference("STATION_ID","KATL");
        u.p("station ID = " + stationID);
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
