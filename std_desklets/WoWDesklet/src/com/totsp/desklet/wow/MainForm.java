/*
 * MainForm.java
 *
 * Created on February 19, 2007, 6:51 PM
 */

package com.totsp.desklet.wow;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.jdesktop.swingx.painter.ImagePainter;

/**
 *
 * @author  cooper
 */
public class MainForm extends javax.swing.JPanel {
    
    private BufferedImage background;
    
    
    /** Creates new form MainForm */
    public MainForm() {
        try{
            background = ImageIO.read( getClass().getResource("/com/totsp/desklet/wow/Default.png"));
        } catch(Exception e){
            e.printStackTrace();
        }
        initComponents();
    }
    
   
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel2 = new javax.swing.JLabel();
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        serverStatus = new javax.swing.JLabel();
        serverName = new javax.swing.JComboBox();

        jLabel2.setText("jLabel2");

        setOpaque(false);
        jXPanel1.setBackgroundPainter(new ImagePainter( background ));
        jXPanel1.setOpaque(false);
        serverStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/wow/uparrow.gif")));

        serverName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        serverName.setOpaque(false);

        org.jdesktop.layout.GroupLayout jXPanel1Layout = new org.jdesktop.layout.GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(
            jXPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jXPanel1Layout.createSequentialGroup()
                .addContainerGap(157, Short.MAX_VALUE)
                .add(serverStatus)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(serverName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(30, 30, 30))
        );
        jXPanel1Layout.setVerticalGroup(
            jXPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jXPanel1Layout.createSequentialGroup()
                .addContainerGap(122, Short.MAX_VALUE)
                .add(jXPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(serverName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(serverStatus))
                .add(36, 36, 36))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jXPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jXPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private org.jdesktop.swingx.JXPanel jXPanel1;
    public javax.swing.JComboBox serverName;
    public javax.swing.JLabel serverStatus;
    // End of variables declaration//GEN-END:variables
    
}