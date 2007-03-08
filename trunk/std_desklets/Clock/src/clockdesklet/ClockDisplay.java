/*
 * ClockDesklet.java
 *
 * Created on May 30, 2006, 4:52 PM
 */

package clockdesklet;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;
import org.jdesktop.swingx.util.PaintUtils;
/**
 * @author  joshy
 */
public class ClockDisplay extends JXPanel {
    
    /** Creates new form ClockDesklet */
    public ClockDisplay() {
        initComponents();
        
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
        clip.setSnapPaint(true);
        
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

        jButton1.setText("setup");
        jButton1.setOpaque(false);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(amLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pmLabel))
                    .addComponent(jButton1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1)
                .addComponent(currentTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amLabel)
                    .addComponent(pmLabel)))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel amLabel;
    public org.jdesktop.swingx.JXLabel currentTime;
    private javax.swing.JButton jButton1;
    public javax.swing.JLabel pmLabel;
    // End of variables declaration//GEN-END:variables
    
}
