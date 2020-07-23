package functions;

import java.awt.Dimension;

public class GeomFunctions {
   public static double pointsDistance (Dimension p1, Dimension p2)
   {
       return Math.hypot(p1.getWidth()-p2.getWidth(), p1.getHeight()-p2.getHeight());
   } 
}