/*
 * Dependency.java
 *
 * Created on August 3, 2006, 6:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.security;

import com.totsp.util.ObjectBean;

/**
 *
 * @author cooper
 */
public class Dependency {
    
    
    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private ObjectBean obj = new ObjectBean(Dependency.class, this);
    
    
    /** Creates a new instance of Dependency */
    public Dependency( String groupId, String artifactId, String version, String type ){
        if( groupId == null || artifactId == null || version == null ){
            throw new RuntimeException( "groupId, artifactId and version are required.");
        }
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = type == null ? "jar" : type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
