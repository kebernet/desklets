/*
 * SecurityPolicy.java
 *
 * Created on August 3, 2006, 4:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ab5k.security;

import java.awt.Window;

import java.io.FilePermission;

import java.net.URL;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


/**
 *
 * @author cooper
 */
public class SecurityPolicy extends Policy {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private HashMap<String, ArrayList<String>> always;
    private HashMap<String, ArrayList<String>> nevers;
    private HashMap<CodeSource, Permissions> permissions = new HashMap<CodeSource, Permissions>();
    private Preferences prefs = Preferences.userNodeForPackage(SecurityPolicy.class);

    /** Creates a new instance of SecurityPolicy */
    public SecurityPolicy() {
        super();
        always = this.deserializeRemembered("always");
        nevers = this.deserializeRemembered("never");
    }

    HashMap<String, ArrayList<String>> deserializeRemembered(String mode) {
        StringTokenizer urls = new StringTokenizer(prefs.get(mode + "-urls", ""),
                "\n");
        HashMap<String, ArrayList<String>> perms = new HashMap<String, ArrayList<String>>();

        while(urls.hasMoreTokens()) {
            try {
                StringTokenizer idUrl = new StringTokenizer(urls.nextToken(),
                        "\t");
                String id = idUrl.nextToken();
                String url = idUrl.nextToken();
                StringTokenizer entries = new StringTokenizer(prefs.get(mode +
                            "-" + id, ""), "\n");
                LOG.info("Deserializing for: " + mode + " " + id + " -- " +
                    url);
                LOG.info(prefs.get(mode + "-" + id, ""));

                ArrayList<String> grants = new ArrayList<String>();

                while(entries.hasMoreTokens()) {
                    String next = entries.nextToken();

                    if(!grants.contains(next)) {
                        grants.add(next);
                    }
                }

                perms.put(url, grants);
            } catch(Exception e) {
                LOG.log(Level.WARNING, "Exception loading Grant preferences.", e);

                continue;
            }
        }

        return perms;
    }

    public PermissionCollection getPermissions(CodeSource codeSource) {
        Permissions p = permissions.get(codeSource);

        if(p == null) {
            p = new Permissions();
            permissions.put(codeSource, p);
        }

        return p;
    }

    public boolean implies(ProtectionDomain protectionDomain,
        Permission permission) {
        LOG.finer(protectionDomain.getCodeSource().getLocation() +
            " requesting permission " + permission.getClass().getName() + " " +
            permission.getName());

        if(permission instanceof java.awt.AWTPermission ||
                permission instanceof java.util.PropertyPermission) {
            return true;
        }

        if(permission instanceof ab5k.security.DeskletAdministrationPermission) {
            return (protectionDomain.getClassLoader() == SecurityPolicy.class.getClassLoader());
        }

        if(protectionDomain.getClassLoader() instanceof ClassLoader) {
            ClassLoader loader = (ClassLoader) protectionDomain.getClassLoader();
            Permissions perms = permissions.get(protectionDomain.getCodeSource());

            if(perms == null) {
                perms = new Permissions();
            }

            if(perms.implies(permission)) {
                return true;
            }

            if(permission instanceof FilePermission && (Registry.HOME != null) &&
                    (permission.getName()
                                   .startsWith(Registry.HOME.getAbsolutePath()) ||
                    permission.getName()
                                  .startsWith(System.getProperty(
                            "java.io.tmpdir")))) {
                perms.add(permission);

                return true;
            }

            ArrayList<String> grants = always.get(protectionDomain.getCodeSource()
                                                                  .getLocation()
                                                                  .toExternalForm());

            if(grants == null) {
                grants = new ArrayList<String>();
            }

            if(grants.contains(permission.getName())) {
                return true;
            }

            ArrayList<String> denies = nevers.get(protectionDomain.getCodeSource()
                                                                  .getLocation()
                                                                  .toExternalForm());

            if(denies == null) {
                denies = new ArrayList<String>();
            }

            if(denies.contains(permission.getName())) {
                return false;
            }

            StringBuffer sb = new StringBuffer();
            sb.append(loader.getName());
            sb.append(" has asked for permission " + permission.getName());
            sb.append(". \n Would you like to grant this permission?");

            String[] options = { "Yes", "No", "Always", "Never" };
            Window win = SwingUtilities.windowForComponent(DeskletManager.main.getDesktop());
            int value = JOptionPane.showOptionDialog(win, sb.toString(),
                    "Grant Permission", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch(value) {
            case 0:
                perms.add(permission);
                permissions.put(protectionDomain.getCodeSource(), perms);

                return true;

            case 2:
                perms.add(permission);
                permissions.put(protectionDomain.getCodeSource(), perms);

                if(!grants.contains(permission.getName())) {
                    grants.add(permission.getName());
                }

                always.put(protectionDomain.getCodeSource().getLocation()
                                           .toExternalForm(), grants);
                this.storeRemembered(always, "always");

                try {
                    prefs.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                return true;

            case 3:

                if(!denies.contains(permission.getName())) {
                    denies.add(permission.getName());
                }

                nevers.put(protectionDomain.getCodeSource().getLocation()
                                           .toExternalForm(), denies);
                storeRemembered(nevers, "never");

                try {
                    prefs.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                return false;

            default:
                return false;
            }
        } else {
            return true;
        }
    }

    public void refresh() {
        ; //
    }

    void storeRemembered(HashMap<String, ArrayList<String>> perms, String mode) {
        Iterator<Entry<String, ArrayList<String>>> uit = perms.entrySet()
                                                              .iterator();
        StringBuffer urls = new StringBuffer();

        for(int i = 0; uit.hasNext(); i++) {
            StringBuffer sb = new StringBuffer();
            Entry<String, ArrayList<String>> entry = uit.next();
            String source = entry.getKey();
            ArrayList<String> entries = entry.getValue();
            Iterator<String> it = entries.iterator();

            while(it.hasNext()) {
                sb.append(it.next());
                sb.append("\n");
            }

            LOG.info("Serializing for:  " + mode + " " + i + " -- " + source);
            LOG.info(sb.toString());
            prefs.remove(mode + "-" + i);
            prefs.put(mode + "-" + i, sb.toString());
            urls.append(i + "\t" + source);

            if(uit.hasNext()) {
                urls.append("\n");
            }
        }

        prefs.put(mode + "-urls", urls.toString());
    }
}
