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
    private ClassLoader classLoader;
    private File homeDir;
    private ObjectBean obj = new ObjectBean(DeskletConfig.class, this);
    private String UUID;
    private String authorName;
    private String className;
    private String description;
    private String name;
    private String version;
    private URL homePage;
    private Dependency[] dependencies;
    private URL[] repositories;
    private URL source;
    private URL sourceDef;
    private String specificationVersion;
    

    /** Creates a new instance of DeskletConfig */
    public DeskletConfig() {
        super();
    }

    public boolean equals(Object o) {
        return obj.equals(o);
    }

    public String getAuthorName() {
        return authorName;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public String getClassName() {
        return className;
    }

    public Dependency[] getDependencies() {
        Dependency[] ret = new Dependency[this.dependencies.length];

        for(int i = 0; i < this.dependencies.length; i++) {
            ret[i] = this.dependencies[i];
        }

        return ret;
    }

    public String getDescription() {
        return description;
    }

    public File getHomeDir() {
        return homeDir;
    }

    public URL getHomePage() {
        return homePage;
    }

    public String getName() {
        return name;
    }

    public URL[] getRepositories() {
        URL[] ret = new URL[this.repositories.length];

        for(int i = 0; i < this.repositories.length; i++) {
            ret[i] = this.repositories[i];
        }

        return ret;
    }

    public String getUUID() {
        return UUID;
    }

    public String getVersion() {
        return version;
    }

    public int hashCode() {
        return obj.hashCode();
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDependencies(Dependency[] dependencies) {
        Dependency[] set = new Dependency[dependencies.length];

        for(int i = 0; i < dependencies.length; i++) {
            set[i] = dependencies[i];
        }

        this.dependencies = set;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    void setHomeDir(File homeDir) {
        this.homeDir = homeDir;
    }

    public void setHomePage(URL homePage) {
        this.homePage = homePage;
    }

    void setName(String name) {
        this.name = name;
    }

    public void setRepositories(URL[] repositories) {
        URL[] set = new URL[repositories.length];

        for(int i = 0; i < repositories.length; i++) {
            set[i] = repositories[i];
        }

        this.repositories = set;
    }

    void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return obj.toString();
    }

    public URL getSource() {
        return source;
    }

    public void setSource(URL source) {
        this.source = source;
    }

    public URL getSourceDef() {
        return sourceDef;
    }

    public void setSourceDef(URL sourceDef) {
        this.sourceDef = sourceDef;
    }

    public String getSpecificationVersion() {
        return specificationVersion;
    }

    public void setSpecificationVersion(String specificationVersion) {
        this.specificationVersion = specificationVersion;
    }
}
