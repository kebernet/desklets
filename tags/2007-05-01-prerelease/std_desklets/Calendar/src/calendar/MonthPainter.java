package calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.joda.time.DateTime;

// End of variables declaration

class MonthPainter extends AbstractPainter<JXPanel> {
    
    private CalendarForm form;
    MonthPainter(CalendarForm form) {
        this.form = form;
    }
    protected void doPaint(Graphics2D g, JXPanel component, int width, int height) {
        
        DateTime now = new DateTime(form.getDate());
        DateTime firstOfMonth = now.dayOfMonth().withMinimumValue();
        int dayOfWeek = firstOfMonth.getDayOfWeek();
        
        DateTime date = firstOfMonth.minusDays(dayOfWeek);
        
        // the width of each column
        int wsize = width/7;
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setFont(new Font("Sanserif",Font.BOLD,16));
        // print week headers
        for(int day=0; day<7; day++) {
            drawDayOfWeek(g2,date,wsize);
            date = date.plusDays(1);
            g2.translate(wsize,0);
            if(day != 6) {
                g2.setColor(new Color(120,0,0));
                g2.drawLine(1,3, 1,height-15);
            }
        }
        g2.dispose();
        
        // print each day
        g.translate(0,20);
        g.setFont(new Font("Sanserif",Font.BOLD,13));
        date = firstOfMonth.minusDays(dayOfWeek);
        int hsize = height/7;
        for(int week=0; week<6; week++) {
            for(int day=0; day<7; day++) {
                g.translate(day*wsize,week*hsize);
                if(date.getMonthOfYear() == firstOfMonth.getMonthOfYear()) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                drawDay(g,date,wsize);
                date = date.plusDays(1);
                g.translate(-day*wsize,-week*hsize);
            }
        }
        g.translate(0,-30);
    }
    
    private void drawDayOfWeek(Graphics2D g, DateTime date, int width) {
        String str = date.dayOfWeek().getAsShortText().substring(0,1);
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(str,g);
        g = (Graphics2D) g.create();
        g.setColor(Color.GRAY);
        g.drawString(str,(width-(float)bounds.getWidth())-5,15f);
    }
    
    private void drawDay(Graphics2D g, DateTime date, int width) {
        String str = date.dayOfMonth().getAsString();
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(str,g);
        g = (Graphics2D) g.create();
        g.translate(bounds.getX(),bounds.getY());
        
        DateTime now = new DateTime();
        if(date.getMonthOfYear() == now.getMonthOfYear() &&
                date.getDayOfMonth() == now.getDayOfMonth() &&
                date.getYear() == now.getYear()) {
            
            int ins = 2;
            g.setColor(Color.BLACK);
            RoundRectangle2D shape = new RoundRectangle2D.Double(
                    width-bounds.getX()-bounds.getWidth()-5-ins,-bounds.getY()+1,
                    bounds.getWidth()+ins*2,bounds.getHeight()+ins*2,
                    5,5);
            g.fill(shape);
            g.setColor(Color.WHITE);
            g.draw(shape);
        }
        
        g.setColor(Color.WHITE);
        g.drawString(str,width-(float)bounds.getWidth()-5,(float)bounds.getHeight()-(float)bounds.getY());
    }
    
    private static void p(String s) {
        System.out.println(s);
    }
    
}