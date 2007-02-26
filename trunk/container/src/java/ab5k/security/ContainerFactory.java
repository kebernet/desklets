/*
 * ContainerFactory.java
 *
 * Created on February 22, 2007, 6:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.security;

import java.awt.Container;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;


/**
 *
 * @author cooper
 */
public class ContainerFactory {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private static final String LOCATION_X = "ab5k.location.x";
    private static final String LOCATION_Y = "ab5k.location.y";
    private JDesktopPane desktop;
    private Container dock;
    private HashMap<JPanel, JInternalFrame> iframes = new HashMap<JPanel, JInternalFrame>();
    private static final ContainerFactory instance = new ContainerFactory();
    
    
    /** Creates a new instance of ContainerFactory */
    private ContainerFactory() {
        super();
    }
    
    public void init( JDesktopPane desktop, Container dock ){
        this.desktop = desktop;
        this.dock = dock;
    }
    
    public static ContainerFactory getInstance(){
        return instance;
    }
    
    public InternalFrameContainer createInternalFrameContainer(DefaultContext context){
        InternalFrameContainer ifc = new InternalFrameContainer(context.getConfig().getName());
        iframes.put( ifc.panel, ifc.iframe );
        try{
            Point point = new Point(
                    (int) Double.parseDouble(
                    context.getPreference( ContainerFactory.LOCATION_X, "50")),
                    (int) Double.parseDouble(
                    context.getPreference( ContainerFactory.LOCATION_Y, "50") ));
            System.out.println( "Setting location: "+ point.getX() +" "+point.getY() );
            
            
            /* Commnented out temporarily
            // get the bounds of the window
            Rectangle bounds = desktop.getBounds();
             
            // if the desklet would be outside the bounds of the window, then move it back in
            if(!bounds.contains(point)) {
                LOG.info("desklet : " + getConfig().getName() + " is out of bounds!");
                u.p("bounds = " + bounds);
                u.p("new point = " + point);
                point = new Point(300,300);
            }
             */
            ifc.iframe.setLocation(point);
        } catch(NumberFormatException nfe){
            LOG.log( Level.WARNING, "Excpetion reading screen position information.", nfe);
            ifc.iframe.setLocation( 50, 50 );
        }
        desktop.add( ifc.iframe );
        return ifc;
    }
    
    public DockContainer createDockContainer(DefaultContext context ){
        DockContainer dock = new DockContainer();
        this.dock.add( dock.panel );
        return dock;
    }
    
    public void cleanup(DefaultContext context){
        if( context.hasDock() ){
            dock.remove( ((DockContainer)context.getDockingContainer()).panel );
            dock.validate();
        }
        if( context.hasContainer() ){
            final InternalFrameContainer ifc = (InternalFrameContainer) context.getContainer();
            Point location = ifc.iframe.getLocation();
            context.setPreference( ContainerFactory.LOCATION_X, Double.toString( location.getX()));
            context.setPreference( ContainerFactory.LOCATION_Y, Double.toString( location.getY()));
            
            Animator an = PropertySetter.createAnimator(500,
                    ifc.iframe,"location",
                    ifc.iframe.getLocation(), new Point(-desktop.getWidth(),
                    desktop.getHeight()));
            
            an.addTarget(new TimingTarget() {
                public void begin() {  }
                public void end() { desktop.remove( ifc.iframe ); }
                public void repeat() {  }
                public void timingEvent(float fraction) {  }
            });
            
            an.start();
            iframes.remove( ifc.panel );
        }
        try {
            context.flushPreferences();
        } catch (IOException ex) {
            LOG.log( Level.WARNING, "Exception saving prefs for "+context.getConfig().getName(), ex);
        }
        
    }
    
}
