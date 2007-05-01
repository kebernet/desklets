package org.joshy.countdown;
import javax.swing.*;
import java.awt.*;
import java.awt.font.*;

public class AALabel extends JLabel {
    private int tracking;
    private int digits;
    public AALabel(String text, int tracking, int digits) {
        super(text);
        this.tracking = tracking;
        this.digits = digits;
    }
    public void paintComponent(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        String text = getText();
        while(text.length() < digits) {
            text = "0"+text;
        }
        char[] chars = text.toCharArray();

        FontMetrics fm = this.getFontMetrics(getFont());
        int h = fm.getAscent();
        LineMetrics lm = fm.getLineMetrics(getText(),g);
        g.setFont(getFont());
        
        int x = 0;
        
        for(int i=0; i<chars.length; i++) {
            char ch = chars[i];
            int w = fm.charWidth(ch) + tracking;
            g.setColor(Color.white);
            g.drawString(""+chars[i],x,h);
            g.setColor(Color.black);
            g.drawString(""+chars[i],x-1,h-1);
            x+=w;
        }
        
    }
}
