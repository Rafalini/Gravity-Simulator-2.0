package space_objects;

import java.awt.*;
import java.awt.geom.*;

import display.DisplayConvert;
import display.shapes.Arrow;

public class Planet extends SpaceObject implements Cloneable
{
    public Planet (int x, int y, int mass, int radius)
    {
        super(x,y,mass,radius);
    }
    public Planet(Planet obj)
    {
        super(obj);
    }

    public Planet clone() { return new Planet(this); }

    public synchronized void paintObject(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight), radius, radius));
        Arrow.printArrow(g2, (int)Xpos, (int)Ypos, (int)(Xpos+Xvel), (int)(Ypos+Yvel), panelWidth, panelHeight);
    }
}