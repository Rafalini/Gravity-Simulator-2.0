package display.shapes;

import java.awt.*;
import java.awt.geom.*;
import java.lang.Math;

import display.DisplayConvert;

public class Arrow {
    //      p2(x2,y2)   /|\
    //      s(xs,ys)   / | \
    //      height       |
    //      p1(x1,y1)    |

                                            //Xstart, Ystart, Xtarget, Ytarget
    public static void printArrow(Graphics2D g2, int x1, int y1, int x2, int y2, int width, int height, int xoffset, int yoffset, double zoom_amount, int mode)
    {
        //double D = Math.sqrt( Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) );
        double D = 7; //height of triangle
        double H = 1 - 20 / (Math.sqrt( Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))); //height to general length ratio in order to use vector addition
        double W = 1; 
        double Xs = (x1 + H * (x2 - x1)),     Ys = (y1 + H * (y2 - y1));

        double zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             zoom = 1/zoom;

        switch(mode)
        {
            case 0:         g2.setColor(Color.BLUE);  break;
            case 1:         g2.setColor(new Color(63,216, 212)); break;
            case 2:         g2.setColor(Color.RED); break;
        }
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(  DisplayConvert.XforPrint(x1, width, xoffset, zoom), 
                                    DisplayConvert.YforPrint(y1, height, yoffset, zoom), 
                                    DisplayConvert.XforPrint(x2, width, xoffset, zoom),
                                    DisplayConvert.YforPrint(y2, height, yoffset, zoom)));
        if(y1 == y2)    //vertical arrow
        {
            int [] xcoord = {(int) DisplayConvert.XforPrint(x2, width, xoffset, zoom),
                             (int) DisplayConvert.XforPrint(Xs, width, xoffset, zoom),
                             (int) DisplayConvert.XforPrint(Xs, width, xoffset, zoom)};
            int [] ycoord = {(int) DisplayConvert.YforPrint(y2, height, yoffset, zoom),
                             (int) DisplayConvert.YforPrint(Ys+ W*D, height, yoffset, zoom),
                             (int) DisplayConvert.YforPrint(Ys- W*D, height, yoffset, zoom)};
            g2.fillPolygon(xcoord, ycoord, 3);
        }
        else 
        {
            double M = (double)(x2 - x1)/(double)(y2 - y1);
            double Xp1 =  Xs + W*D *Math.sqrt( 1/(Math.pow(M,2) + 1) );
            double Yp1 =  M*(Xs - Xp1) + Ys;
            double Xp2 =  Xs - W*D *Math.sqrt( 1/(Math.pow(M,2) + 1) );
            double Yp2 =  M*(Xs - Xp2) + Ys;
           
            int [] xcoord = {(int) DisplayConvert.XforPrint(x2, width, xoffset, zoom),
                             (int) DisplayConvert.XforPrint(Xp1, width, xoffset, zoom),
                             (int) DisplayConvert.XforPrint(Xp2, width, xoffset, zoom)};
            int [] ycoord = {(int) DisplayConvert.YforPrint(y2, height, yoffset, zoom),
                             (int) DisplayConvert.YforPrint(Yp1, height, yoffset, zoom),
                             (int) DisplayConvert.YforPrint(Yp2, height, yoffset, zoom)};
            g2.fillPolygon(xcoord, ycoord, 3);
         }
        g2.setStroke(new BasicStroke(1));
    }
}