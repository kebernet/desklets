package ab5k.wm.buffered;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;


public class DeskletToplevel extends JPanel {

    private BufferedDeskletContainer container;
    DeskletToplevel(BufferedDeskletContainer container) {
        setOpaque(false);
        this.container = container;
    }
    protected void paintComponent(Graphics g) {
        //g.setColor(Color.GREEN);
        //g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    BufferedDeskletContainer getContainer() {
        return container;
    }
}