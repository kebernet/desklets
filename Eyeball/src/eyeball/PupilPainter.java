package eyeball;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.painter.AbstractPainter;


class PupilPainter extends AbstractPainter<JPanel> {
    private Point mouse;
    public PupilPainter() {
        mouse = new Point(0,0);
    }
    public void setMouseLocation(Point pt) {
        this.mouse = pt;
    }

    protected void doPaint(Graphics2D gfx, JPanel component, int width, int height) {
        Graphics2D g = (Graphics2D) gfx.create();
        g.setPaint(Color.WHITE);
        Point pt = mouse;//MouseInfo.getPointerInfo().getLocation();
        Point pt2 = new Point(width / 2, height / 2);
        //SwingUtilities.convertPointToScreen(pt2, component);
        double dist = pt.distance(pt2);
        double ang = Main.calcAngle(pt, pt2);
        g = (Graphics2D) g.create();
        g.translate(width / 2, height / 2);
        int centerDist = 20;
        int ovalSize = 50;
        if (dist < 130) {
            g.fillOval(-ovalSize / 2, -ovalSize / 2, ovalSize, ovalSize);
        } else {
            g.rotate(ang + Math.PI * 2 / 4 * 2);
            g.fillOval(centerDist, -ovalSize / 2, 20, ovalSize);
        }
        g.dispose();
        gfx.setColor(Color.BLACK);
        gfx.setStroke(new BasicStroke(0.6f));
        gfx.drawOval(50, 50, 200, 200);
    }
}