/*
 * DesktopBackground.java
 *
 * Created on July 3, 2006, 6:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container;

import java.awt.Graphics2D;
import javax.swing.JDesktopPane;

/**
 *
 * @author joshy
 */
public abstract class DesktopBackground {
    protected JDesktopPane desktopPane;
    private String backgroundName;
    private String backgroundDescription;
    
    /** Creates a new instance of DesktopBackground */
    public DesktopBackground() {
    }
    

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public void setDesktopPane(JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
    }
    
    public abstract void paint(Graphics2D g);

    public String getBackgroundName() {
        return backgroundName;
    }

    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }

    public String getBackgroundDescription() {
        return backgroundDescription;
    }

    public void setBackgroundDescription(String backgroundDescription) {
        this.backgroundDescription = backgroundDescription;
    }
}
