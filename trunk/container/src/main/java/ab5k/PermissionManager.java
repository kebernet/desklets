/*
 * PermissionManager.java
 *
 * Created on April 25, 2007, 11:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k;

import java.awt.Dialog;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import ab5k.ui.GrantPanel;
import ab5k.ui.PermissionsPanel;

/**
 *
 * @author joshy
 */
public class PermissionManager {
    
    public enum Permission {YES, NO, ALWAYS, NEVER}
    private Core core;
    private PermissionsPanel panel = null;
    private JDialog dialog = null;
    
    /** Creates a new instance of PermissionManager */
    public PermissionManager(Core core) {
        this.core = core;
    }
    
    private synchronized void setupDialog() {
        if(panel == null) {
            dialog = new JDialog(core.getFrame(),"Grant Permission", Dialog.ModalityType.DOCUMENT_MODAL);
            panel = new PermissionsPanel(dialog);
            dialog.getContentPane().add(panel);
        }
    }
    
    public Permission requestPermission(String message) {
        setupDialog();
        return requestPermissionOnEDT(message);
        //String[] options = { "Yes", "No", "Always", "Never" };
        /*
        Window win = core.getFrame();
        int value = JOptionPane.showOptionDialog(win, message.toString(),
                "Grant Permission", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        switch(value) {
        case 0: return Permission.YES;
        case 2: return Permission.ALWAYS;
        case 3: return Permission.NEVER;
        default: return Permission.NO;
        }*/
    }
    
    private Permission requestPermissionOnEDT(final String message) {
        final GrantPanel grant = new GrantPanel(message,panel);
        //u.p("thread = " + Thread.currentThread().getName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                panel.addGrant(grant);
                if (!dialog.isVisible()) {
                    dialog.pack();
                    dialog.setSize(500,300);
                    dialog.setVisible(true);
                }
            }
        });
        
        synchronized(grant) {
            //u.p("waiting first for : " + message);
            while(!grant.isAnswered()) {
                //u.p("waiting for: " + message);
                try         {
                    grant.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
        }
        //u.p("answered for: " + message);
        return grant.getSelectedPermission();
    }
    
    
}
