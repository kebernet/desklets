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
        try {
            initImages();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        repaint();
    }
    
    void setHex() {
        
        this.setPreferredSize(new Dimension(200,200));
        this.pmLabel.setVisible(false);
        this.currentTime.setVisible(false);
        this.amLabel.setVisible(false);
        
        desklet.getContext().getContainer().pack();
        double scale = 0.4;
        
        
        Painter hands = new HandsPainter(AhourShadImg, AhourImg, AminShadImg, AminImg,
                AsecShadImg, AsecImg, AnubShadImg, AnubImg,
                (int)(63*scale*3.5),(int)(62*scale*3.5),scale*3.5);
        CompoundPainter cp = new CompoundPainter(hface,hbackground,hands);
        this.setBackgroundPainter(cp);
        this.repaint();
        repaint();
    }
    
    void setAnalog() {
        this.setPreferredSize(new Dimension(200,200));
        this.pmLabel.setVisible(false);
        this.currentTime.setVisible(false);
        this.amLabel.setVisible(false);
        desklet.getContext().getContainer().pack();
        double scale = 0.4;
        
        
        Painter hands = new HandsPainter(
                anhourShadImg, anhourImg, anminShadImg, anminImg,
                ansecShadImg, ansecImg, annubShadImg, annubImg,
                (int)(236*scale),(int)(236*scale), 0.7*scale);
        
        CompoundPainter cp1 = new CompoundPainter(
                aborderShadow,
                aborder,
                //abevelShadow,
                //abackgroundShadow,
                //aglassShadow,
                abevel,
                abackground,
                aticks,
                adigits);
        CompoundPainter cp2 = new CompoundPainter(
                aglassColor,
                aglassShine);
        CompoundPainter cp3 = new CompoundPainter(cp1,hands,cp2);
        cp3.setCacheable(false);
        
        this.setBackgroundPainter(cp3);
        this.repaint();
        repaint();
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
    
    BufferedImage AhourImg;
    BufferedImage AhourShadImg;
    BufferedImage AminImg;
    BufferedImage AminShadImg;
    BufferedImage AsecImg;
    BufferedImage AsecShadImg;
    BufferedImage AnubImg;
    BufferedImage AnubShadImg;
    
    BufferedImage anhourImg;
    BufferedImage anhourShadImg;
    BufferedImage anminImg;
    BufferedImage anminShadImg;
    BufferedImage ansecImg;
    BufferedImage ansecShadImg;
    BufferedImage annubImg;
    BufferedImage annubShadImg;
    ImagePainter abackground;
    ImagePainter aticks;
    ImagePainter abackgroundShadow;
    ImagePainter abevel;
    ImagePainter abevelShadow;
    
    ImagePainter adigits;
    ImagePainter aglassColor;
    ImagePainter aglassShadow;
    ImagePainter aglassShine;
    ImagePainter agradient;
    ImagePainter aborder;
    ImagePainter aborderShadow;
    
    ImagePainter hbackground;
    ImagePainter hface;
    
    private void initImages() throws IOException {
        AhourImg = ImageIO.read(getClass().getResource("images/HexClock/Hour.png"));
        AhourShadImg = ImageIO.read(getClass().getResource("images/HexClock/Hour Shadow.png"));
        AminImg = ImageIO.read(getClass().getResource("images/HexClock/Minute.png"));
        AminShadImg = ImageIO.read(getClass().getResource("images/HexClock/Minute Shadow.png"));
        AsecImg = ImageIO.read(getClass().getResource("images/HexClock/Second.png"));
        AsecShadImg = ImageIO.read(getClass().getResource("images/HexClock/Second Shadow.png"));
        AnubImg = ImageIO.read(getClass().getResource("images/HexClock/Nub.png"));
        AnubShadImg = ImageIO.read(getClass().getResource("images/HexClock/Nub Shadow.png"));
        anhourImg = ImageIO.read(getClass().getResource("images/Analog/Hands/HourHand.png"));
        anhourShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/HourHandShadow.png"));
        anminImg = ImageIO.read(getClass().getResource("images/Analog/Hands/MinuteHand.png"));
        anminShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/MinuteHandShadow.png"));
        ansecImg = ImageIO.read(getClass().getResource("images/Analog/Hands/SecondHand.png"));
        ansecShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/SecondHandShadow.png"));
        annubImg = ImageIO.read(getClass().getResource("images/Analog/Hands/Cap.png"));
        annubShadImg = ImageIO.read(getClass().getResource("images/Analog/Hands/CapShadow.png"));
        
        double scale = 0.4;
         abackground = loadImagePainter("Analog/Base/Background.png",scale);
        aticks = loadImagePainter("Analog/Base/Ticks.png",scale);
        abackgroundShadow = loadImagePainter("Analog/Base/BackgroundShadow.png",scale);
        abevel = loadImagePainter("Analog/Base/Bevel.png",scale);
        abevelShadow = loadImagePainter("Analog/Base/BevelShadow.png",scale);
        
        adigits = loadImagePainter("Analog/Base/Digits.png",scale);
        aglassColor = loadImagePainter("Analog/Base/GlassColor.png",scale);
        aglassShadow = loadImagePainter("Analog/Base/GlassShadow.png",scale);
        aglassShine = loadImagePainter("Analog/Base/GlassShine.png",scale);
        agradient = loadImagePainter("Analog/Base/Gradient.png",scale);
        aborder = loadImagePainter("Analog/Base/Border.png",scale);
        aborderShadow = loadImagePainter("Analog/Base/BorderShadow.png",scale);
        
        
        hbackground = loadImagePainter("HexClock/Background.png",scale);
        hface = loadImagePainter("HexClock/Face.png",scale);
        hface.setInsets(new Insets((int)(32*scale),(int)(40*scale),0,0));
    }
    
    private class HandsPainter extends AbstractPainter {
        
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
        
        protected void doPaint(Graphics2D g, Object object, int w, int h) {
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
