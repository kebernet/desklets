/*
 * DeskletProxy.java
 *
 * Created on April 9, 2007, 4:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.desklet.DeskletContext;
import ab5k.security.DefaultContext;
import java.util.List;

/**
 *
 * @author joshy
 */
public class DeskletProxy {
    private DefaultContext context;
    public List<BufferedDeskletContainer> popupContainers;
    public List<BufferedDeskletContainer> dialogContainers;
    public BufferedDeskletContainer configContainer;
    public BufferedDeskletContainer contentContainer;
    
    /** Creates a new instance of DeskletProxy */
    public DeskletProxy(DefaultContext context, BufferedWM wm) {
        this.context = context;
        this.wm = wm;
        contentContainer = new BufferedDeskletContainer(wm, context, this);
        configContainer = new BufferedDeskletContainer(wm, context, this);

    }
    private BufferedWM wm;
    
}
