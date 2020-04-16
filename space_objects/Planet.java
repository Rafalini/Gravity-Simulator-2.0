package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.concurrent.*;

import display.DisplayConvert;

public class Planet extends SpaceObject implements Cloneable
{
    private int radius;

    private Semaphore readFreeWriteOneRadi = new Semaphore(1,true);

    public Planet(SpaceObject obj, int R)
    {
        super(obj);
        radius = R;
    }

    public Planet(Planet obj)
    {
        super(obj);
        radius = obj.radius; //ale radius jest prywatny 0.o
    }

    public Planet clone() { return new Planet(this); }

    public int getRadius () { return radius; }

    public void setRadius(int R)
    {
        try {readFreeWriteOneRadi.acquire();}
        catch(Exception e) {}
        radius = R;
        readFreeWriteOneRadi.release();
    }

    public synchronized void paintObject(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight), radius, radius));
    }
}