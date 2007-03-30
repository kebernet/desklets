/*
 * WeatherWatcher.java
 *
 * Created on June 5, 2006, 4:58 PM
 */

package weatherdesklet;

import ab5k.desklet.DeskletContainer;
import java.awt.Color;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.joshy.util.u;
import org.joshy.weather.Weather;
import org.joshy.weather.WeatherFactory;

/**
 *
 * @author  joshy
 */
public class WeatherWatcher extends JXPanel {
    private WeatherFactory fact = null;
    private Weather weather;
    public Map<Integer,Icon> icons16;
    private Map<Integer,Icon> icons32;
    private Desklet desklet;
    
    
    /** Creates new form WeatherWatcher */
    public WeatherWatcher(Desklet desklet) {
        this.desklet = desklet;
        icons16 = new HashMap<Integer,Icon>();
        icons32 = new HashMap<Integer,Icon>();
        initComponents();
        
        icons16.put(Weather.CLEAR,new ImageIcon(getClass().getResource("images/weather-clear.16.png")));
        icons16.put(Weather.CLOUDY,new ImageIcon(getClass().getResource("images/weather-overcast.16.png")));
        icons16.put(Weather.LIGHTNING,new ImageIcon(getClass().getResource("images/weather-storm.16.png")));
        icons16.put(Weather.RAIN,new ImageIcon(getClass().getResource("images/weather-showers.16.png")));
        icons16.put(Weather.SNOW,new ImageIcon(getClass().getResource("images/weather-snow.16.png")));
        icons16.put(Weather.SUNNY,new ImageIcon(getClass().getResource("images/weather-clear.16.png")));
        icons16.put(Weather.UNKNOWN,new ImageIcon(getClass().getResource("images/weather-severe-alert.16.png")));
        
        icons32.put(Weather.CLEAR,new ImageIcon(getClass().getResource("images/weather-clear.32.png")));
        icons32.put(Weather.CLOUDY,new ImageIcon(getClass().getResource("images/weather-overcast.32.png")));
        icons32.put(Weather.LIGHTNING,new ImageIcon(getClass().getResource("images/weather-storm.32.png")));
        icons32.put(Weather.RAIN,new ImageIcon(getClass().getResource("images/weather-showers.32.png")));
        icons32.put(Weather.SNOW,new ImageIcon(getClass().getResource("images/weather-snow.32.png")));
        icons32.put(Weather.SUNNY,new ImageIcon(getClass().getResource("images/weather-clear.32.png")));
        icons32.put(Weather.UNKNOWN,new ImageIcon(getClass().getResource("images/weather-severe-alert.32.png")));
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        RectanglePainter rp = new RectanglePainter(0,0,0,0, 25,25, true, Color.GREEN, 1.0f, Color.GREEN.darker());
        rp.setBorderWidth(5f);
        rp.setInsets(new Insets(1,1,1,1));
        this.setBackgroundPainter(new CompoundPainter(rp));
        /*
        URL painter = this.getClass().getResource("WeatherWatcher.painter");
        URL painter2 = this.getClass().getResource("WeatherWatcher.temp.painter");
        try {
            this.setBackgroundPainter(PainterUtil.loadPainter(painter));
            ((JXLabel)temp).setForegroundPainter(PainterUtil.loadPainter(painter2));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
         */
        //this.setBackgroundPainter(new URLPainter(WeatherWatcher.class,"WeatherWatcher.painter"));
        /*
        try{
            setWeather(fact.getWeather("KATL"));
        }catch(Exception e){
            e.printStackTrace();
        }*/
    }
    
    public Weather getWeather() {
        return weather;
    }
    
    public void setWeather(Weather weather) {
        this.weather = weather;
        weatherIcon.setText("");
        if(weather == null) {
            description.setText("unknown");
            temp.setText("-99");
            type.setText("Unknown");
            weatherIcon.setIcon(icons32.get(Weather.UNKNOWN));
            desklet.dockLabel.setText("-99 unknown");
            desklet.dockLabel.setIcon(icons16.get(Weather.UNKNOWN));
        } else {
            description.setText(getWeather().getLocation());
            temp.setText(getWeather().getTempF()+"");
            type.setText(getWeather().getWeather());
            weatherIcon.setIcon(icons32.get(getWeather().getType()));
            desklet.dockLabel.setText(weather.getTempF()+" " + weather.getWeather());
            desklet.dockLabel.setIcon(icons16.get(weather.getType()));
        }
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        description = new javax.swing.JLabel();
        type = new javax.swing.JLabel();
        setupButton = new javax.swing.JButton();
        temp = new org.jdesktop.swingx.JXLabel();
        weatherIcon = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 255, 255));
        description.setForeground(new java.awt.Color(51, 51, 51));
        description.setText("this is a really long description of the location");

        type.setForeground(new java.awt.Color(51, 51, 51));
        type.setText("jLabel4");

        setupButton.setText("Setup");
        setupButton.setContentAreaFilled(false);
        setupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupButtonActionPerformed(evt);
            }
        });

        temp.setBackgroundPainter(null);
        temp.setText("i");
        temp.setFont(new java.awt.Font("Lucida Grande", 0, 60));

        weatherIcon.setText("Weather Icon");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 215, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setupButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(weatherIcon)
                            .addComponent(type))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(temp, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(weatherIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(type))
                    .addComponent(temp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setupButton)
                    .addComponent(description))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void setupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupButtonActionPerformed
        
        if(getWeather() != null) { getWeather().getStationID(); }
        
        DeskletContainer dc = desklet.getContext().getConfigurationContainer();
        System.out.println("context = " + dc);
        WeatherDialog dia = new WeatherDialog(desklet, dc);
        dc.setContent(dia);
        dc.setVisible(true);
        /*
        u.p("selected station = " + dia.selectedStation);
        try {
            setWeather(getFact().getWeather(dia.selectedStation));
            setSelectedStation(dia.selectedStation);
        } catch (Exception ex) {
            u.p(ex);
        }*/
        
    }//GEN-LAST:event_setupButtonActionPerformed
    
    private void setSelectedStation(String selectedStation) {
        desklet.setStationID(selectedStation);
    }
    
    public WeatherFactory getFact() {
        if(fact == null) {
            fact = WeatherFactory.newInstance();
        }
        return fact;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel description;
    private javax.swing.JButton setupButton;
    private org.jdesktop.swingx.JXLabel temp;
    private javax.swing.JLabel type;
    private javax.swing.JLabel weatherIcon;
    // End of variables declaration//GEN-END:variables
    
}
