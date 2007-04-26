/*
 * BufferedPeer.java
 *
 * Created on April 10, 2007, 5:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public abstract class BufferedPeer extends DCPeer {
    private BufferedImage buffer;
    private Shape clip;
    private boolean dirty = true;
    private float alpha = 1f;
    private double rotation = 0;
    private double scale = 1.0;
    Dimension2D size = new Dimension(50, 50);
    
    /** Creates a new instance of BufferedPeer */
    public BufferedPeer(BufferedDeskletContainer bdc) {
        super(bdc);
    }
    
    public BufferedImage getBuffer() {
        return buffer;
    }
    
    
    void setDirty(boolean b) {
        this.dirty = b;
    }
    
    boolean isDirty() {
        return this.dirty;
    }
    
    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }
    
    
    
    
    public Dimension2D getSize() {
        return size;
    }

    public void setSize(Dimension2D dimension2D) {
        Dimension2D old = this.size;
        if(!old.equals(dimension2D)) {
            this.size = dimension2D;
            setDirty(true);
        }
    }
    
    public void setClip(Shape clip) {
        this.clip = clip;
    }
    
    public Shape getClip() {
        return this.clip;
    }
    public float getAlpha() {
        return alpha;
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    
    public double getRotation() {
        return rotation;
    }
    
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    public double getScale() {
        return scale;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    public void updateTexture(CellRendererPane rendererPane, Dimension size) {
        if (getBuffer() == null || isDirty()) {
            if(getSize().getWidth() < 1 || getSize().getHeight() < 1) {
                // too small. just skip it
                return;
            }
            //u.p("updating buffer because it's dirty");
            BufferedImage img = drawToBuffer(bdc.getTopComponent(), size, getBuffer(), rendererPane);
            setBuffer(img);
            rendererPane.removeAll();
            setDirty(false);
            //move back to the hidden parent
            this.bdc.wm.hidden.add(bdc.getTopComponent());
            //wasDirty = true;
        }
    }
    private BufferedImage drawToBuffer(final JComponent comp, final Dimension size, BufferedImage img, CellRendererPane rendererPane) {
        //JComponent comp = bdc.getTopComponent();
        //u.p("drawing to a buffer");
        //u.p("rendererpane = ");
        Component ct = rendererPane;
        while(ct.getParent() != null) {
            //u.p("" + ct);
            ct = ct.getParent();
        }
        //u.p("children = " + comp);
        for(Component c : comp.getComponents()) {
            //u.p("     c = " + c);
        }
        int pad = 0;
        // decide if we need to create a new image
        if(img == null ||
                img.getWidth() != (int)size.getWidth() ||
                img.getHeight() != (int)size.getHeight()) {
            //u.p("the size is different!");
            //u.p("building new buffered image");
            img = GraphicsUtilities.createCompatibleTranslucentImage((int)size.getWidth(), (int)size.getHeight());
        }
        // josh: there's some bug I can't fix. always recreating instead
        //img = GraphicsUtilities.createCompatibleTranslucentImage((int)size.getWidth()+pad, (int)size.getHeight()+pad);
        
        // draw the component into the image
        Graphics2D gx = img.createGraphics();
        //clear the buffer
        if(isShaped()) {
            gx.setColor(new Color(0,0,0,0));
        } else {
            gx.setColor(Color.WHITE);
        }
        gx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        gx.fillRect(0,0,img.getWidth(),img.getHeight());
        
        rendererPane.add(comp);
        rendererPane.paintComponent(gx, comp, null,
                pad/2,pad/2,  size.width, size.height,
                true);
        
        // draw debugging info
        /*
        if (this.bufferedWM.DEBUG_BORDERS) {
            gx.setColor(Color.GREEN);
            gx.drawLine(0,0,size.width,size.height);
            gx.drawLine(size.width,0,0,size.height);
        }*/
        //gx.setColor(Color.RED);
        //gx.drawString("comps: "+ comp.getComponentCount(),2,15);
        
        // dispose and return
        gx.dispose();
        return img;
    }
}
