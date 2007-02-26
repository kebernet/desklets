/*
 * DeskletManager.java
 *
 * Created on August 4, 2006, 11:55 PM
 */
package ab5k.security;

import ab5k.Main;

import com.totsp.util.BeanArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 *
 * @author cooper
 */
public class DeskletManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(DeskletManager.class);
    static Main main;
    private final static DeskletAdministrationPermission PERMISSION = new DeskletAdministrationPermission("Desklet Manager",
            "all");
    private final static DeskletManager INSTANCE = new DeskletManager();
    private final BeanArrayList<DeskletRunner> runners = new BeanArrayList<DeskletRunner>("runners",
            this);
    private final ArrayList<String> suspendedUUIDs = new ArrayList<String>();
    
    /** Creates a new instance of DeskletManager */
    private DeskletManager() {
        super();
    }
    
    public static DeskletManager getInstance() {
        checkSecurity();
        
        return INSTANCE;
    }
    
    public void startUp(Main main) throws LifeCycleException {
        if (this.main != null) {
            throw new LifeCycleException("Startup has already been run!", null);
        }
        
        DeskletManager.main = main;
        
        ArrayList<String> uuids = this.getRunningDeskletIds();
        ArrayList<String> copy = new ArrayList<String>(uuids);
        
        for (String uuid : copy) {
            //System.out.println("Startup: " + uuid);
            
            DeskletRunner r = this.startDeskletRunner(uuid);
            
            if (r == null) {
                uuids.remove(uuid);
            }
        }
        
        this.setRunningDeskletIds(uuids);
    }
    
    public void pause(){
        ArrayList<DeskletRunner> runners = new ArrayList<DeskletRunner>(this.runners);
        ThreadPoolExecutor exec = new ThreadPoolExecutor( 10, 20, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
        
        for (DeskletRunner runner : runners) {
            final DeskletRunner r = runner;
            exec.execute( new Runnable(){
                public void run() {
                    if( r.isShutdownWhenIdle() ){
                        shutdownRunner(r);
                        suspendedUUIDs.add( r.getConfig().getUUID() );
                    }
                }
            });
        }
        try{
            exec.shutdown();
            exec.awaitTermination( 1, TimeUnit.MINUTES );
        } catch(InterruptedException ie ){
            ie.printStackTrace();;
        }
    }
    
    public void resume() {
        ThreadPoolExecutor exec = new ThreadPoolExecutor( 10, 20, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
        
        for(String uuid: this.suspendedUUIDs ){
            final String start = uuid;
            exec.execute( new Runnable(){
                public void run() {
                    try{
                        startDeskletRunner( start );
                    } catch(LifeCycleException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        try{
            exec.shutdown();
            exec.awaitTermination( 2, TimeUnit.MINUTES );
        } catch(InterruptedException ie ){
            ie.printStackTrace();;
        }
        this.suspendedUUIDs.clear();
    }
    
    
    private ArrayList<String> getRunningDeskletIds() {
        ArrayList<String> results = new ArrayList<String>();
        String runningPref = prefs.get("running", null);
        
        if (runningPref != null) {
            StringTokenizer tok = new StringTokenizer(runningPref, ",");
            
            while (tok.hasMoreElements()) {
                String uuid = tok.nextToken();
                results.add(uuid);
            }
        }
        
        return results;
    }
    
    private void setRunningDeskletIds(ArrayList<String> uuids) {
        Iterator it = uuids.iterator();
        StringBuffer pref = new StringBuffer();
        
        while (it.hasNext()) {
            pref.append(it.next());
            
            if (it.hasNext()) {
                pref.append(",");
            }
        }
        
        prefs.put("running", pref.toString());
    }
    
    public void startDesklet(String uuid) throws LifeCycleException {
        checkSecurity();
        
        ArrayList<String> uuids = this.getRunningDeskletIds();
        this.startDeskletRunner(uuid);
        uuids.add(uuid);
        this.setRunningDeskletIds(uuids);
    }
    
    private DeskletRunner startDeskletRunner(String uuid)
    throws LifeCycleException {
        Registry r = Registry.getInstance();
        DeskletConfig config = r.getDeskletConfig(uuid);
        
        if (config == null) {
            return null;
        }
        
        try {
            DeskletRunner runner = new DeskletRunner(main,
                    new DefaultContext(config) );
            runners.add(runner);
            runner.start();
            
            return runner;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LifeCycleException("Unable to start desklet " +
                    config.getName(), e);
        }
    }
    
    public void shutdownDesklet(String uuid) {
        checkSecurity();
        
        for (DeskletRunner runner : runners) {
            if (runner.getConfig().getUUID().equals(uuid)) {
                ArrayList<String> uuids = this.getRunningDeskletIds();
                uuids.remove(uuid);
                this.setRunningDeskletIds(uuids);
                shutdownRunner(runner);
                
                return;
            }
        }
    }
    
    private void shutdownRunner(DeskletRunner runner) {
        runner.stopDesklet();
        runner.destroyDesklet();
        runners.remove(runner);
    }
    
    public BeanArrayList<DeskletRunner> getRunners() {
        checkSecurity();
        
        return runners;
    }
    
    private static void checkSecurity() {
        SecurityManager sm = System.getSecurityManager();
        
        if (sm != null) {
            Object ctx = sm.getSecurityContext();
            sm.checkPermission(PERMISSION, ctx);
        }
    }
    
    public void shutdown() {
        ArrayList<DeskletRunner> runners = new ArrayList<DeskletRunner>(this.runners);
        ThreadPoolExecutor exec = new ThreadPoolExecutor( 10, 20, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
        for (DeskletRunner runner : runners) {
            final DeskletRunner r = runner;
            exec.execute( new Runnable(){
                public void run() {
                    shutdownRunner(r);
                }
            });
        }
        
        try{
            exec.shutdown();
            exec.awaitTermination( 2, TimeUnit.MINUTES );
        } catch(InterruptedException ie ){
            ie.printStackTrace();;
        }
        
        try {
             prefs.flush();
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
    }
}
