/*
 * AboutPanel.java
 *
 * Created on February 19, 2007, 3:29 PM
 */

package org.glossitope.container;

import java.util.Properties;

/**
 *
 * @author  joshy
 */
public class AboutPanel extends javax.swing.JPanel {
    
    /** Creates new form AboutPanel */
    public AboutPanel() {
        initComponents();
    }
    public AboutPanel(Core main) {
        this.main = main;
        initComponents();
        try {
            Properties props = new Properties();
            props.load(AboutPanel.class.getResourceAsStream("/build.properties"));
            System.out.println("got keys:" + props.keySet());
            this.buildNumberLabel.setText(props.getProperty("build.number"));
            this.buildDateLabel.setText(props.getProperty("build.time"));
            this.buildTimestampLabel.setText(props.getProperty("build.timestamp"));
              
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buildNumberLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        buildTimestampLabel = new javax.swing.JLabel();
        buildDateLabel = new javax.swing.JLabel();

        jLabel1.setText("org.glossitope : Widgets for the World");

        jLabel2.setText("Build number: ");

        buildNumberLabel.setText("jLabel3");

        jLabel3.setText("Build date:");

        jLabel4.setText("Build timestamp:");

        buildTimestampLabel.setText("jLabel5");

        buildDateLabel.setText("jLabel6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buildNumberLabel)
                            .addComponent(buildDateLabel)
                            .addComponent(buildTimestampLabel))))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(buildNumberLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(buildDateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(buildTimestampLabel))
                .addContainerGap(192, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel buildDateLabel;
    private javax.swing.JLabel buildNumberLabel;
    private javax.swing.JLabel buildTimestampLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
    
    private Core main;
    
}
