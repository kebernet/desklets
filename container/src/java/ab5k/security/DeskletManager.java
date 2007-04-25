/*
 * DeskletManager.java
 *
 * Created on August 4, 2006, 11:55 PM
 */
package ab5k.security;

import ab5k.Core;
import ab5k.Environment;
import ab5k.prefs.ConfigurationImportExport;

import com.totsp.util.BeanArrayList;
import java.awt.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.joshy.util.u;


/**
 *
 * @author cooper
 */
public class DeskletManager {
    private static final Properties prefs = new Properties();
    private static final File STARTUP_PROPS = new File(Environment.HOME,
            "startup.properties");
    static Core main;
    private final static DeskletAdministrationPermission PERMISSION = new DeskletAdministrationPermission("Desklet Manager",
            "all");
    private final static DeskletManager INSTANCE = new DeskletManager();
    static{
        ConfigurationImportExport.registerExport( "startup", STARTUP_PROPS );
    }
    private final ArrayList<String> suspendedUUIDs = new ArrayList<String>();
    private final BeanArrayList<DeskletRunner> runners = new BeanArrayList<DeskletRunner>("runners",
            this);

    /** Creates a new instance of DeskletManager */
    private DeskletManager() {
        super();

        try {
            if(!STARTUP_PROPS.exists()) {
                u.p("file doesn't exist: " + STARTUP_PROPS.getAbsolutePath());
            } else {
                prefs.load(new FileInputStream(STARTUP_PROPS));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        DeskletUpdater updater = new DeskletUpdater(this, prefs);
        Thread t = new Thread(updater);
        t.start();
    }

    private static void checkSecurity() {
        SecurityManager sm = System.getSecurityManager();

        if(sm != null) {
            Object ctx = sm.getSecurityContext();
            sm.checkPermission(PERMISSION, ctx);
        }
    }

    public static DeskletManager getInstance() {
        checkSecurity();

        return INSTANCE;
    }

    public BeanArrayList<DeskletRunner> getRunners() {
        checkSecurity();

        return runners;
    }
    
    public DeskletRunner getDeskletRunner(DefaultContext context) {
        checkSecurity();
        for(DeskletRunner r : runners) {
            if(r.getContext() == context) {
                return r;
            }
        }
        return null;
    }

    private ArrayList<String> getRunningDeskletIds() {
        ArrayList<String> results = new ArrayList<String>();
        String runningPref = prefs.getProperty("running", null);

        if(runningPref != null) {
            StringTokenizer tok = new StringTokenizer(runningPref, ",");

            while(tok.hasMoreElements()) {
                String uuid = tok.nextToken();
                results.add(uuid);
            }
        }

        return results;
    }

    public void flushPreferences(){
        ArrayList<DeskletRunner> runners = new ArrayList<DeskletRunner>(this.runners);
        for( DeskletRunner runner : runners ){
            try{
                runner.getContext().flushPreferences();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public void pause() {
        ArrayList<DeskletRunner> runners = new ArrayList<DeskletRunner>(this.runners);
        ThreadPoolExecutor exec = new ThreadPoolExecutor(10, 20, 100,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for(DeskletRunner runner : runners) {
            final DeskletRunner r = runner;
            exec.execute(new Runnable() {
                    public void run() {
                        if(r.isShutdownWhenIdle()) {
                            shutdownRunner(r);
                            suspendedUUIDs.add(r.getConfig().getUUID());
                        }
                    }
                });
        }

        try {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.MINUTES);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
            ;
        }
    }

    public void resume() {
        ThreadPoolExecutor exec = new ThreadPoolExecutor(10, 20, 100,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for(String uuid : this.suspendedUUIDs) {
            final String start = uuid;
            exec.execute(new Runnable() {
                    public void run() {
                        try {
                            startDeskletRunner(start);
                        } catch(LifeCycleException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }

        try {
            exec.shutdown();
            exec.awaitTermination(2, TimeUnit.MINUTES);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
            ;
        }

        this.suspendedUUIDs.clear();
    }

    private void setRunningDeskletIds(ArrayList<String> uuids) {
        Iterator it = uuids.iterator();
        StringBuffer pref = new StringBuffer();

        while(it.hasNext()) {
            pref.append(it.next());

            if(it.hasNext()) {
                pref.append(",");
            }
        }

        prefs.put("running", pref.toString());
        try {
            prefs.store(new FileOutputStream(STARTUP_PROPS), "Startup Settings");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        ArrayList<DeskletRunner> runners = new ArrayList<DeskletRunner>(this.runners);
        ThreadPoolExecutor exec = new ThreadPoolExecutor(10, 20, 100,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for(DeskletRunner runner : runners) {
            final DeskletRunner r = runner;
            exec.execute(new Runnable() {
                    public void run() {
                        shutdownRunner(r);
                    }
                });
        }

        try {
            exec.shutdown();
            exec.awaitTermination(2, TimeUnit.MINUTES);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
            ;
        }
    }

    public void shutdownDesklet(String uuid) {
        checkSecurity();

        System.out.println("num uids = " + this.getRunningDeskletIds().size());
        for(String s : this.getRunningDeskletIds()) {
            System.out.println("id = " + s);
        }
        for(DeskletRunner runner : runners) {
            if(runner.getConfig().getUUID().equals(uuid)) {
                ArrayList<String> uuids = this.getRunningDeskletIds();
                uuids.remove(uuid);
                this.setRunningDeskletIds(uuids);
                shutdownRunner(runner);

                return;
            }
        }
    }

    // shuts down a runner. called when ab5k shuts down
    public void shutdownRunner(DeskletRunner runner) {
        runner.stopDesklet();
        runner.destroyDesklet();
        runners.remove(runner);
    }

    // stops a runner. this removes it from the list so it won't
    // come back the next time ab5k is restarted
    public void stopRunner(DeskletRunner runner) {
        String id = runner.getConfig().getUUID();
        ArrayList<String> uuids = this.getRunningDeskletIds();
        if(uuids.contains(id)) {
            uuids.remove(id);
            this.setRunningDeskletIds(uuids);
        }
        runner.stopDesklet();
        runner.destroyDesklet();
        runners.remove(runner);
    }
    
    public void startDesklet(String uuid) throws LifeCycleException {
        checkSecurity();

        ArrayList<String> uuids = this.getRunningDeskletIds();
        
        if(!Environment.allowMultipleInstances && uuids.contains(uuid) ){
            DeskletManager.main.handleError("Error","That desklet is already running",null);
            return;
        }
        this.startDeskletRunner(uuid);
        uuids.add(uuid);
        this.setRunningDeskletIds(uuids);
    }

    private DeskletRunner startDeskletRunner(String uuid)
        throws LifeCycleException {
        Registry r = Registry.getInstance();
        DeskletConfig config = r.getDeskletConfig(uuid);

        if(config == null) {
            return null;
        }

        try {
            DeskletRunner runner = new DeskletRunner(main,
                    new DefaultContext(main,config));
            runners.add(runner);
            runner.start();

            return runner;
        } catch(Exception e) {
            e.printStackTrace();
            throw new LifeCycleException("Unable to start desklet " +
                config.getName(), e);
        }
    }
    
    public void restart() throws LifeCycleException {
        this.shutdown();
        Core main = this.main;
        this.main = null;
        try {
            prefs.load(new FileInputStream(STARTUP_PROPS));
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.startUp( main );
    }

    public void startUp(Core main) throws LifeCycleException {
        if(this.main != null) {
            throw new LifeCycleException("Startup has already been run!", null);
        }

        DeskletManager.main = main;

        ArrayList<String> uuids = this.getRunningDeskletIds();
        ArrayList<String> copy = new ArrayList<String>(uuids);

        for(String uuid : copy) {
            //System.out.println("Startup: " + uuid);
            DeskletRunner r = this.startDeskletRunner(uuid);

            if(r == null) {
                uuids.remove(uuid);
            }
        }

        this.setRunningDeskletIds(uuids);
    }
}
