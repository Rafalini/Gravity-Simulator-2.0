package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;

import display.DisplayConvert;
import display.shapes.Arrow;

//Basic extension of SpaceObject, extends SpaceObject only by specyfic painting function.

public class Planet extends SpaceObject implements Cloneable
{
    HashMap<Long, Double> neighbors; //id, range - to spaceobj with this id

    public Planet (final int x, final int y, final int Vx, final int Vy, final int mass, final int radius)
    {
        super(x,y,mass,radius);
        Xvel = Vx;
        Yvel = Vy;
    }
    public Planet(final Planet obj)
    {
        super(obj);
    }
    public Planet(final SpaceObject obj)
    {
        super(obj);
    }

    public Planet clone() { return new Planet(this); }

    public void setNeighbors(final HashMap<Long, Double> newNeighbors) {neighbors = newNeighbors;}

    public void impact()
    {
        
    }
    public void performIteration()
    {

    }

    public synchronized void paintObject(final Graphics2D g2, final int panelWidth, final int panelHeight, final int Xoffset, final int Yoffset, final double zoom_amount, final int mode)
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        switch(mode)
        {
            case 0:         g2.setColor(Color.RED);
                            g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth, Xoffset, Zoom),
                                                            DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight,Yoffset, Zoom), radius*Zoom, radius*Zoom));
            break;
            case 1:         g2.setColor(Color.BLACK);
                            g2.draw(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth, Xoffset, Zoom),
                                                            DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight,Yoffset, Zoom), radius*Zoom, radius*Zoom));
                            Arrow.printArrow(g2, (int)(Xpos), (int)(Ypos), (int)(Xpos+0.5*Xvel), (int)(Ypos+0.5*Yvel), panelWidth, panelHeight, Xoffset, Yoffset, Zoom, mode);
                            g2.setColor(Color.BLACK);
                            g2.drawString("M: "+Mass,   (int)DisplayConvert.XforPrint(Xpos- 0.5*radius, panelWidth, Xoffset, Zoom),
                                                        (int)DisplayConvert.YforPrint(Ypos+ 0.5*radius, panelHeight,Yoffset, Zoom));
            break;
            case 2:         g2.setColor(Color.RED); break;
        }
    }
}