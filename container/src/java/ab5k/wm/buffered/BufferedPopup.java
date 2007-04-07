/*
 * BufferedPopup.java
 *
 * Created on March 31, 2007, 9:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Popup;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class BufferedPopup extends Popup {
    
    private Component contents;
    private Component owner;
    private BufferedWM wm;
    JComponent panel;
    
    /** Creates a new instance of BufferedPopup */
    public BufferedPopup(BufferedWM wm, Component owner, Component contents) {
        u.p("getting popup for: " + owner);
        u.p("contents = " + contents);
        this.wm = wm;
        this.owner = owner;
        this.contents = contents;
    }
    
    public void show() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(contents,"Center");
        panel.add(new JButton("button"),"North");
        panel.setSize(new Dimension(100,200));
        panel.setBorder(BorderFactory.createLineBorder(Color.RED));
        u.p("contents = " + contents);
        Component[] comps = ((Container)contents).getComponents();
        for(Component c : comps) {
            u.p("child = " + c);
        }
        
        DeskletToplevel top = (DeskletToplevel)wm.getTopParent(owner,DeskletToplevel.class);
        top.getContainer().popups.add(this);
        top.getContainer().setDirty(true);
        u.p("showing ");
        this.wm.panel.repaint();
    }
    
    public void hide() {
        u.p("hide");
    }
    
}