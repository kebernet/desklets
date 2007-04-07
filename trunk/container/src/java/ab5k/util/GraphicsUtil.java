/*
 * GraphicsUtil.java
 *
 * Created on March 27, 2007, 4:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.util;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author joshy
 */
public final class GraphicsUtil {
    
    private GraphicsUtil() {
    }
    
    public static Point toPoint(Point2D point2D) {
        if(point2D instanceof Point) {
            return (Point)point2D;
        } else {
            return new Point((int)point2D.getX(),(int)point2D.getY());
        }
    }
}
