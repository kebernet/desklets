/*
 * SingleLaunchSupport.java
 *
 * Created on February 20, 2007, 8:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.actions;

import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceListener;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class SingleLaunchSupport {
    
    public static void setupSingleLaunchSupport() {
        String[] services = ServiceManager.getServiceNames();
        u.p("services = ");
        u.p(services);
        Object serv;
        try {
            serv = ServiceManager.lookup("javax.jnlp.SingleInstanceService");
            if(serv != null) {
                SingleInstanceService single = (SingleInstanceService) serv;
                single.addSingleInstanceListener(new SingleInstanceListener() {
                    public void newActivation(String[] args) {
                        u.p("relaunching");
                        u.p(args);
                        u.p("eln = " + args.length);
                    }
                });
            }
        } catch (UnavailableServiceException ex) {
            System.out.println("the SingleInstanceService was not available");
        }
        
    }
    
}
