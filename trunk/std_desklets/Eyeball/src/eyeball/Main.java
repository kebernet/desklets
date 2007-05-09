/*
 * Main.java
 *
 * Created on February 10, 2007, 12:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eyeball;

import org.glossitope.desklet.test.DeskletTester;
import java.awt.geom.Point2D;

/**
 *
 * @author joshy
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DeskletTester.start(EyeballDesklet.class);
        /*
        // TODO code application logic here
        JFrame frame = new JFrame("A Frame");
        final JXPanel panel = new JXPanel();
        panel.setPreferredSize(new Dimension(300,300));
        
        panel.setBackgroundPainter(new CompoundPainter(
                new CompoundPainter(true, new EyeballPainter()),
                new PupilPainter()));
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    panel.repaint();
                }
            }
        }).start();*/
    }
    
    public static double calcAngle(Point2D p1, Point2D p2) {
        double x_off = p2.getX() - p1.getX();
        double y_off = p2.getY() - p1.getY();
        double angle = Math.atan(y_off / x_off);
        if (x_off < 0) {
            angle = angle + Math.PI;
        }
        
        if(angle < 0) { angle+= 2*Math.PI; }
        if(angle > 2*Math.PI) { angle -= 2*Math.PI; }
        return angle;
    }
/*    
    public void more() {
        for (int i = -size; i <= size; i++) {
            for (int j = -size; j <= size; j++) {
                double distance = i * i + j * j;
                float alpha = opacity;
                if (distance > 0.0d) {
                    alpha = (float) (1.0f / ((distance * size) * opacity));
                }
                alpha *= preAlpha;
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                }
                g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
                g2.drawString(title, i + size, j + size + ascent);
            }
        }
    }
  */  


}
