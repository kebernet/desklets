package org.glossitope.container.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jdesktop.swingx.util.WindowUtils;


public class BusyDialog extends JFrame {
    private JLabel label;
    private String busyText;
    
    public BusyDialog() {
        label = new JLabel("doing stuff");
        add(label);
        
    }
    
    public void setBusyText(final String text) {
        busyText = text;
        label.setText(busyText + "--");
    }
    
    int count = 0;
    Timer timer = null;
    public void start() {
        
        timer = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String t = "--";
                if(count % 4 == 0) { t = "\\"; }
                if(count % 4 == 1) { t = "|"; }
                if(count % 4 == 2) { t = "/"; }
                if(count % 4 == 3) { t = "-"; }
                label.setText(busyText + t);
                label.repaint();
                count++;
            }
        });
        timer.start();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                pack();
                setLocation(WindowUtils.getPointForCentering(BusyDialog.this));
                setVisible(true);
            }
        });
        
    }
    
    public void stop() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(false);
                if(timer != null) {
                    timer.stop();
                    timer = null;
                }
            }
        });
    }
    
}