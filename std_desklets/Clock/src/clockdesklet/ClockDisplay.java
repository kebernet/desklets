/*
 * ClockDesklet.java
 *
 * Created on May 30, 2006, 4:52 PM
 */

package clockdesklet;
import ab5k.desklet.DeskletContainer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;
import org.jdesktop.swingx.util.PaintUtils;
/**
 * @author  joshy
 */
public class ClockDisplay extends JXPanel {
    Desklet desklet;
    
    private DeskletContainer setupContainer;
    
    /** Creates new form ClockDesklet */
    public ClockDisplay() {
        
    }
    public ClockDisplay(Desklet desklet) {
        this.desklet = desklet;
        initComponents();
        setDigital();
        //setHex();
        //setAnalog();
        
    }
    void setDigital() {
        this.pmLabel.setVisible(true);
        this.currentTime.setVisible(true);
        this.amLabel.setVisible(true);
        this.setPreferredSize(new Dimension(300,100));
        this.desklet.getContext().getContainer().pack();
        
        try {
            currentTime.setFont(
                    Font.createFont(Font.TRUETYPE_FONT,
                    this.getClass().getResourceAsStream("Digir___.ttf")
                    ).deriveFont(Font.PLAIN,80));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //currentTime.setFont(new Font("Sanserif",Font.BOLD,12));
        
        
        // shiny metal gradient
        Paint bp = new org.apache.batik.ext.awt.LinearGradientPaint(new Point(0,0),new Point(0,10),
                new float[] {0.0f,0.37f,1.0f},
                new Color[] {new Color(100,100,100,255),new Color(255,255,255,255),new Color(150,150,150,255)});
        
        RectanglePainter clip = new RectanglePainter(2,2,2,2, 20,20, true, Color.BLACK, 1f, Color.GRAY);
        clip.setStyle(RectanglePainter.Style.BOTH);
        clip.setBorderPaint(Color.BLACK);
        clip.setFillPaint(bp);
        clip.setPaintStretched(true);
        
        int ins = 10;
        RectanglePainter border = new RectanglePainter(ins,ins,ins,ins, 10,10, true, Color.BLACK, 3f, Color.RED);
        border.setStyle(RectanglePainter.Style.FILLED);
        
        TextPainter tp = new TextPainter();
        tp.setFillPaint(Color.RED);
        
        GlossPainter gloss = new GlossPainter();
        CompoundPainter cp = new CompoundPainter(clip,border,gloss);
        cp.setClipPreserved(true);
        
        currentTime.setForegroundPainter(tp);
        
        this.setBackgroundPainter(cp);
        this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        currentTime.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        amLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,1));
        pmLabel.setBorder(BorderFactory.createEmptyBorder(5,1,5,5));
    }
    
    void setHex() {
        
        this.setPreferredSize(new Dimension(200,200));
        this.pmLabel.setVisible(false);
        this.currentTime.setVisible(false);
        this.amLabel.setVisible(false);
        
        try {
            desklet.getContext().getContainer().pack();
            double scale = 0.4;
            ImagePainter background = loadImagePainter("HexClock/Background.png",scale);
            ImagePainter face = loadImagePainter("HexClock/Face.png",scale);
            face.setInsets(new Insets((int)(32*scale),(int)(40*scale),0,0));
            
            final BufferedImage hourImg = ImageIO.read(getClass().getResource("images/HexClock/Hour.png"));
            final BufferedImage hourShadImg = ImageIO.read(getClass().getResource("images/HexClock/Hour Shadow.png"));
            final BufferedImage minImg = ImageIO.read(getClass().getResource("images/HexClock/Minute.png"));
            final BufferedImage minShadImg = ImageIO.read(getClass().getResource("images/HexClock/Minute Shadow.png"));
            final BufferedImage secImg = ImageIO.read(getClass().getResource("images/HexClock/Second.png"));
            final BufferedImage secShadImg = ImageIO.read(getClass().getResource("images/HexClock/Second Shadow.png"));
            final BufferedImage nubImg = ImageIO.read(getClass().getResource("images/HexClock/Nub.png"));
            final BufferedImage nubShadImg = ImageIO.read(getClass().getResource("images/HexClock/Nub Shadow.png"));
            
            Painter hands = new HandsPainter(hourShadImg, hourImg, minShadImg, minImg,
                    secShadImg, secImg, nubShadImg, nubImg,
                    (int)(63*scale*3.5),(int)(62*scale*3.5),scale*3.5);
            CompoundPainter cp = new CompoundPainter(face,background,hands);
            this.setBackgroundPainter(cp);
            this.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    void setAnalog() {
        this.setPreferredSize(new Dimension(200,200));
        this.pmLabel.setVisible(false);
        this.currentTime.setVisible(false);
        this.amLabel.setVisible(false);
        desklet.getContext().getContainer().pack();
        try {
            double scale = 0.4;
            
            ImagePainter background = loadImagePainter("Analog/Base/Background.png",scale);
            ImagePainter ticks = loadImagePainter("Analog/Base/Ticks.png",scale);
            ImagePainter backgroundShadow = loadImagePainter("Analog/Base/BackgroundShadow.png",scale);
            ImagePainter bevel = loadImagePainter("Analog/Base/Bevel.png",scale);
            ImagePainter bevelShadow = loadImagePainter("Analog/Base/BevelShadow.png",scale);
            
            ImagePainter digits = loadImagePainter("Analog/Base/Digits.png",scale);
            ImagePainter glassColor = loadImagePainter("Analog/Base/GlassColor.png",scale);
            ImagePainter glassShadow = loadImagePainter("Analog/Base/GlassShadow.png",scale);
            ImagePainter glassShine = loadImagePainter("Analog/Base/GlassShine.png",scale);
            ImagePainter gradient = loadImagePainter("Analog/Base/Gradient.png",scale);
            ImagePainter border = loadImagePainter("Analog/Base/Border.png",scale);
            ImagePainter borderShadow = loadImagePainter("Analog/Base/BorderShadow.png",scale);
            
            final BufferedImage hourImg = ImageIO.read(getClass().getResource("images/Analog/Hands/HourHand.png"));
            final BufferedImage hourShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/HourHandShadow.png"));
            final BufferedImage minImg = ImageIO.read(getClass().getResource("images/Analog/Hands/MinuteHand.png"));
            final BufferedImage minShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/MinuteHandShadow.png"));
            final BufferedImage secImg = ImageIO.read(getClass().getResource("images/Analog/Hands/SecondHand.png"));
            final BufferedImage secShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/SecondHandShadow.png"));
            final BufferedImage nubImg = ImageIO.read(getClass().getResource("images/Analog/Hands/Cap.png"));
            final BufferedImage nubShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/CapShadow.png"));
            
            Painter hands = new HandsPainter(
                    hourShadImg, hourImg, minShadImg, minImg,
                    secShadImg, secImg, nubShadImg, nubImg,
                    (int)(236*scale),(int)(236*scale), 0.7*scale);
            
            CompoundPainter cp = new CompoundPainter(
                    borderShadow,
                    border,
                    //bevelShadow,
                    //backgroundShadow,
                    //glassShadow,
                    bevel,
                    background,
                    ticks,
                    digits,
                    hands,
                    glassColor,
                    glassShine
                    //gradient,
                    );
            this.setBackgroundPainter(cp);
            this.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        currentTime = new org.jdesktop.swingx.JXLabel();
        jButton1 = new javax.swing.JButton();
        amLabel = new javax.swing.JLabel();
        pmLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 255, 0));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 15));
        setOpaque(false);
        currentTime.setBackground(new java.awt.Color(0, 255, 153));
        currentTime.setForeground(new java.awt.Color(255, 0, 0));
        currentTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentTime.setText("00:00");
        currentTime.setFont(new java.awt.Font("Lucida Grande", 0, 60));

        jButton1.setText("i");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setOpaque(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        amLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        amLabel.setText("AM");

        pmLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        pmLabel.setText("PM");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(currentTime, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(amLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pmLabel)
                        .addGap(42, 42, 42))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(currentTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amLabel)
                    .addComponent(pmLabel)))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setupContainer = desklet.getContext().getConfigurationContainer();
        SetupPanel setup = new SetupPanel();
        setup.display = this;
        setupContainer.setContent(setup);
        setupContainer.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    void closeSetup() {
        setupContainer.setVisible(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel amLabel;
    public org.jdesktop.swingx.JXLabel currentTime;
    private javax.swing.JButton jButton1;
    public javax.swing.JLabel pmLabel;
    // End of variables declaration//GEN-END:variables
    
    final SimpleDateFormat format = new SimpleDateFormat("hh:mm");
    final SimpleDateFormat ampm = new SimpleDateFormat("aa");
    
    public void setTime() {
        currentTime.setText(format.format(new Date()));
        this.repaint();
        if(ampm.format(new Date()).equals("am")) {
            amLabel.setForeground(Color.RED);
            pmLabel.setForeground(Color.RED.darker().darker());
        } else {
            amLabel.setForeground(Color.RED.darker().darker());
            pmLabel.setForeground(Color.RED);
        }
    }
    
    void setDesklet(Desklet desklet) {
        this.desklet = desklet;
    }
    
    private void p(String string) {
        System.out.println(string);
    }
    
    private ImagePainter loadImagePainter(String path, double scale) throws IOException {
        ImagePainter painter = new ImagePainter(getClass().getResource("images/"+path),
                ImagePainter.HorizontalAlignment.LEFT,
                ImagePainter.VerticalAlignment.TOP);
        painter.setInterpolation(ImagePainter.Interpolation.Bicubic);
        painter.setImageScale(scale);
        return painter;
    }
    
    private class HandsPainter implements Painter {
        
        private BufferedImage hourShadImg;
        private BufferedImage hourImg;
        private BufferedImage minShadImg;
        private BufferedImage minImg;
        private BufferedImage secShadImg;
        private BufferedImage secImg;
        private BufferedImage nubShadImg;
        private BufferedImage nubImg;
        private int xoff;
        private int yoff;
        
        private double scale;
        
        public HandsPainter(BufferedImage hourShadImg, BufferedImage hourImg, BufferedImage minShadImg, BufferedImage minImg,
                BufferedImage secShadImg, BufferedImage secImg, BufferedImage nubShadImg, BufferedImage nubImg,
                int xoff, int yoff, double scale) {
            super();
            this.hourShadImg = hourShadImg;
            this.hourImg = hourImg;
            this.minShadImg = minShadImg;
            this.minImg = minImg;
            this.secShadImg = secShadImg;
            this.secImg = secImg;
            this.nubShadImg = nubShadImg;
            this.nubImg = nubImg;
            this.xoff = xoff;
            this.yoff = yoff;
            this.scale = scale;
        }
        
        public void paint(Graphics2D g, Object o, int w, int h) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);
            g.translate(xoff,yoff);
            g.scale(scale,scale);
            
            g.rotate(+Math.PI * 2 / 12 * hour);
            g.drawImage(hourShadImg, -hourShadImg.getWidth() / 2 + 1, -hourShadImg.getHeight() + 1, null);
            g.drawImage(hourImg, -hourImg.getWidth() / 2, -hourImg.getHeight(), null);
            g.rotate(-Math.PI * 2 / 12 * hour);
            
            g.rotate(+Math.PI * 2 / 60 * min);
            g.drawImage(minShadImg, -minShadImg.getWidth() / 2 + 1, -minShadImg.getHeight() + 1, null);
            g.drawImage(minImg, -minImg.getWidth() / 2, -minImg.getHeight(), null);
            g.rotate(-Math.PI * 2 / 60 * min);
            
            g.rotate(+Math.PI * 2 / 60 * sec);
            g.drawImage(secShadImg, -secShadImg.getWidth() / 2 + 0, -secShadImg.getHeight() + 0, null);
            g.drawImage(secImg, -secImg.getWidth() / 2, -secImg.getHeight(), null);
            g.rotate(-Math.PI * 2 / 60 * sec);
            
            g.drawImage(nubShadImg, -nubShadImg.getWidth() / 2 + 0, -nubShadImg.getHeight() / 2 + 0, null);
            g.drawImage(nubImg, -nubImg.getWidth() / 2, -nubImg.getHeight() / 2, null);
        }
    }
    
    
    
}
