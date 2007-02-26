/*
 * DeskletRunner.java
 *
 * Created on August 3, 2006, 10:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ab5k.security;

import ab5k.Main;

import ab5k.desklet.Desklet;

import ab5k.util.MoveMouseListener;
import java.awt.Rectangle;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.net.URI;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joshy.util.u;


/**
 *
 * @author cooper
 */
public class DeskletRunner extends Thread {
    private static final Logger LOG = Logger.getLogger("AB5K");
    
    private Desklet desklet;
    private DefaultContext context;
    private Main main;
    /** Creates a new instance of DeskletRunner */
    public DeskletRunner(Main main, DefaultContext context)
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
            this.desklet = (Desklet) config.getClassLoader().loadClass( config.getClassName())
            .newInstance();
            
            desklet.init(context);
        } catch (ClassNotFoundException e) {
            throw new LifeCycleException("Unable to init desklet: " +
                    config.getName(), e);
        } catch (InstantiationException e) {
            throw new LifeCycleException("Unable to init desklet: " +
                    config.getName(), e);
        }catch (IllegalAccessException e) {
            throw new LifeCycleException("Unable to init desklet: " +
                    config.getName(), e);
        }catch (Exception e) {
            throw new LifeCycleException("Unable to init desklet: " +
                    config.getName(), e);
        } catch (Throwable e) {
            throw new LifeCycleException("Unable to init desklet: " +
                    config.getName(), e);
        }
        //this.setupMiniFrame();
        
        
    }
    
    /*private void setupMiniFrame() {
        if(desklet.isDockable()) {
            JPanel dockingContainer = (JPanel) context.getDockingContainer();
            dockingContainer.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
            main.getMainPanel().getDockPanel().add(dockingContainer);
            u.p("added the desklet to the dock");
        }
    }*/
    
    
    public void run() {
        
        try {
            desklet.start();
        } catch (Exception e) {
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
            while( !context.isStopped() ){
                try{
                    Thread.sleep( 1000 );
                    if( System.currentTimeMillis() - begin > 20 * 1000 ){
                        this.interrupt();
                        throw new RuntimeException("20 Second timeout waiting for notifyStop expired.");
                    }
                } catch(java.lang.InterruptedException e){
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            LOG.log(Level.WARNING,
                    "Desklet " + getConfig().getName() +
                    " threw an exception from .stop() ", e);
            destroyDesklet();
        }
    }
    
    public void destroyDesklet() {
        try{
            ContainerFactory.getInstance().cleanup(context);
            desklet.destroy();
        } catch (Exception e) {
            LOG.log(Level.WARNING,
                    "Desklet " + getConfig().getName() +
                    " threw an exception from .destroy() ", e);
        }
        
    }
    
    public DeskletConfig getConfig() {
        return context.getConfig();
    }
    
    
    public String toString(){
        return context.getConfig().getName() + "("+context.getConfig().getUUID()+")";
    }
    
    
    public boolean isShutdownWhenIdle(){
        return context.isShutdownWhenIdle();
    }
    
}
