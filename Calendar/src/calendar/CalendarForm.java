/*
 * CalendarForm.java
 *
 * Created on February 13, 2007, 12:30 PM
 */

package calendar;

import java.awt.Color;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.joda.time.DateTime;

/**
 *
 * @author  joshy
 */
public class CalendarForm extends javax.swing.JPanel {
    
    private DateTime date = new DateTime();
    
    /** Creates new form CalendarForm */
    public CalendarForm() {
        initComponents();
        updateDate();
        
        RectanglePainter rect = new RectanglePainter(new Color(150,0,0), Color.BLACK);
        rect.setRounded(true);
        rect.setRoundHeight(10);
        rect.setRoundWidth(10);
        rect.setInsets(new Insets(3,3,3,3));
        rect.setStyle(RectanglePainter.Style.FILLED);
        
        ((JXPanel)leftPanel).setBackgroundPainter(rect);
        ((JXPanel)rightPanel).setBackgroundPainter(rect);
        
        JXPanel panel = (JXPanel)jPanel1;
        panel.setBackgroundPainter(new CompoundPainter(
                //new MattePainter(Color.WHITE),
                new MonthPainter(this)
                ));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        leftPanel = new JXPanel();
        jPanel1 = new JXPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        month = new javax.swing.JLabel();
        rightPanel = new JXPanel();
        dayOfWeek = new javax.swing.JLabel();
        dayOfMonth = new javax.swing.JLabel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        setBackground(new java.awt.Color(0, 0, 0));
        setOpaque(false);
        jPanel1.setOpaque(false);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("<");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(">");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        month.setForeground(new java.awt.Color(255, 255, 255));
        month.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        month.setText("Month");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(month, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        add(leftPanel);

        dayOfWeek.setFont(new java.awt.Font("Lucida Grande", 0, 24));
        dayOfWeek.setForeground(new java.awt.Color(255, 255, 255));
        dayOfWeek.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayOfWeek.setText("Wednesday");

        dayOfMonth.setFont(new java.awt.Font("Lucida Grande", 0, 100));
        dayOfMonth.setForeground(new java.awt.Color(255, 255, 255));
        dayOfMonth.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayOfMonth.setText("33");

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dayOfWeek, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(dayOfMonth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(dayOfWeek)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dayOfMonth, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
        );
        add(rightPanel);

    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setDate(getDate().plusMonths(1));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setDate(getDate().minusMonths(1));
        
    }//GEN-LAST:event_jButton1ActionPerformed

    public void updateDate() {
        setDate(new DateTime(new Date()));
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
        month.setText(date.monthOfYear().getAsText() + " " +
                date.year().getAsText());
        dayOfWeek.setText(date.dayOfWeek().getAsText());//dayOfWeekFormat.format(date));
        dayOfMonth.setText(date.dayOfMonth().getAsText());//dayOfMonthFormat.format(date));
        jPanel1.repaint();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dayOfMonth;
    private javax.swing.JLabel dayOfWeek;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel month;
    private javax.swing.JPanel rightPanel;
    // End of variables declaration//GEN-END:variables


    
}
