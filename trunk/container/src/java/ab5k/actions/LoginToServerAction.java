/*
 * LoginToServerAction.java
 *
 * Created on February 19, 2007, 5:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.actions;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class LoginToServerAction {
    
    /** Creates a new instance of LoginToServerAction */
    public LoginToServerAction() {
        
    }
    
    public void loginToServer() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    u.p("logging in to the server");
                    InputStream in = new URL("http://joshy.org:8088/AB5kTracker/login.jsp?hostname=blahfoo.com").openStream();
                    byte[] buf = new byte[256];
                    while(true) {
                        int n = in.read(buf);
                        if(n == -1) break;
                        System.out.print("read: ");
                        for(int i=0; i<n; i++) {
                            System.out.print((char)buf[i]);
                        }
                        System.out.println("");
                    }
                    u.p("finished reading");
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        ).start();
    }
    
}
