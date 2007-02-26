/*
 * DeskletConfig.java
 *
 * Created on August 3, 2006, 6:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.security;

import com.totsp.util.ObjectBean;
import java.io.File;
import java.net.URL;

/**
 *
 * @author cooper
 */
public class DeskletConfig {
    
    private File homeDir;
    private String name;
    private String UUID;
    private ClassLoader classLoader;
    private String className;
    private Dependency[] dependencies;
    private URL[] repositories;
    private ObjectBean obj = new ObjectBean(DeskletConfig.class, this);
    private String authorName;
    private URL homePage;
    private String description;
    private String version;
    
    /** Creates a new instance of DeskletConfig */
    public DeskletConfig() {
        super();
    }

    public File getHomeDir() {
        return homeDir;
    }

    void setHomeDir(File homeDir) {
        this.homeDir = homeDir;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return UUID;
    }

    void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Dependency[] getDependencies() {
        Dependency[] ret = new Dependency[ this.dependencies.length ];
        for( int i =0; i< this.dependencies.length ; i++){
            ret[i] = this.dependencies[i];
        }
        return ret;
    }

    public void setDependencies(Dependency[] dependencies) {
        Dependency[] set = new Dependency[ dependencies.length ];
        for( int i =0; i< dependencies.length ; i++){
            set[i] = dependencies[i];
        }
        this.dependencies = set;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public URL[] getRepositories() {
        URL[] ret = new URL[ this.repositories.length ];
        for( int i =0; i< this.repositories.length ; i++){
            ret[i] = this.repositories[i];
        }
        return ret;
    }

    public void setRepositories(URL[] repositories) {
        URL[] set = new URL[ repositories.length ];
        for( int i =0; i< repositories.length ; i++){
            set[i] = repositories[i];
        }
        this.repositories = set;
    }
    
    public boolean equals(Object o) {
        return obj.equals(o);
    }

    public int hashCode() {
        return obj.hashCode();
    }

    public String toString() {
        return obj.toString();
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public URL getHomePage() {
        return homePage;
    }

    public void setHomePage(URL homePage) {
        this.homePage = homePage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
