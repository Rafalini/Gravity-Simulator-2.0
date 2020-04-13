package space_objects;

import java.awt.*;
import java.awt.geom.*;

import display.DisplayConvert;
import display.shapes.Arrow;

public class SpaceObject implements SpacePrintable
{
    protected int Xpos, Ypos;
    protected double Xvel, Yvel;
    private int Mass; //can be 0
    private int dotradius=5;

    public SpaceObject (int x, int y)
    {
        Xpos = x;  Ypos = y;
        Xvel = 0; Yvel = 0;
    }

    public SpaceObject (SpaceObject obj)
    {
        Xpos = obj.getXpos();  Ypos = obj.getYpos();
        Xvel = obj.getXvel();  Yvel = obj.getYvel();
    }
//getters
    public final int getXpos () { return Xpos;}
    public final int getYpos () { return Ypos;}
    public final int getMass () { return Mass;}

    public final double getXvel () { return Xvel;}
    public final double getYvel () { return Yvel;}
//setters
    public final void newXpos (int X) {Xpos = X;}
    public final void newYpos (int Y) {Ypos = Y;}
    public final void newMass (int M) {Mass = M;}

    public final void newXvel (int Vx) {Xvel = Vx - Xpos;}
    public final void newYvel (int Vy) {Yvel = Vy - Ypos;}

    public void paintObject  (Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {
        g2.setColor(Color.WHITE);
        g2.draw(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 5*dotradius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + 5*dotradius, panelHeight), 10*dotradius, 10*dotradius));
                                        
        Arrow.printArrow(g2, Xpos, Ypos, (int)(Xpos+Xvel), (int)(Ypos+Yvel), panelWidth, panelHeight);

        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - dotradius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + dotradius, panelHeight), 2*dotradius, 2*dotradius));
    }
    public void paintHighlight(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {}
}