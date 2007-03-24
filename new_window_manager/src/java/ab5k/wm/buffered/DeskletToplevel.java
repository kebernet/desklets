package ab5k.wm;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;


class DeskletToplevel extends JPanel {
    DeskletToplevel() {
        setOpaque(false);
    }
    protected void paintComponent(Graphics g) {
        //g.setColor(Color.GREEN);
        //g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}