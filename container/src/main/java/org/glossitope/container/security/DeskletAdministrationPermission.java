/*
 * DeskletAdministrationPermission.java
 *
 * Created on August 9, 2006, 8:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.glossitope.container.security;

import java.security.Permission;


/**
 *
 * @author cooper
 */
public class DeskletAdministrationPermission extends Permission {
    private String actions;
    private String name;

    /** Creates a new instance of DeskletAdministrationPermission */
    public DeskletAdministrationPermission(String name, String actions) {
        super(name);
        this.name = name;
        this.actions = actions;
    }

    public boolean equals(Object object) {
        return (object instanceof DeskletAdministrationPermission &&
        ((DeskletAdministrationPermission) object).name.equals(name) &&
        ((DeskletAdministrationPermission) object).actions.equals(actions));
    }

    public String getActions() {
        return actions;
    }

    public int hashCode() {
        return name.hashCode() | actions.hashCode();
    }

    public boolean implies(Permission permission) {
        return permission.equals(this);
    }
}
