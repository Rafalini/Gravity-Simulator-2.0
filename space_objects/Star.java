package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;

import display.DisplayConvert;
import display.shapes.Arrow;

//Basic extension of SpaceObject, extends SpaceObject only by specyfic painting function.

public class Star extends SpaceObject implements Cloneable
{
    HashMap<Long, Double> neighbors; //id, range - to spaceobj with this id

    public Star (int x, int y, int mass, int radius)
    {
        super(x,y,mass,radius);
    }
    public Star(Star obj)
    {
        super(obj);
    }
    public Star(SpaceObject obj)
    {
        super(obj);
    }

    public Star clone() { return new Star(this); }

    public void setNeighbors(HashMap<Long, Double> newNeighbors) {neighbors = newNeighbors;}

    public void impact()
    {
        
    }
    public void performIteration()
    {

    }

    public synchronized void paintObject(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset, double zoom_amount, int mode)
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        switch(mode)
        {
            case 0:         g2.setColor(Color.YELLOW);
                            g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth, Xoffset, Zoom),
                                                            DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight, Yoffset, Zoom), radius*Zoom, radius*Zoom));
            break;
            case 1:          g2.setColor(Color.BLACK);
                             g2.draw(new Ellipse2D.Double(  DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth, Xoffset, Zoom),
                                                            DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight, Yoffset, Zoom), radius*Zoom, radius*Zoom));
            break;
            case 2:         g2.setColor(Color.RED); break;
        }
        Arrow.printArrow(g2, (int)(Xpos), (int)(Ypos), (int)(Xpos+Xvel), (int)(Ypos+Yvel), panelWidth, panelHeight, Xoffset, Yoffset, Zoom, mode);
    }
}