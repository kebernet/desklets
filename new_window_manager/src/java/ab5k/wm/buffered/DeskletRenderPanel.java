package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.util.GraphicsUtil;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.painter.Painter;
import org.joshy.util.u;


// this just lays out components using their preferred size. it does not
// move them around at all.
// it also leaves the parent at whatever size it was set at

class DeskletRenderPanel extends JPanel {
    private final BufferedWM bufferedWM;
    
    
    private CellRendererPane rendererPane;
    private boolean animating = false;
    
    private Painter painter;
    
    
    public DeskletRenderPanel(BufferedWM bufferedWM) {
        super();
        this.bufferedWM = bufferedWM;
        rendererPane = new CellRendererPane();
        add(rendererPane);
        this.setLayout(new AbsLayout());
    }
    
    
    public void setBackgroundPainter(Painter painter) {
        this.painter = painter;
    }
    
    
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //u.p("repainting main panel");
        if(painter != null) {
            painter.paint(g2,this,getWidth(),getHeight());
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        g.setColor(Color.YELLOW);
        if(isAnimating()) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        }
        for(DeskletContainer dc : bufferedWM.getDesklets()) {
            if(dc instanceof BufferedDeskletContainer) {
                BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
                if(bdc.isVisible()) {
                    drawWindow(g2, bdc);
                }
                for(BaseDC dialog : bufferedWM.getDialogs(bdc)) {
                    BufferedDeskletContainer bdcd = (BufferedDeskletContainer) dialog;
                    if(bdcd.isVisible()) {
                        drawWindow(g2, bdcd);
                    }
                }
                for(BufferedPopup popup : bdc.popups) {
                    u.p("drawing popup");
                    drawPopup(g2, popup, bdc);
                }
                if(bdc.showSurfaces) {
                    for(BSurface s : bdc.surfaces) {
                        drawSurface(g2, s, bdc);
                    }
                }
            }
        }
    }
    
    private void drawPopup(Graphics2D g2, BufferedPopup popup, BufferedDeskletContainer bdc) {
        Dimension size = new Dimension(100,200);
        Point2D pt = new Point(20,20);
        
        BufferedImage img = drawToBuffer(popup.panel, size, bdc.getBuffer());
        
        Graphics2D g3 = (Graphics2D) g2.create();
        g3.translate(bdc.getLocation().getX(), bdc.getLocation().getX());
        g3.translate(pt.getX(), pt.getY());
        g3.drawImage(img,0,0,null);
        g3.dispose();
    }
    
    private void drawWindow(final Graphics2D g2, final BufferedDeskletContainer bdc) {
        Dimension size = new Dimension((int) bdc.getSize().getWidth(), (int) bdc.getSize().getHeight());
        Point2D pt = bdc.getLocation();
        boolean wasDirty = false;
        if (bdc.getBuffer() == null || bdc.isDirty()) {
            BufferedImage img = drawToBuffer(bdc.comp, size, bdc.getBuffer());
            bdc.setBuffer(img);
            rendererPane.removeAll();
            bdc.setDirty(false);
            //move back to the hidden parent
            this.bufferedWM.hidden.add(bdc.comp);
            wasDirty = true;
        }
        
        // draw to the screen
        Graphics2D g3 = (Graphics2D) g2.create();
        g3.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,bdc.getAlpha()));
        g3.translate(pt.getX(),pt.getY());
        g3.translate(size.width/2,size.height/2);
        g3.rotate(bdc.getRotation(),0,0);
        g3.translate(-size.width/2,-size.height/2);
        g3.scale(bdc.getScale(),bdc.getScale());
        
        // only draw if in the clip rect
        if(g3.getClip().intersects(0,0,bdc.getBuffer().getWidth(),
                bdc.getBuffer().getHeight())) {
            Graphics2D gimg = (Graphics2D) g3.create();
            if(bdc.getClip() != null) {
                gimg.setClip(bdc.getClip());
            }
            gimg.drawImage(bdc.getBuffer(), 0, 0, null);
            gimg.dispose();
        }
        
        if (this.bufferedWM.DEBUG_BORDERS) {
            if (wasDirty) {
                g3.setColor(Color.CYAN);
            }  else {
                g3.setColor(Color.BLACK);
            }
            g3.setStroke(new BasicStroke(3));
            g3.drawRect(0, 0, size.width, size.height);
        }
        
        if(this.bufferedWM.DEBUG_BORDERS) {
            g3.setColor(Color.GRAY);
            String text = pt.getX() + "," + pt.getY() + " - " + bdc.getSize().getWidth() + "x" + bdc.getSize().getHeight();
            g3.drawString(text,5,16);
            g3.setColor(Color.WHITE);
            g3.drawString(text,6,17);
            g3.dispose();
        }
    }
    
    private BufferedImage drawToBuffer(final JComponent comp, final Dimension size, BufferedImage img) {
        // decide if we need to create a new image
        if(img == null ||
                img.getWidth() != (int)size.getWidth() ||
                img.getHeight() != (int)size.getHeight()) {
            img = GraphicsUtilities.createCompatibleTranslucentImage((int)size.getWidth(), (int)size.getHeight());
            //, BufferedImage.TYPE_INT_ARGB);
        }
        
        // draw the component into the image
        Graphics gx = img.getGraphics();
        rendererPane.add(comp);
        rendererPane.paintComponent(gx, comp, this,
                0,0,  size.width, size.height,
                true);
        
        // draw debugging info
        if (this.bufferedWM.DEBUG_BORDERS) {
            gx.setColor(Color.GREEN);
            gx.drawLine(0,0,size.width,size.height);
            gx.drawLine(size.width,0,0,size.height);
        }
        //gx.setColor(Color.RED);
        //gx.drawString("comps: "+ comp.getComponentCount(),2,15);
        
        // dispose and return
        gx.dispose();
        return img;
    }
    
    public boolean isAnimating() {
        return animating;
    }
    
    public void setAnimating(boolean animating) {
        this.animating = animating;
    }
    
    private void drawSurface(Graphics2D g, BSurface s, BufferedDeskletContainer bdc) {
        Point2D pt = s.getLocation();
        Dimension2D size = s.getSize();
        
        Graphics2D g3 = (Graphics2D) g.create();
        g3.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,s.getAlpha()));
        //u.p("size = " + size);
        g3.translate(pt.getX(),pt.getY());
        g3.translate( size.getWidth()/2, size.getHeight()/2);
        g3.rotate(s.getRotation(),0,0);
        g3.translate(-size.getWidth()/2,-size.getHeight()/2);
        
        if(s.getImg() != null) {
            g3.drawImage(s.getImg(),0,0,null);
        }
        
        if(s.subRect != null) {
            int w = (int)size.getWidth();
            int h = (int)size.getHeight();
            g3.drawImage(bdc.getBuffer(),0,0,w,h,
                    s.subRect.x, s.subRect.y,
                    s.subRect.x+ s.subRect.width,
                    s.subRect.y + s.subRect.height,
                    null);
        }
        //g3.scale(bdc.getScale(),bdc.getScale());
        g3.dispose();
        
    }
    
}