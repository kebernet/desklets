/*
 * WeatherDialog.java
 *
 * Created on June 5, 2006, 5:49 PM
 */

package weatherdesklet;
import java.awt.Component;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.joshy.util.u;
import org.joshy.weather.WeatherFactory;

/**
 *
 * @author  joshy
 */
public class WeatherDialog extends javax.swing.JDialog {
    WeatherFactory fact = WeatherFactory.newInstance();
    public String selectedStation = "KATL";
    
    /** Creates new form WeatherDialog */
    public WeatherDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        try {
            List regionList;
            regionList = fact.getRegions();
            regions.setModel(new DefaultComboBoxModel(new Vector(regionList)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        stations.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList jList, Object object, int i, boolean b, boolean b0) {
                JLabel label = (JLabel)super.getListCellRendererComponent(jList,object,i,b,b0);
                ListModel model = jList.getModel();
                if(model instanceof MapComboBoxModel) {
                    Map map = ((MapComboBoxModel)jList.getModel()).getMap();
                    label.setText((String) map.get(object));
                }
                return label;
            }
        });
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        regions = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        stations = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setText("Region:");

        regions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        regions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regionsActionPerformed(evt);
            }
        });

        jLabel2.setText("Station:");

        stations.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Done");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(regions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(stations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        selectedStation = stations.getSelectedItem().toString();
        setVisible(false);
// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void regionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regionsActionPerformed
        try {
            Map stationMap = fact.getStations(regions.getSelectedItem()+"");
            stations.setModel(new MapComboBoxModel(stationMap));
            stations.repaint();
            pack();
        } catch (Exception ex) {
            u.p(ex);
        }
    }//GEN-LAST:event_regionsActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox regions;
    private javax.swing.JComboBox stations;
    // End of variables declaration//GEN-END:variables
    
}
