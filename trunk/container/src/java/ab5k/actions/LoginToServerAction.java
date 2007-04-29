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
import java.net.URLEncoder;
import java.util.TimeZone;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class LoginToServerAction {

    private boolean shouldLogin = true;
    
    /** Creates a new instance of LoginToServerAction */
    public LoginToServerAction() {
        
    }
    
    public void loginToServer() {
        new Thread(new Runnable() {
            public void run() {
                if(!shouldLogin) {
                    u.p("skipping the auto-login");
                    return;
                }
                try {
                    u.p("logging in to the server");
                    String query =  
                            "hostname=blahfoo2.com"+
                            "&osname="+URLEncoder.encode(System.getProperty("os.name"),"UTF-8")+
                            "&javaver="+URLEncoder.encode(System.getProperty("java.runtime.version"),"UTF-8")+
                            "&timezone="+URLEncoder.encode(TimeZone.getDefault().getID(),"UTF-8");
                    u.p("unencoded query: \n" + query);
                    String login_url = "http://joshy.org:8088/AB5kTracker/login.jsp?";
                    //login_url = "http://localhost:8080/AB5kTracker/login.jsp?";
                    String url = login_url+query;
                    u.p("final encoded url = \n" + url);
                    InputStream in = new URL(url).openStream();
                    byte[] buf = new byte[256];
                    while(true) {
                        int n = in.read(buf);
                        if(n == -1) break;
                        //System.out.print("read: ");
                        for(int i=0; i<n; i++) {
                            //System.out.print((char)buf[i]);
                        }
                        //System.out.println("");
                    }
                    //u.p("finished reading");
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        },
        "LoginToServerAction").start();
    }

    public void setShouldLogin(boolean b) {
        this.shouldLogin = b;
    }
    
}
