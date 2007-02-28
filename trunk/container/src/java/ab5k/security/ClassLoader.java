/*
 * ClassLoader.java
 *
 * Created on August 3, 2006, 4:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ab5k.security;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author cooper
 */
public class ClassLoader extends URLClassLoader {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private final java.lang.ClassLoader parent = Thread.currentThread()
                                                       .getContextClassLoader();
    private String deskletName;
    private String deskletUUID;
    public URL[] classPath;

    /** Creates a new instance of ClassLoader */
    public ClassLoader(String deskletUUID, String deskletName, URL[] classPath) {
        super(classPath);
        this.deskletUUID = deskletUUID;
        this.deskletName = deskletName;

        URL[] set = new URL[classPath.length];

        for(int i = 0; i < classPath.length; i++) {
            set[i] = classPath[i];
        }

        this.classPath = set;
    }

    public boolean equals(Object o) {
        //Just make sure the UUID and URLs are the same;
        if(!(o instanceof ClassLoader)) {
            return false;
        }

        ClassLoader l = (ClassLoader) o;

        if(!l.deskletUUID.equals(this.deskletUUID)) {
            return false;
        }

        URL[] myUrls = this.getURLs();
        URL[] herUrls = l.getURLs();

        if(myUrls.length != herUrls.length) {
            return false;
        }

        for(int i = 0; i < myUrls.length; i++) {
            if(!myUrls[i].equals(herUrls[i])) {
                return false;
            }
        }

        return true;
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        if(name.startsWith("ab5k") && !name.startsWith("ab5k.desklet")) {
            throw new ClassNotFoundException(
                "Access to core classes prohibited.");
        }

        LOG.fine("Loading class: " + name);

        try {
            return super.findClass(name);
        } catch(ClassNotFoundException nfe) {
            LOG.log(Level.FINEST, "Class not found in secured loader.", nfe);

            return parent.loadClass(name);
        }
    }

    public String getName() {
        return this.deskletName;
    }

    public String getUUID() {
        return this.deskletUUID;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ClassLoader: \n");

        for(URL url : this.getURLs()) {
            sb.append(url.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
