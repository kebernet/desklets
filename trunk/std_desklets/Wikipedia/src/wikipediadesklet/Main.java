/*
 * Main.java
 *
 * Created on February 8, 2007, 5:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wikipediadesklet;

import org.glossitope.desklet.*;
import org.glossitope.desklet.test.DeskletTester;

/**
 *
 * @author joshy
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DeskletTester.start(MainDesklet.class);
    }
    
}
