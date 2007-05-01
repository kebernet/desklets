/*
 * CustomCellRendererPane.java
 * 
 * Created on Apr 28, 2007, 9:29:52 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.accessibility.*;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import org.joshy.util.u;

public class CustomCellRendererPane extends CellRendererPane {

    public CustomCellRendererPane() {
        super();
    }


    public void paintComponent(Graphics g, Component c, Container p, int x, int y, int w, int h, boolean shouldValidate) {
	if (c == null) {
	    if (p != null) {
		Color oldColor = g.getColor();
		g.setColor(p.getBackground());
		g.fillRect(x, y, w, h);
		g.setColor(oldColor);
	    }
	    return;
	}

	if (c.getParent() != this) {
	    this.add(c);
	}

	c.setBounds(x, y, w, h);

	if(shouldValidate) {
	    c.validate();
	}

	boolean wasDoubleBuffered = false;
	if ((c instanceof JComponent) && ((JComponent)c).isDoubleBuffered()) {
	    wasDoubleBuffered = true;
	    ((JComponent)c).setDoubleBuffered(false);
	}

	Graphics cg = g.create(x, y, w, h);
	try {
	    c.paint(cg);
	}
	finally {
	    cg.dispose();
	}

	if (wasDoubleBuffered && (c instanceof JComponent)) {
	    ((JComponent)c).setDoubleBuffered(true);
	}

	c.setBounds(-w, -h, 0, 0);
    }

}


