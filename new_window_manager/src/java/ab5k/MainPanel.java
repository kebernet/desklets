/*
 * MainPanel.java
 *
 * Created on August 21, 2006, 8:21 PM
 */

package ab5k;

import ab5k.security.DockSkinner;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Date;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.xml.stream.events.StartDocument;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.PinstripePainter;
import org.joshy.util.u;

/**
 *
 * @author  joshy
 */
public class MainPanel extends javax.swing.JPanel {
    public enum DockingSide { Left, Right };
    private DockingSide side = DockingSide.Right;
    public Core main;
    
    
    /** Creates new form MainPanel */
    public MainPanel() {
        this(null);
    }
    
    public MainPanel(Core main) {
        setMain(main);
        initComponents();
        
        
        logoButton.setText("");
        logoButton.setIcon(new ImageIcon(getClass().getResource("/images/logo 1.png")));
        
        JXButton logo = (JXButton) logoButton;
        logo.setContentAreaFilled(false);
        //GradientPaint grad = new GradientPaint(new Point(0,0), Color.GRAY, new Point(10,0), Color.WHITE);
        //MattePainter bg = new MattePainter(grad);
        //bg.setSnapPaint(true);
        
        //Painter comp = logo.getForegroundPainter();
        //CompoundPainter cp = new CompoundPainter(bg,comp);
        //logo.setForegroundPainter(cp);
        miniModePanel.setLayout(new BoxLayout(miniModePanel,BoxLayout.Y_AXIS));
        
        //stripButtonLeft.addActionListener(main.getCollapseWindowAction());
        //stripButtonRight.addActionListener(main.getCollapseWindowAction());
        logoButton.addActionListener(main.getCollapseWindowAction());
        
        DockSkinner.skinDock(this);
    }
    
    public void setMain(Core main) {
        this.main = main;
    }
    
    public Action getPreferencesAction() {
        return main.getPreferencesAction();
    }
    
    public Action getShowManageDialogAction() {
        return main.getShowManageDialogAction();
    }
    
    public Action getQuitAction() {
        return main.getQuitAction();
    }
    
    public Action getCollapseWindowAction() {
        return main.getCollapseWindowAction();
    }
    
    public DockingSide getDockingSide() {
        return this.side;
    }
    
    public void setDockingSide(DockingSide side) {
        this.side = side;
        main.getWindowManager().setDockingSide(side);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        collapseButton = new javax.swing.JButton();
        dockPanel = new JXPanel();
        manageButton = new javax.swing.JButton();
        logoButton = new JXButton();
        quitButton = new javax.swing.JButton();
        miniModePanel = new javax.swing.JPanel();
        loadingSpinner = new org.jdesktop.swingx.JXBusyLabel();

        collapseButton.setAction(getCollapseWindowAction());
        collapseButton.setText("<");
        collapseButton.setBorder(null);
        collapseButton.setBorderPainted(false);
        collapseButton.setOpaque(false);
        collapseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collapseButtonActionPerformed(evt);
            }
        });

        setLayout(new java.awt.BorderLayout());

        dockPanel.setLayout(new java.awt.GridBagLayout());

        dockPanel.setBackground(new java.awt.Color(102, 255, 102));
        dockPanel.setPreferredSize(new java.awt.Dimension(180, 500));
        dockPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dockPanelMouseClicked(evt);
            }
        });

        manageButton.setAction(getShowManageDialogAction());
        manageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dock/orange/images/configure.png")));
        manageButton.setText("Setup");
        manageButton.setToolTipText("Manage Widgets and Preferences");
        manageButton.setBorder(null);
        manageButton.setBorderPainted(false);
        manageButton.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        dockPanel.add(manageButton, gridBagConstraints);

        logoButton.setBorderPainted(false);
        logoButton.setContentAreaFilled(false);
        logoButton.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        dockPanel.add(logoButton, gridBagConstraints);

        quitButton.setAction(getQuitAction());
        quitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dock/orange/images/close.png")));
        quitButton.setText("Quit");
        quitButton.setToolTipText("Quit");
        quitButton.setBorder(null);
        quitButton.setBorderPainted(false);
        quitButton.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        dockPanel.add(quitButton, gridBagConstraints);

        miniModePanel.setLayout(new java.awt.GridLayout(20, 1));

        miniModePanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 10.0;
        dockPanel.add(miniModePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        dockPanel.add(loadingSpinner, gridBagConstraints);

        add(dockPanel, java.awt.BorderLayout.WEST);

    }// </editor-fold>//GEN-END:initComponents

    private void dockPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dockPanelMouseClicked
        
        main.getCollapseWindowAction().actionPerformed(
                new ActionEvent(this,-1,"toggle"));
        // TODO add your handling code here:
    }//GEN-LAST:event_dockPanelMouseClicked
    
    private void collapseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collapseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_collapseButtonActionPerformed
    
    public int getDockWidth() {
        return dockPanel.getWidth();
    }
    
    public JPanel getDockPanel() {
        return miniModePanel;
    }
    
    public JXBusyLabel getSpinner() {
        return loadingSpinner;
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton collapseButton;
    public javax.swing.JPanel dockPanel;
    private org.jdesktop.swingx.JXBusyLabel loadingSpinner;
    public javax.swing.JButton logoButton;
    private javax.swing.JButton manageButton;
    private javax.swing.JPanel miniModePanel;
    private javax.swing.JButton quitButton;
    // End of variables declaration//GEN-END:variables
    
    
}
