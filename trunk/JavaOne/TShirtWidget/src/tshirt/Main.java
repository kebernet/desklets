/*
 * Main.java
 *
 * Created on April 25, 2007, 3:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tshirt;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContainer;
import ab5k.desklet.test.DeskletTester;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author joshy
 */
public class Main extends Desklet {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DeskletTester.start(Main.class);
    }
    
    public void init() throws Exception {
        URL url = getClass().getResource("TShirt.png");
        BufferedImage img = ImageIO.read(url);
        Icon icon = new ImageIcon(img);
        JLabel btn = new JLabel(icon);
        //btn.setBorderPainted(false);
        //btn.setOpaque(false);
        DeskletContainer c = getContext().getContainer();
        c.setContent(btn);
        c.setShaped(true);
        c.setBackgroundDraggable(true);
        //c.pack();
        //c.setVisible(true);
    }
    
    public void start() throws Exception {
    }
    
    public void stop() throws Exception {
        getContext().notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
}
