/*
 * Main.java
 *
 * Created on April 25, 2007, 3:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fxwidget;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContainer;
import ab5k.desklet.test.DeskletTester;
import java.awt.Color;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author joshy
 */
public class FXCalculator extends Desklet {
    
    /** Creates a new instance of Main */
    public FXCalculator() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DeskletTester.start(FXCalculator.class);
    }
    
    public void init() throws Exception {
        // josh: look into using this
        // Thread.currentThread().setContextClassLoader(cl);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("fx");
        ScriptContext context = engine.getContext();
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setBorder(BorderFactory.createMatteBorder(10,0,0,0,new Color(0x2d4d6a)));
        bindings.put("GLOSSITOPE:java.awt.Container", container);
        String script = "import calc.Calculator;";
        engine.eval(script);
        
        
        ///frame.pack();
        //frame.setVisible(true);
        DeskletContainer c = getContext().getContainer();
        c.setContent(container);//new JButton("fx test"));
        c.setShaped(true);
        //c.setResizable(true);
        c.setBackgroundDraggable(true);
        //c.pack();
        //c.setVisible(true);
    }
    
    public void start() throws Exception {
    }
    
    public void stop() throws Exception {
        getContext().notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
}
