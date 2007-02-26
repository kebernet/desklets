/*
 * Main.java
 *
 * Created on August 9, 2006, 9:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package clockdesklet;

import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;
import java.awt.Container;
import java.net.URI;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author cooper
 */
public class Main {
    public static void main(String[] args) {
        // TODO code application logic here
        DeskletTester.start(clockdesklet.Desklet.class);
    }
}
