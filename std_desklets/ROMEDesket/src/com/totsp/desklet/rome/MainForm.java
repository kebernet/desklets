/*
 * MainForm.java
 *
 * Created on March 2, 2007, 9:16 PM
 */

package com.totsp.desklet.rome;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 *
 * @author  cooper
 */
public class MainForm extends JXPanel {
    BufferedImage background;
    XHTMLPanel display = new XHTMLPanel();
    /** Creates new form MainForm */
    public MainForm() {
        try{
            background = ImageIO.read( getClass().getResource("/com/totsp/desklet/rome/Background.png"));
        } catch(Exception e){
            e.printStackTrace();
        }
        this.setBackgroundPainter(new ImagePainter( background ));
        initComponents();
        display.setOpaque( false );
        display.setDocument( getClass().getResource("/com/totsp/desklet/rome/temp.xml").toExternalForm());
        display.setForeground( Color.WHITE );
        this.jPanel1.add( display );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        now = new javax.swing.JLabel();
        now1 = new javax.swing.JLabel();
        now2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        browse = new javax.swing.JButton();
        feed = new javax.swing.JButton();
        back = new javax.swing.JButton();
        pause = new javax.swing.JButton();
        forward = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(325, 450));
        setMinimumSize(new java.awt.Dimension(325, 450));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/rome/romepower02-tiny.png")));

        now.setForeground(new java.awt.Color(255, 255, 255));
        now.setText("current");

        now1.setForeground(new java.awt.Color(153, 153, 153));
        now1.setText("prev");

        now2.setForeground(new java.awt.Color(51, 51, 51));
        now2.setText("prevprev");

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setOpaque(false);

        browse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/rome/browse.gif")));
        browse.setOpaque(false);

        feed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/rome/rssicon.png")));
        feed.setOpaque(false);

        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/rome/backward.png")));
        back.setOpaque(false);

        pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/rome/pause.png")));
        pause.setOpaque(false);

        forward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/totsp/desklet/rome/forward.png")));
        forward.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(now2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(now1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(now, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pause, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forward, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(feed, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(browse, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(now2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(now1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(now)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browse)
                    .addComponent(feed)
                    .addComponent(forward, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pause, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton back;
    public javax.swing.JButton browse;
    public javax.swing.JButton feed;
    public javax.swing.JButton forward;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JLabel now;
    public javax.swing.JLabel now1;
    public javax.swing.JLabel now2;
    public javax.swing.JButton pause;
    // End of variables declaration//GEN-END:variables
    
}