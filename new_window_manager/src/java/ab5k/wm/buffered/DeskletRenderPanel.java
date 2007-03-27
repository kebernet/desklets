package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.CellRendererPane;
import javax.swing.JPanel;
import org.joshy.util.u;


// this just lays out components using their preferred size. it does not
// move them around at all.
// it also leaves the parent at whatever size it was set at

class DeskletRenderPanel extends JPanel {
    private final BufferedWM bufferedWM;
    
    
    private CellRendererPane rendererPane;
    private boolean animating = false;
    
    
    public DeskletRenderPanel(BufferedWM bufferedWM) {
        super();
        this.bufferedWM = bufferedWM;
        rendererPane = new CellRendererPane();
        add(rendererPane);
        this.setLayout(new AbsLayout());
    }
    
    
    
    protected void paintComponent(Graphics g) {
        //u.p("repainting main panel");
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g;
        
        g.setColor(Color.YELLOW);
        if(isAnimating()) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        }
        for(DeskletContainer dc : this.bufferedWM.desklets) {
            if(dc instanceof BufferedDeskletContainer) {
                BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
                Dimension size = new Dimension((int) bdc.getSize().getWidth(), (int) bdc.getSize().getHeight());
                Point pt = bdc.getLocation();
                boolean wasDirty = false;
                if (bdc.getBuffer() == null || bdc.isDirty()) {
                    BufferedImage img = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics gx = img.getGraphics();
                    rendererPane.add(bdc.comp);
                    rendererPane.paintComponent(gx, bdc.comp, this,
                            0,0,  size.width, size.height,
                            true);
                    if (this.bufferedWM.DEBUG_BORDERS) {
                        gx.setColor(Color.GREEN);
                        gx.drawLine(0,0,size.width,size.height);
                        gx.drawLine(size.width,0,0,size.height);
                    }
                    gx.dispose();
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
                g3.translate(pt.x,pt.y);
                g3.translate(size.width/2,size.height/2);
                g3.rotate(bdc.getRotation(),0,0);
                g3.translate(-size.width/2,-size.height/2);
                g3.scale(bdc.getScale(),bdc.getScale());
                
                if(g3.getClip().intersects(0,0,bdc.getBuffer().getWidth(),bdc.getBuffer().getHeight())) {
                    g3.drawImage(bdc.getBuffer(), 0, 0, null);
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
                    String text = pt.x + "," + pt.y + " - " + bdc.getSize().getWidth() + "x" + bdc.getSize().getHeight();
                    g3.drawString(text,5,16);
                    g3.setColor(Color.WHITE);
                    g3.drawString(text,6,17);
                    g3.dispose();
                }
                /*
                 
                // draw the close boxes
                Graphics2D g4 = (Graphics2D) g2.create();
                g4.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g4.translate(pt.x,pt.y);
                g4.translate(-30,0);
                g4.setColor(Color.BLACK);
                g4.setStroke(new BasicStroke(4));
                g4.drawOval(0,0,20,20);
                g4.drawLine(0,0,20,20);
                g4.drawLine(20,0,0,20);
                g4.dispose();*/
            }
        }
        /*
        if (this.bufferedWM.DEBUG_REPAINT_AREA) {
            u.p("clip = " + g2.getClip());
            g2.setColor(Color.GREEN);
            g2.draw(g2.getClip());
        }*/
        
    }
    
    public boolean isAnimating() {
        return animating;
    }
    
    public void setAnimating(boolean animating) {
        this.animating = animating;
    }
}