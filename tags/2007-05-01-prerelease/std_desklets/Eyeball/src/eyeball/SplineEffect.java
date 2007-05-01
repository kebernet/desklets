/*
 * HackedEffect.java
 *
 * Created on February 10, 2007, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eyeball;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import org.jdesktop.swingx.painter.effects.AbstractAreaEffect;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class SplineEffect extends AbstractAreaEffect {
    float x1, y1, x2, y2;
    SplineEffect(float x1, float y1, float x2, float y2) {
        this.setRenderInsideShape(true);
        this.setShapeMasked(true);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    protected void paintBorderGlow(Graphics2D g2, Shape clipShape, int width, int height) {
        //float effectWidth = 50f;
        float effectWidth = getEffectWidth();
        float steps = getBrushSteps();//float steps = 100f;
        float alpha = 1f/steps;
        //g2.setColor(new Color(0,0,0,alpha));
        g2.setColor(getBrushColor());//Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, alpha));
        for(float i=0; i<steps; i=i+1f) {
            float brushWidth = calcBrushWidth(i,steps, effectWidth);
            if(brushWidth >=0 ) {
                g2.setStroke(new BasicStroke(brushWidth,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(clipShape);
            }
        }
    }
    /*
    public void apply(Graphics2D g2, Shape clipShape, int width, int height) {
        u.p("drawing");
        float effectWidth = 50f;
        float steps = 100f;
        float alpha = 1f/steps;
        g2.setColor(new Color(0,0,0,alpha));
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, alpha));
        for(float i=0; i<steps; i=i+1f) {
            float brushWidth = calcBrushWidth(i,steps, effectWidth);
            if(brushWidth >=0 ) {
                g2.setStroke(new BasicStroke(brushWidth,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(clipShape);
            }
        }
    }
     */
    // linear: bw = i * width
    private float calcBrushWidth(float i, float steps, float effectWidth) {
        float width = effectWidth/steps;
        float x = 0;
        float y = i/steps;
        x = y;//(float)Math.sqrt(y) + y*y -y;
        
        
        float t = i/steps;
        //Point2D.Float pt = getXY(t,0,0,1,1);
        //Point2D.Float pt = getXY(t,0,1,1,1);
        //Point2D.Float pt = getXY(t,0,1,0.07f,1);
        Point2D.Float pt = getXY(t,x1,y1,x2,y2);
        x = (float)pt.getX();//*t;
        
        return x*effectWidth;
    }
    
    /**
     * Calculates the XY point for a given t value.
     *
     * The general spline equation is:
     *   x = b0*x0 + b1*x1 + b2*x2 + b3*x3
     *   y = b0*y0 + b1*y1 + b2*y2 + b3*y3
     * where:
     *   b0 = (1-t)^3
     *   b1 = 3 * t * (1-t)^2
     *   b2 = 3 * t^2 * (1-t)
     *   b3 = t^3
     * We know that (x0,y0) == (0,0) and (x1,y1) == (1,1) for our splines,
     * so this simplifies to:
     *   x = b1*x1 + b2*x2 + b3
     *   y = b1*x1 + b2*x2 + b3
     *
     * @param t parametric value for spline calculation
     */
    private static Point2D.Float getXY(float t, float x1, float y1, float x2, float y2) {
        Point2D.Float xy;
        float invT = (1 - t);
        float b1 = 3 * t * (invT * invT);
        float b2 = 3 * (t * t) * invT;
        float b3 = t * t * t;
        xy = new Point2D.Float((b1 * x1) + (b2 * x2) + b3, (b1 * y1) + (b2 * y2) + b3);
        return xy;
    }

}
