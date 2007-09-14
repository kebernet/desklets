/*
 * WeatherWatcher.java
 *
 * Created on June 5, 2006, 4:58 PM
 */

package weatherdesklet;

import org.glossitope.desklet.DeskletContainer;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.joshy.weather.Weather;
import org.joshy.weather.WeatherFactory;

/**
 *
 * @author  joshy
 */
public class WeatherWatcher extends JXPanel {
    private boolean useDummyData = true;
    
    private WeatherFactory fact = null;
    private Weather weather;
    public Map<Integer,Icon> icons16;
    private Map<Integer,Icon> icons32;
    private Map<Integer, BufferedImage> iconsLarge;
    private Desklet desklet;
    
    
    
    private ImagePainter picture;
    private ImagePainter digit10;
    private ImagePainter digit1;
    private BufferedImage[] digits;
    
    
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
        /*
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
         */
        try {
            ImagePainter base = loadImagePainter("widget_base/widget_base.png");
            base.setInsets(new Insets(0,0,0,0));
            picture = loadImagePainter("forecasts/cloudy.png");
            digit10 = loadImagePainter("temperature/numbers_0.png");
            digit10.setInsets(new Insets(87,160,0,0));
            digit1 = loadImagePainter("temperature/numbers_0.png");
            digit1.setInsets(new Insets(87,195,0,0));
            digit1.setCacheable(false);
            ImagePainter degree = loadImagePainter("temperature/degree_symbol_big.png");
            degree.setInsets(new Insets(87,235,0,0));
            ImagePainter unit = loadImagePainter("temperature/farenheit.png");
            unit.setInsets(new Insets(112,233,0,0));
            
            
            digits = new BufferedImage[10];
            for(int i=0; i<10; i++) {
                digits[i] = ImageIO.read(getClass().getResource("dcimages/temperature/numbers_"+i+".png"));
            }
            this.setBackgroundPainter(new CompoundPainter(base, picture,
                    digit10, digit1, degree, unit));
            
            iconsLarge = new HashMap<Integer, BufferedImage>();
            iconsLarge.put(Weather.CLOUDY, loadImage("forecasts/cloudy.png"));
            iconsLarge.put(Weather.CLEAR, loadImage("forecasts/sunny.png"));
            iconsLarge.put(Weather.HAIL, loadImage("forecasts/stormy.png"));
            iconsLarge.put(Weather.LIGHTNING, loadImage("forecasts/stormy.png"));
            iconsLarge.put(Weather.RAIN, loadImage("forecasts/rain.png"));
            iconsLarge.put(Weather.SLEET, loadImage("forecasts/stormy.png"));
            iconsLarge.put(Weather.SNOW, loadImage("forecasts/snow.png"));
            iconsLarge.put(Weather.SUNNY, loadImage("forecasts/sunny.png"));
            
            
            setupButton.setIcon(loadIcon("config_button/config_mouseout.png"));
            setupButton.setRolloverIcon(loadIcon("config_button/config_mouseover.png"));
            setupButton.setText("");
            setupButton.setBorder(BorderFactory.createEmptyBorder());
            
            this.setPreferredSize(new Dimension(300,150));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public ImagePainter loadImagePainter(String path) throws IOException {
        ImagePainter base = new ImagePainter(getClass().getResource("dcimages/"+path));
        base.setHorizontalAlignment(ImagePainter.HorizontalAlignment.LEFT);
        base.setVerticalAlignment(ImagePainter.VerticalAlignment.TOP);
        return base;
    }
    
    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(getClass().getResource("dcimages/"+path));
    }
    
    private Icon loadIcon(String path) throws IOException {
        return new ImageIcon(loadImage(path));
    }
    
    
    public Weather getWeather() {
        return weather;
    }
    
    public void setWeather(Weather weather) {
        this.weather = weather;
        
        
        if(useDummyData) {
            type.setText("CLOUDY");
            digit1.setImage(digits[3]);
            digit10.setImage(digits[5]);
            desklet.dockLabel.setText("35F cloudy KEUG");
            desklet.dockLabel.setIcon(icons16.get(Weather.CLOUDY));
            picture.setImage(iconsLarge.get(Weather.CLOUDY));
            this.repaint();
            return;
        }
        if(weather == null) {
            digit1.setImage(digits[9]);
            digit10.setImage(digits[9]);
            this.repaint();
            return;
        }
        
        
        digit10.setImage(digits[(int)weather.getTempF()/10]);
        digit1.setImage(digits[((int)weather.getTempF())%10]);
        picture.setImage(iconsLarge.get(weather.getType()));
        type.setText(weather.getWeather());
        description.setText(getWeather().getStationID());
        desklet.dockLabel.setText(weather.getTempF()+" " + weather.getWeather() + " " + weather.getStationID());
        desklet.dockLabel.setIcon(icons16.get(weather.getType()));
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        description = new javax.swing.JLabel();
        type = new javax.swing.JLabel();
        setupButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(153, 255, 255));

        description.setFont(new java.awt.Font("Arial", 2, 14));
        description.setForeground(new java.awt.Color(255, 255, 255));
        description.setText("WEUG");

        type.setFont(new java.awt.Font("Arial", 0, 14));
        type.setForeground(new java.awt.Color(255, 255, 255));
        type.setText("SHOWERS");

        setupButton.setText("Setup");
        setupButton.setContentAreaFilled(false);
        setupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(180, 180, 180)
                .add(type))
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(description, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(89, 89, 89)
                .add(setupButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(60, 60, 60)
                .add(type)
                .add(36, 36, 36)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(7, 7, 7)
                        .add(description))
                    .add(setupButton)))
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
    private javax.swing.JLabel type;
    // End of variables declaration//GEN-END:variables
    
}
