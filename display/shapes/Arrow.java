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
    public static void printArrow(Graphics2D g2, int x1, int y1, int x2, int y2, int width, int height)
    {
        //double D = Math.sqrt( Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) );
        double D = 7; //height of triangle
        double H = 1 - 20 / (Math.sqrt( Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))); //height to general length ratio in order to use vector addition
        double W = 1; 
        double Xs = (x1 + H * (x2 - x1)),     Ys = (y1 + H * (y2 - y1));

        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(  DisplayConvert.XforPrint(x1, width), 
                                    DisplayConvert.YforPrint(y1, height), 
                                    DisplayConvert.XforPrint(x2, width),
                                    DisplayConvert.YforPrint(y2, height)));
        if(y1 == y2)
        {
            /*g2.draw(new Line2D.Double(  DisplayConvert.XforPrint(x2, width),
                                        DisplayConvert.YforPrint(y2, height),
                                        DisplayConvert.XforPrint(Xs, width),
                                        DisplayConvert.YforPrint(Ys+ W*D, height)));
            g2.draw(new Line2D.Double(  DisplayConvert.XforPrint(x2, width),
                                        DisplayConvert.YforPrint(y2, height),
                                        DisplayConvert.XforPrint(Xs, width),
                                        DisplayConvert.YforPrint(Ys- W*D, height)));*/

            int [] xcoord = {(int) DisplayConvert.XforPrint(x2, width),
                             (int) DisplayConvert.XforPrint(Xs, width),
                             (int) DisplayConvert.XforPrint(Xs, width)};
            int [] ycoord = {(int) DisplayConvert.YforPrint(y2, height),
                             (int) DisplayConvert.YforPrint(Ys+ W*D, height),
                             (int) DisplayConvert.YforPrint(Ys- W*D, height)};
            g2.fillPolygon(xcoord, ycoord, 3);
        } else {
            double M = (double)(x2 - x1)/(double)(y2 - y1);
            double Xp1 =  Xs + W*D *Math.sqrt( 1/(Math.pow(M,2) + 1) );
            double Yp1 =  M*(Xs - Xp1) + Ys;
            double Xp2 =  Xs - W*D *Math.sqrt( 1/(Math.pow(M,2) + 1) );
            double Yp2 =  M*(Xs - Xp2) + Ys;
            /*g2.draw(new Line2D.Double(  DisplayConvert.XforPrint(x2, width),
                                        DisplayConvert.YforPrint(y2, height),
                                        DisplayConvert.XforPrint(Xp1, width),
                                        DisplayConvert.YforPrint(Yp1, height)));
            g2.draw(new Line2D.Double(  DisplayConvert.XforPrint(x2, width),
                                        DisplayConvert.YforPrint(y2, height),
                                        DisplayConvert.XforPrint(Xp2, width),
                                        DisplayConvert.YforPrint(Yp2, height)));*/
            int [] xcoord = {(int) DisplayConvert.XforPrint(x2, width),
                             (int) DisplayConvert.XforPrint(Xp1, width),
                             (int) DisplayConvert.XforPrint(Xp2, width)};
            int [] ycoord = {(int) DisplayConvert.YforPrint(y2, height),
                             (int) DisplayConvert.YforPrint(Yp1, height),
                             (int) DisplayConvert.YforPrint(Yp2, height)};
            g2.fillPolygon(xcoord, ycoord, 3);
         }
        g2.setStroke(new BasicStroke(1));
    }
}