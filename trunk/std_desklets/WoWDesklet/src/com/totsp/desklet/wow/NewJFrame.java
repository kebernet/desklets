/*
 * NewJFrame.java
 *
 * Created on February 19, 2007, 7:14 PM
 */

package com.totsp.desklet.wow;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  cooper
 */
public class NewJFrame extends javax.swing.JFrame {
    
    /** Creates new form NewJFrame */
    public NewJFrame() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 133, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception{
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });*/
        String serverName = "Blackhand";
        Pattern p = Pattern.compile( "(up|down)arrow\\.gif\" [^>]+><\\/td>\\s+<td [^>]+><b [^>]+>"+serverName, Pattern.MULTILINE);
        String statusPage = StreamUtility.readStreamAsString( new URL( "http://www.worldofwarcraft.com/realmstatus/compat.html").openStream());
        statusPage = statusPage.replaceAll("\r", "" ).replaceAll("\n", "");
    // System.out.println(statusPage);
        Matcher m = p.matcher( statusPage );
        System.out.println( m.find() );
        String status = m.group();
        System.out.println( status );
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
