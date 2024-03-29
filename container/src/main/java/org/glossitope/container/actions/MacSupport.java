/*
 * MacSupport.java
 *
 * Created on February 20, 2007, 8:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import org.joshy.util.u;

import org.glossitope.container.Core;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.ApplicationListener;

/**
 *
 * @author joshy
 */
public class MacSupport {
    public static boolean isMac() {
        try {
            Application.getApplication();
            return true;
        } catch (Throwable t) {
            u.p(t);
            return false;
        }
    }
    
    public static void setupMacSupport(final Core main) {
        try {
            u.p("installing mac support");
            Application app = Application.getApplication();
            if (app != null) {
            Application.getApplication().addApplicationListener(new ApplicationListener() {
                public void handleAbout(ApplicationEvent applicationEvent) {
                    u.p("Apple API: about called:" + applicationEvent);
                }
                public void handleOpenApplication(ApplicationEvent applicationEvent) {
                    u.p("Apple API: open app called:" + applicationEvent);
                }
                public void handleOpenFile(ApplicationEvent applicationEvent) {
                    u.p("Apple API: open file called:" + applicationEvent);
                    u.p("filename = " + applicationEvent.getFilename());
                    String filename = applicationEvent.getFilename();
                    u.p("already running = " + SingleLaunchSupport.isAlreadyRunning());
                    if(SingleLaunchSupport.isAlreadyRunning()) {
                        SingleLaunchSupport.relaunch(filename);
                        System.exit(0);
                        return;
                    }
                    try {
                        main.getLoadDeskletAction().load(new File(filename).toURI().toURL());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                public void handlePreferences(ApplicationEvent applicationEvent) {
                    u.p("Apple API: prefs called:" + applicationEvent);
                }
                public void handlePrintFile(ApplicationEvent applicationEvent) {
                    u.p("Apple API: print called:" + applicationEvent);
                }
                public void handleQuit(ApplicationEvent applicationEvent) {
                    u.p("Apple API: quit called:" + applicationEvent);
                    main.getQuitAction().actionPerformed(
                            new ActionEvent(this,-1,"exit"));
                }
                public void handleReOpenApplication(ApplicationEvent applicationEvent) {
                    u.p("Apple API: re-open called:" + applicationEvent);
                }
            });
            u.p("mac support installed");
            } {
                u.p("not on a mac");
            }
        }catch (Throwable thr) {
            u.p("installing mac support failed");
            u.p(thr.getMessage());
            thr.printStackTrace();
        }
    }
    
}
