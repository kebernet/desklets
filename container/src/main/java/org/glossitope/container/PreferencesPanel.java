/*
 * PreferencesPanel.java
 *
 * Created on July 3, 2006, 1:26 PM
 */

package org.glossitope.container;

import org.glossitope.container.prefs.PrefsBean;
import org.glossitope.container.util.BeanArrayListModel;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.joshy.util.u;

/**
 *
 * @author  joshy
 */
public class PreferencesPanel extends javax.swing.JPanel {
    private Core main;
    
    /** Creates new form PreferencesPanel */
    public PreferencesPanel(Core main) {
        initComponents();
        this.main = main;
        backgroundList.setModel(new BeanArrayListModel(main.getBackgroundManager().getBackgrounds()));
        backgroundList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList jList, Object object, int i, boolean b, boolean b0) {
                JLabel label = (JLabel)super.getListCellRendererComponent(jList, object, i, b, b0);
                if(object instanceof DesktopBackground) {
                    DesktopBackground bg = (DesktopBackground)object;
                    label.setText(bg.getBackgroundName());
                } else {
                    label.setText("Non background: " + object.getClass().getName());
                }
                
                return label;
            }
        });
        backgroundList.setSelectedIndex(0);
        loadFromPrefs();
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        backgroundList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        dockingSideCombo = new javax.swing.JComboBox();
        useMicroDock = new javax.swing.JCheckBox();
        reportUsageCheckbox = new javax.swing.JCheckBox();

        jLabel1.setText("Preferences");

        jCheckBox1.setText("Show in system tray / menulet");
        jCheckBox1.setEnabled(false);
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jCheckBox2.setText("Run in full screen");
        jCheckBox2.setEnabled(false);
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel2.setText("Background:");

        backgroundList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        backgroundList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                backgroundListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(backgroundList);

        jLabel3.setText("Dock side:");

        dockingSideCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Right", "Left" }));
        dockingSideCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dockingSideComboItemStateChanged(evt);
            }
        });

        useMicroDock.setText("micro dock");
        useMicroDock.setMargin(new java.awt.Insets(0, 0, 0, 0));
        useMicroDock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useMicroDockActionPerformed(evt);
            }
        });

        reportUsageCheckbox.setText("report usage");
        reportUsageCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        reportUsageCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportUsageCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dockingSideCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(86, 86, 86)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(reportUsageCheckbox)
                                    .addComponent(useMicroDock))))
                        .addGap(154, 154, 154))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox2)
                        .addContainerGap(418, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(472, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                        .addGap(324, 324, 324))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dockingSideCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(useMicroDock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reportUsageCheckbox)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void reportUsageCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportUsageCheckboxActionPerformed
        main.getLoginToServerAction().setShouldLogin(reportUsageCheckbox.isSelected());
        main.getPrefsBean().setProperty(PrefsBean.TRACKINGENABLED, reportUsageCheckbox.isSelected());
    }//GEN-LAST:event_reportUsageCheckboxActionPerformed
    
    private void useMicroDockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useMicroDockActionPerformed
        main.getCloser().setMicrodocking(useMicroDock.isSelected());
        main.getPrefsBean().setProperty(PrefsBean.MICRODOCKING, useMicroDock.isSelected());
    }//GEN-LAST:event_useMicroDockActionPerformed
    
    private void dockingSideComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dockingSideComboItemStateChanged
        u.p("docking changed to : " + evt.getItem());
        if("Right".equals(evt.getItem())) {
            main.getMainPanel().setDockingSide(MainPanel.DockingSide.Right);
        } else {
            main.getMainPanel().setDockingSide(MainPanel.DockingSide.Left);
        }
        main.getPrefsBean().setProperty(PrefsBean.DOCKINGSIDE,
                main.getMainPanel().getDockingSide().toString());
    }//GEN-LAST:event_dockingSideComboItemStateChanged
    
    private void backgroundListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_backgroundListValueChanged
        if(!evt.getValueIsAdjusting()) {
            u.p("updating the background");// TODO add your handling code here:
            int n = backgroundList.getSelectedIndex();
            u.p("n = " + n);
            Object bg = main.getBackgroundManager().getBackgrounds().get(n);
            u.p("bg = " + bg);
            main.getBackgroundManager().setDesktopBackground((DesktopBackground)bg);
        }
    }//GEN-LAST:event_backgroundListValueChanged

    private void loadFromPrefs() {
        useMicroDock.setSelected(main.getPrefsBean().getBoolean(PrefsBean.MICRODOCKING,false));
        dockingSideCombo.setSelectedItem(main.getPrefsBean().getString(PrefsBean.DOCKINGSIDE,"Right"));
        reportUsageCheckbox.setSelected(main.getPrefsBean().getBoolean(PrefsBean.TRACKINGENABLED,true));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList backgroundList;
    private javax.swing.JComboBox dockingSideCombo;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox reportUsageCheckbox;
    private javax.swing.JCheckBox useMicroDock;
    // End of variables declaration//GEN-END:variables
    
}
