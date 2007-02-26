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
    private HashMap<CodeSource, Permissions> permissions = new HashMap<CodeSource, Permissions>();
    private HashMap<URL, ArrayList<String>> nevers;
    private HashMap<URL, ArrayList<String>> always;
    private Preferences prefs = Preferences.userNodeForPackage(SecurityPolicy.class);
    
    /** Creates a new instance of SecurityPolicy */
    public SecurityPolicy() {
        super();
        always = this.deserializeRemembered("always");
        nevers = this.deserializeRemembered("never");
    }
    
    public boolean implies(ProtectionDomain protectionDomain,
            Permission permission) {
        LOG.finer(protectionDomain.getCodeSource().getLocation() +
                " requesting permission " + permission.getClass().getName() + " " +
                permission.getName());
        
        if (permission instanceof java.awt.AWTPermission ||
                permission instanceof java.util.PropertyPermission) {
            return true;
        }
        
        if( permission instanceof ab5k.security.DeskletAdministrationPermission ){
            return (protectionDomain.getClassLoader() == SecurityPolicy.class.getClassLoader() );
        }
        
        if (protectionDomain.getClassLoader() instanceof ClassLoader) {
            ClassLoader loader = (ClassLoader) protectionDomain.getClassLoader();
            Permissions perms = permissions.get(protectionDomain.getCodeSource());
            
            if (perms == null) {
                perms = new Permissions();
            }
            
            if (perms.implies(permission)) {
                return true;
            }
            
            if (permission instanceof FilePermission &&
                    (Registry.HOME != null) &&
                    (permission.getName()
                    .startsWith(Registry.HOME.getAbsolutePath())
                    ||
                    permission.getName().startsWith( System.getProperty("java.io.tmpdir") )
                    ) ) {
                perms.add(permission);
                
                return true;
            }
            
            ArrayList<String> grants = always.get(protectionDomain.getCodeSource()
            .getLocation());
            
            if (grants == null) {
                grants = new ArrayList<String>();
            }
            
            if (grants.contains(permission.getName())) {
                return true;
            }
            
            ArrayList<String> denies = nevers.get(protectionDomain.getCodeSource()
            .getLocation());
            
            if (denies == null) {
                denies = new ArrayList<String>();
            }
            
            if (denies.contains(permission.getName())) {
                return false;
            }
            
            StringBuffer sb = new StringBuffer();
            sb.append(loader.getName());
            sb.append(" has asked for permission " + permission.getName());
            sb.append(". \n Would you like to grant this permission?");
            
            String[] options = { "Yes", "No", "Always", "Never" };
            Window win = SwingUtilities.windowForComponent(DeskletManager.main.getDesktop());
            int value = JOptionPane.showOptionDialog(win,
                    sb.toString(), "Grant Permission",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            switch (value) {
                case 0:
                    perms.add(permission);
                    permissions.put(protectionDomain.getCodeSource(), perms);
                    
                    return true;
                    
                case 2:
                    perms.add(permission);
                    permissions.put(protectionDomain.getCodeSource(), perms);
                    if( ! grants.contains( permission.getName()) ){
                        grants.add(permission.getName());
                    }
                    always.put(protectionDomain.getCodeSource().getLocation(),
                            grants);
                    this.storeRemembered( always, "always");
                    try{
                        prefs.flush();
                    } catch(Exception e ){
                        e.printStackTrace();
                    }
                    return true;
                    
                case 3:
                    if( !denies.contains( permission.getName()) ){
                        denies.add(permission.getName());
                    }
                    nevers.put(protectionDomain.getCodeSource().getLocation(),
                            denies);
                    storeRemembered(nevers, "never");
                    try{
                        prefs.flush();
                    } catch(Exception e ){
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
    
    public PermissionCollection getPermissions(CodeSource codeSource) {
        Permissions p = permissions.get(codeSource);
        
        if (p == null) {
            p = new Permissions();
            permissions.put(codeSource, p);
        }
        
        return p;
    }
    
    void storeRemembered(HashMap<URL, ArrayList<String>> perms, String mode) {
        StringBuffer sb = new StringBuffer();
        Iterator<Entry<URL, ArrayList<String>>> uit = perms.entrySet().iterator();
        StringBuffer urls = new StringBuffer();
        
        while (uit.hasNext()) {
            Entry<URL, ArrayList<String>> entry = uit.next();
            URL source = entry.getKey();
            ArrayList<String> grants = entry.getValue();
            Iterator<String> it = grants.iterator();
            
            while (it.hasNext()) {
                sb.append(it.next());
                sb.append("\n");
                
            }
            LOG.info( "Serializing for: "+ source.toExternalForm());
            LOG.info( sb.toString() );
            prefs.remove(mode+"-"+source.toExternalForm());
            prefs.put( mode+"-"+source.toExternalForm(), sb.toString() );
            urls.append( source.toString() );
            
            if (uit.hasNext()) {
                urls.append("\n");
            }
        }
        prefs.put( mode+"-urls", urls.toString() );
        
        
    }
    
    HashMap<URL, ArrayList<String>> deserializeRemembered(String mode) {
        StringTokenizer urls = new StringTokenizer(prefs.get(mode+"-urls", ""), "\n");
        HashMap<URL, ArrayList<String>> perms = new HashMap<URL, ArrayList<String>>();
        
        while (urls.hasMoreTokens()) {
            
            
            try {
                
                URL url = new URL(urls.nextToken() );
                StringTokenizer entries = new StringTokenizer(
                        prefs.get(mode+"-"+url.toExternalForm(),""),
                        "\n");
                LOG.info( "Deserializing for: "+ url.toExternalForm());
                LOG.info(prefs.get(mode+"-"+url.toExternalForm(),""));
                ArrayList<String> grants = new ArrayList<String>();
                
                while (entries.hasMoreTokens()) {
                    String next = entries.nextToken();
                    if( !grants.contains( next )) {
                        grants.add(next);
                    }
                }
                
                perms.put(url, grants);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception loading Grant preferences.", e);
                
                continue;
            }
        }
        
        return perms;
    }
}
