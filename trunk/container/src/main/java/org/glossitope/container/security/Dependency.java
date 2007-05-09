/*
 * Dependency.java
 *
 * Created on August 3, 2006, 6:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.glossitope.container.security;

import com.totsp.util.ObjectBean;


/**
 *
 * @author cooper
 */
public class Dependency {
    private ObjectBean obj = new ObjectBean(Dependency.class, this);
    private String artifactId;
    private String groupId;
    private String type;
    private String version;

    /** Creates a new instance of Dependency */
    public Dependency(String groupId, String artifactId, String version,
        String type) {
        if((groupId == null) || (artifactId == null) || (version == null)) {
            throw new RuntimeException(
                "groupId, artifactId and version are required.");
        }

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = (type == null) ? "jar" : type;
    }

    public boolean equals(Object o) {
        return obj.equals(o);
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public int hashCode() {
        return obj.hashCode();
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return obj.toString();
    }
}
