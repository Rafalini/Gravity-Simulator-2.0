package space_objects;

import java.awt.*;
import java.awt.geom.*;

import display.DisplayConvert;

public class Planet extends SpaceObject
{
    private int radius;

    public Planet(SpaceObject obj, int R)
    {
        super(obj);
        radius = R;
    }

    public void setRadius(int R)
    {
        if(R>0)
            radius = R;
        else
            radius = 100;
    }

    public void paintObject(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {
        g2.setColor(Color.RED);
        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight), radius, radius));
    }
}