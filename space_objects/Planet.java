package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;

import display.DisplayConvert;
import display.shapes.Arrow;

public class Planet extends SpaceObject implements Cloneable
{
    HashMap<Long, Double> neighbors; //id, range - to spaceobj with this id

    public Planet (int x, int y, int mass, int radius)
    {
        super(x,y,mass,radius);
    }
    public Planet(Planet obj)
    {
        super(obj);
    }

    public Planet clone() { return new Planet(this); }

    public void setNeighbors(HashMap<Long, Double> newNeighbors) {neighbors = newNeighbors;}

    public void impact()
    {
        
    }
    public void performIteration()
    {

    }

    public synchronized void paintObject(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight), radius, radius));
        Arrow.printArrow(g2, (int)Xpos, (int)Ypos, (int)(Xpos+Xvel), (int)(Ypos+Yvel), panelWidth, panelHeight);
    }
}