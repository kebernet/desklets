package ab5k.wm.buffered;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joshy.util.u;


public class DeskletToplevel extends JPanel {

    private BufferedDeskletContainer container;
    DeskletToplevel(BufferedDeskletContainer container) {
        setOpaque(false);
        this.container = container;
        this.setDoubleBuffered(false);
    }
    protected void paintComponent(Graphics g) {
        //g.setColor(Color.GREEN);
        //g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
        //g.setColor(Color.RED);
        //g.drawString("desklet top level",5,15);
    }
    private void printFlags(Component c) {
        try {
            java.lang.reflect.Method m = JComponent.class.getDeclaredMethod("getFlag", java.lang.Integer.TYPE);
            m.setAccessible(true);
            u.p("ancestor using buffer = " + m.invoke(c, new java.lang.Integer(1)));
            u.p("is painting tile = " + m.invoke(c, new java.lang.Integer(2)));
        } catch (Throwable ex) {
            u.p(ex);
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    BufferedDeskletContainer getContainer() {
        return container;
    }
}