/*
 * Main.java
 *
 * Created on February 15, 2007, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package worldmap;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;

/**
 *
 * @author joshy
 */
public class WorldMapDesklet extends AbstractDesklet {

    private WorldMapForm form;
    
    /** Creates a new instance of Main */
    public WorldMapDesklet() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DeskletTester.start(WorldMapDesklet.class);
    }

    public void init(DeskletContext context) throws Exception {
        this.context = context;
        form = new WorldMapForm();
        context.getContainer().setContent(form);
        context.getContainer().setVisible(true);
     }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        this.context.notifyStopped();
    }

    public void destroy() throws Exception {
    }
    
}
