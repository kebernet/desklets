/*
 * BackgroundManager.java
 *
 * Created on July 12, 2006, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k;

import com.totsp.util.BeanArrayList;
import org.joshy.util.u;

/**
 *
 * @author jm158417
 */
public class BackgroundManager {
    private Main main;
    private BeanArrayList backgrounds;
    
    /** Creates a new instance of BackgroundManager */
    public BackgroundManager(Main main) {
        this.main = main;
        setBackgrounds(new BeanArrayList("background", this));
    }
    
    
    public void setDesktopBackground(DesktopBackground bg) {
        u.p("desktop here = " + main.getDesktop());
        ((CustomDesktopPane)main.getDesktop()).setDesktopBackground(bg);
    }

    public BeanArrayList getBackgrounds() {
        return backgrounds;
    }

    public void addBackground(DesktopBackground bg) {
        getBackgrounds().add(bg);
    }
    public void setBackgrounds(BeanArrayList backgrounds) {
        this.backgrounds = backgrounds;
    }
}
