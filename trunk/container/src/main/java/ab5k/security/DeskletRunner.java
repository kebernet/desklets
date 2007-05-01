/*
 * DeskletRunner.java
 *
 * Created on August 3, 2006, 10:10 PM
 */
package ab5k.security;

import ab5k.Core;

import ab5k.desklet.Desklet;
import java.io.IOException;

import java.net.URI;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cooper
 */
public class DeskletRunner extends Thread {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private DefaultContext context;
    private Desklet desklet;
    private Core main;
    
    /** Creates a new instance of DeskletRunner */
    public DeskletRunner(Core main, DefaultContext context)
    throws LifeCycleException {
        super();
        this.context = context;
        this.main = main;
        context.setBrowserHandler(new BrowserHandler() {
            public void showURL(URI uri) {
                DeskletRunner.this.main.showURL(uri);
            }
        });
        
        DeskletConfig config = context.getConfig();
        ClassLoader loader = context.getConfig().getClassLoader();
        
        this.setContextClassLoader(loader);
        
        try {
            this.desklet = (Desklet) config.getClassLoader()
            .loadClass(config.getClassName())
            .newInstance();
            desklet.setContext(context);
            desklet.init();
        } catch(Throwable e) {
            throw new LifeCycleException("Unable to init desklet: " +
                    config.getName(), e);
        }
    }
    
    public void destroyDesklet() {
        
        try {
            ContainerFactory.getInstance().cleanup(getContext());
            desklet.destroy();
        } catch(Exception e) {
            LOG.log(Level.WARNING,
                    "Desklet " + getConfig().getName() +
                    " threw an exception from .destroy() ", e);
        }
        try {
            getContext().flushPreferences();
        } catch(IOException ex) {
            LOG.log(Level.WARNING,
                "Exception saving prefs for " + getContext().getConfig().getName(),
                ex);
        }
    }
    
    public DeskletConfig getConfig() {
        return getContext().getConfig();
    }
    
    public boolean isShutdownWhenIdle() {
        return getContext().isShutdownWhenIdle();
    }
    
    public void run() {
        try {
            desklet.start();
        } catch(Exception e) {
            LOG.log(Level.WARNING,
                    "Desklet " + getConfig().getName() +
                    " threw an exception from .start() ", e);
            stopDesklet();
            destroyDesklet();
        }
    }
    
    public void stopDesklet() {
        try {
            desklet.stop();
            
            long begin = System.currentTimeMillis();
            
            while(!getContext().isStopped()) {
                try {
                    Thread.sleep(1000);
                    
                    if((System.currentTimeMillis() - begin) > (10 * 1000)) {
                        this.interrupt();
                        throw new RuntimeException(
                                "20 Second timeout waiting for notifyStop expired.");
                    }
                } catch(java.lang.InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            LOG.log(Level.WARNING,
                    "Desklet " + getConfig().getName() +
                    " threw an exception from .stop() ", e);
            destroyDesklet();
        }
    }
    
    public String toString() {
        return getContext().getConfig().getName() + "(" +
                getContext().getConfig().getUUID() + ")";
    }

    public DefaultContext getContext() {
        return context;
    }
}
