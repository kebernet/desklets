/*
 * SecurityPolicy.java
 *
 * Created on August 3, 2006, 4:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ab5k.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import ab5k.Core;
import ab5k.Environment;
import ab5k.PermissionManager;


/**
 *
 * @author cooper
 */
public class SecurityPolicy extends Policy {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private static final File SECURITY_PROPS = new File( Environment.HOME, "security.properties");
    private static final ArrayList<String> SAFE_RUNTIME = new ArrayList<String>();
    private static final ArrayList<String> SAFE_ACCESS_CLASS_IN_PACKAGE = new ArrayList<String>();

    
    static{
        SAFE_RUNTIME.add( "modifyThread");
        SAFE_RUNTIME.add( "modifyThreadGroup");
        SAFE_RUNTIME.add( "stopThread");
        SAFE_RUNTIME.add( "getClassLoader");
        SAFE_RUNTIME.add( "createClassLoader");
        SAFE_RUNTIME.add( "accessClassInPackage");
        SAFE_ACCESS_CLASS_IN_PACKAGE.add( "sun.util.logging.resources");
    }
    private Hashtable<String, ArrayList<String>> always;
    private Hashtable<String, ArrayList<String>> nevers;
    private Hashtable<CodeSource, Permissions> permissions = new Hashtable<CodeSource, Permissions>();
    
    private Properties prefs = new java.util.Properties();
    private Core core;
    /** Creates a new instance of SecurityPolicy */
    public SecurityPolicy(Core core) {
        super();
        this.core = core;
        if(!SECURITY_PROPS.exists()) {
            System.out.println("security properties not created yet: " + SECURITY_PROPS.getAbsolutePath());
        } else {
            try{
                prefs.load( new FileInputStream(SECURITY_PROPS));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        always = this.deserializeRemembered("always");
        nevers = this.deserializeRemembered("never");
    }
    
    Hashtable<String, ArrayList<String>> deserializeRemembered(String mode) {
        StringTokenizer urls = new StringTokenizer(prefs.getProperty(mode + "-urls", ""),
                "\n");
        Hashtable<String, ArrayList<String>> perms = new Hashtable<String, ArrayList<String>>();
        
        while(urls.hasMoreTokens()) {
            try {
                StringTokenizer idUrl = new StringTokenizer(urls.nextToken(),
                        "\t");
                String id = idUrl.nextToken();
                String url = idUrl.nextToken();
                StringTokenizer entries = new StringTokenizer(prefs.getProperty(mode +
                        "-" + id, ""), "\n");
                LOG.info("Deserializing for: " + mode + " " + id + " -- " +
                        url);
                LOG.info(prefs.getProperty(mode + "-" + id, ""));
                
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
        
        if(Environment.autoGrantAll) { return true; }
        if(permission instanceof java.awt.AWTPermission ||
                permission instanceof java.util.PropertyPermission ||
                (permission instanceof java.lang.RuntimePermission && SAFE_RUNTIME.contains( permission.getName() )  ) ) {
            return true;
        }
        if(permission instanceof java.lang.RuntimePermission) {
            if(permission.getName().startsWith("accessClassInPackage")) {
                String clss = permission.getName().substring("accessClassInPackage".length()+1);
                if(SAFE_ACCESS_CLASS_IN_PACKAGE.contains(clss)) {
                    return true;
                }
            }
        }
        /*System.out.println(protectionDomain.getCodeSource().getLocation() +
                " requesting permission " + permission.getClass().getName() + " " +
                permission.getName());*/
        
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
            
            if(permission instanceof FilePermission && (Environment.HOME != null) &&
                    (permission.getName()
                    .startsWith(Environment.HOME.getAbsolutePath()) ||
                    permission.getName()
                    .startsWith(System.getProperty(
                    "java.io.tmpdir")))) {
                perms.add(permission);
                
                return true;
            }
            
            ArrayList<String> grants = always.get(protectionDomain.getCodeSource()
                    .getLocation()
                    .toExternalForm());
            String permissionValue = permission instanceof java.net.SocketPermission ? "java.net.SocketPermission" : permission.getName();
            permissionValue = permission instanceof java.io.FilePermission ? "java.io.FilePermission" : permission.getName();
            
            if(grants == null) {
                grants = new ArrayList<String>();
            }
            
            if(grants.contains(permissionValue)) {
                return true;
            }
            
            ArrayList<String> denies = nevers.get(protectionDomain.getCodeSource()
                    .getLocation()
                    .toExternalForm());
            
            if(denies == null) {
                denies = new ArrayList<String>();
            }
            
            if(denies.contains(permissionValue)) {
                return false;
            }
            
            StringBuffer sb = new StringBuffer();
            sb.append(loader.getName());
            sb.append(" has asked for permission " + permission.getName());
            //sb.append(". \n Would you like to grant this permission?");
            PermissionManager.Permission value = core.getPermissionManager().requestPermission(sb.toString());
            //u.p("permission value = " + value);
            /*
            String[] options = { "Yes", "No", "Always", "Never" };
            Window win = DeskletManager.main.getFrame();
            int value = JOptionPane.showOptionDialog(win, sb.toString(),
                    "Grant Permission", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            */
            switch(value) {
            case YES:
                perms.add(permission);
                permissions.put(protectionDomain.getCodeSource(), perms);
                
                return true;
                
            case ALWAYS:
                perms.add(permission);
                permissions.put(protectionDomain.getCodeSource(), perms);
                if(!grants.contains(permissionValue)) {
                    grants.add(permissionValue);
                }
                
                always.put(protectionDomain.getCodeSource().getLocation()
                        .toExternalForm(), grants);
                this.storeRemembered(always, "always");
                
                try {
                    prefs.store( new FileOutputStream( SecurityPolicy.SECURITY_PROPS ), "Security Settings" );
                } catch(Exception e) {
                    e.printStackTrace();
                }
                
                return true;
                
            case NEVER:
                
                if(!denies.contains(permissionValue)) {
                    denies.add(permissionValue);
                }
                
                nevers.put(protectionDomain.getCodeSource().getLocation()
                        .toExternalForm(), denies);
                storeRemembered(nevers, "never");
                
                try {
                    prefs.store( new FileOutputStream( SecurityPolicy.SECURITY_PROPS ), "Security Settings" );
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
    
    void storeRemembered(Hashtable<String, ArrayList<String>> perms, String mode) {
        Iterator<Entry<String, ArrayList<String>>> uit = new HashSet(perms.entrySet()).iterator();
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
