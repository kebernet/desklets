package eyeball;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.effects.AbstractAreaEffect;

public class EyeballPainter extends AbstractPainter<JXPanel> {

    protected void doPaint(Graphics2D gfx, JXPanel component, int width, int height) {
        System.out.println("painting");
        gfx.setPaint(Color.WHITE);
        //gfx.fillRect(0, 0, width, height);
        AbstractAreaEffect ispe = new SplineEffect(0.37f, 0, 0.65f, 0);
        ispe.setOffset(new Point(0, 0));
        ispe.setEffectWidth(50);
        ispe.setBrushSteps(50);
        Graphics2D g = (Graphics2D) gfx.create();
        g.setPaint(Color.WHITE);
        Ellipse2D oval1 = new Ellipse2D.Double(50, 50, 200, 200);
        g.fill(oval1);
        ispe.apply(g, oval1, width, height);
        ispe.setEffectWidth(30);
        ispe.setBrushSteps(30);
        g.setPaint(new Color(60, 170, 255));
        Ellipse2D oval2 = new Ellipse2D.Double(80, 80, 140, 140);
        g.fill(oval2);
        ispe.apply(g, oval2, width, height);
        Ellipse2D oval3 = new Ellipse2D.Double(100, 100, 100, 100);
        g.setPaint(Color.BLACK);
        g.fill(oval3);
        g.dispose();
        
        
        
    }
}