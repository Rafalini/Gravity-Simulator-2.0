package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import display.DisplayConvert;
import display.shapes.Arrow;
import display.*;

//Basic extension of SpaceObject, extends SpaceObject only by specyfic painting function.

public class Star extends SpaceObject implements Cloneable
{
    int [] xCoords; //light shape
    int [] yCoords;
    int nCoords=0;

    public int getN() {return nCoords;}
    public void setLightShape(ArrayList<Integer> xCoord, ArrayList<Integer>yCoord, display.Menu myMenu, SpaceMap myMap)
    {   
        xCoords = new int[xCoord.size()];
        yCoords = new int[yCoord.size()];
        nCoords = xCoord.size();
        for(int i=0; i<xCoord.size(); i++)
        {
            xCoords[i]=(int)DisplayConvert.XforPrint(xCoord.get(i), (int)myMap.getMapSize().getWidth(), myMap.getXoffset(), myMap.getZoom());
            yCoords[i]=(int)DisplayConvert.YforPrint(yCoord.get(i), (int)myMap.getMapSize().getHeight(), myMap.getXoffset(), myMap.getZoom());
        }
    }
    public int [] getXcoords() {return xCoords;}
    public int [] getYcoords() {return yCoords;}

    public Star (int x, int y, int xvel, int yvel, int mass, int radius)
    {
        super(x,y,mass,radius);
        Xvel = xvel;
        Yvel = yvel;
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
            case 1:         g2.setColor(Color.BLACK);
                            g2.draw(new Ellipse2D.Double(  DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth, Xoffset, Zoom),
                                                            DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight, Yoffset, Zoom), radius*Zoom, radius*Zoom));
                            Arrow.printArrow(g2, (int)(Xpos), (int)(Ypos), (int)(Xpos+0.5*Xvel), (int)(Ypos+0.5*Yvel), panelWidth, panelHeight, Xoffset, Yoffset, Zoom, mode);
            break;
            case 2:         g2.setColor(Color.RED); break;
        }
    }

    public synchronized void printLight(Graphics2D g2)
    {
        g2.setColor(Color.BLUE);
        g2.fillPolygon(xCoords, yCoords, nCoords);
    }
}