/*
 * PermissionManager.java
 *
 * Created on April 25, 2007, 11:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k;

import ab5k.ui.GrantPanel;
import ab5k.ui.PermissionsPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.joshy.util.u;

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
    private List<Window> parents = new ArrayList<Window>();
    
    private synchronized Window getCurrentParent() {
        if(parents.size() < 1) {
            u.p("adding the frame: " + core.getFrame());
            parents.add(core.getFrame());
            return core.getFrame();
        }
        return parents.get(parents.size()-1);
    }
    
    static class PermissionsDialog extends JDialog {
        Permission selectedPermission;
        public PermissionsDialog(Window win, String message) {
            super(win ,"Grant Permission",Dialog.ModalityType.APPLICATION_MODAL);
            JLabel label = new JLabel(message);
            
            JButton deny = new JButton("Deny");
            deny.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent act) {
                    selectedPermission = Permission.NO;
                    setVisible(false);
                }
            });
            JButton allow = new JButton("Allow");
            allow.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent act) {
                    selectedPermission = Permission.YES;
                    setVisible(false);
                }
            });
            JButton always = new JButton("Always");
            always.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent act) {
                    selectedPermission = Permission.ALWAYS;
                    setVisible(false);
                }
            });
            JButton never = new JButton("Never");
            never.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent act) {
                    selectedPermission = Permission.NEVER;
                    setVisible(false);
                }
            });
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(label,"Center");
            JPanel p2 = new JPanel();
            p2.setLayout(new FlowLayout());
            p2.add(never);
            p2.add(deny);
            p2.add(allow);
            p2.add(always);
            panel.add(p2,"South");
            getContentPane().add(panel);            
        }
    }
    
    public Permission requestPermission(String message) {
        if(Environment.useOptionPanePermissions) {
            System.out.println("=== doint here ===");
            Window win = getCurrentParent();
            u.p("curent parent = " + win);
            final PermissionsDialog dialog = new PermissionsDialog(win, message);
            parents.add(dialog);
            u.p("starting");
            dialog.pack();
            dialog.setVisible(true);
            u.p("done waiting");
            parents.remove(dialog);
            return dialog.selectedPermission;
            /*
            String[] options = { "Yes", "No", "Always", "Never" };
            int value = JOptionPane.showOptionDialog(win, message.toString(),
                    "Grant Permission", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            switch(value) {
                case 0: return Permission.YES;
                case 2: return Permission.ALWAYS;
                case 3: return Permission.NEVER;
                default: return Permission.NO;
            }*/
        } else {
            setupDialog();
            return requestPermissionOnEDT(message);
        }
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
