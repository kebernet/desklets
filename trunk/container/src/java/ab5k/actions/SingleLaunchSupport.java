/*
 * SingleLaunchSupport.java
 *
 * Created on February 20, 2007, 8:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.actions;

import ab5k.Main;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static boolean useJWS = false;
    private static Main main;
    
    public static boolean setupSingleLaunchSupport(Main main, String ... args) {
        SingleLaunchSupport.main = main;
        if(useJWS) {
            setupJWS(args);
            return false;
        } else {
            return setupLocal(args);
        }
    }
    
    private static void setupJWS(String ... args) {
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
    
    public static final int PORT = 38629;
    public static ServerSocket server;
    
    private static boolean alreadyRunning = false;
    
    private static synchronized boolean setupLocal(String ... args) {
        try {
            server = new ServerSocket(PORT);
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("waiting for a connection");
                    while(true) {
                        try {
                            // wait for a socket connection
                            Socket sock = server.accept();
                            
                            // read the contents into a string buffer
                            InputStreamReader in = new InputStreamReader(sock.getInputStream());
                            StringBuffer sb = new StringBuffer();
                            char[] buf = new char[256];
                            while(true) {
                                int n = in.read(buf);
                                if(n < 0) { break; }
                                sb.append(buf,0,n);
                            }
                            // split the string buffer into strings
                            String[] results = sb.toString().split("\\n");
                            // call other main
                            otherMain(results);
                        } catch (IOException ex) {
                            System.out.println("ex: " + ex);
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
            firstMain(args);
            return false;
        } catch (IOException ioex) {
            alreadyRunning = true;
            System.out.println("already running!");
            relaunch(args);
            return true;
        }
    }
    
    public static void relaunch(String ... args) {
        try {
            // open a socket to the original instance
            Socket sock = new Socket("localhost",PORT);
            
            // write the args to the output stream
            OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream());
            for(int i=0; i<args.length; i++) {
                out.write(args[i]+"\n");
                u.p("wrote: " + args[i]);
            }
            // cleanup
            out.flush();
            out.close();
        } catch (Exception ex) {
            System.out.println("ex: " + ex);
            ex.printStackTrace();
        }
    }
    
    public static void firstMain(String[] args) {
        u.p("first launch args = ");
        u.p(args);
    }
    
    public static void otherMain( String[] args) {
        u.p("second launch args = ");
        u.p(args);
        for(String file : args) {
            try {
                main.getLoadDeskletAction().load(new File(file).toURL());
            } catch (Throwable th) {
                u.p(th);
            }
        }
        
    }
    
    public static boolean isAlreadyRunning() {
        return alreadyRunning;
    }
}
