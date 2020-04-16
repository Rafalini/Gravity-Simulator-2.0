package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.concurrent.Semaphore;

import display.DisplayConvert;
import display.shapes.Arrow;

public class SpaceObject implements SpacePrintable//multithread safe
{
    protected double Xpos, Ypos;
    protected double Xvel, Yvel;
    protected int Mass; //can be 0
    private int dotradius=5; //only for printing dot

    private Semaphore readFreeWriteOneXpos = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneYpos = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneXvel = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneYvel = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneMass = new Semaphore(Integer.MAX_VALUE,true);

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

    public SpaceObject clone() { return new SpaceObject(this); }
    
//getters - each getter takes one permit (from Integer.MAX_VALUE), multiple (Integer.MAX_VALUE) reads are available at the time
    public final double getXpos ()
    {  
        readFreeWriteOneXpos.acquireUninterruptibly();
        double tmp = Xpos;
        readFreeWriteOneXpos.release();
        return tmp;
    }
    public final double getYpos ()
    {  
        readFreeWriteOneYpos.acquireUninterruptibly();
        double tmp = Ypos;
        readFreeWriteOneYpos.release();
        return tmp;
    }
    public final int getMass ()
    {  
        readFreeWriteOneMass.acquireUninterruptibly();
        int tmp = Mass;
        readFreeWriteOneMass.release();
        return tmp;
    }
    public final double getXvel ()
    {  
        readFreeWriteOneXvel.acquireUninterruptibly();
        double tmp = Xvel;
        readFreeWriteOneXvel.release();
        return tmp;
    }
    public final double getYvel ()
    {  
        readFreeWriteOneYvel.acquireUninterruptibly();
        double tmp = Yvel;
        readFreeWriteOneYvel.release();
        return tmp;
    }
//setters - when value is set all Semaphore premits are aquired (only write can happen at the time)
    public final void newXpos (double X)
    {
        try {readFreeWriteOneXpos.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Xpos = X; readFreeWriteOneXpos.release(Integer.MAX_VALUE);
    }
    public final void newYpos (double Y)
    {
        try{readFreeWriteOneYpos.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Ypos = Y; readFreeWriteOneYpos.release(Integer.MAX_VALUE);
    }
    public final void newMass (int M)
    {
        try{readFreeWriteOneMass.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Mass = M; readFreeWriteOneMass.release(Integer.MAX_VALUE);
    }

    public final void newXvel (double Vx)
    {
        try{readFreeWriteOneXvel.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Xvel = Vx - Xpos; readFreeWriteOneXvel.release(Integer.MAX_VALUE);
    }
    public final void newYvel (double Vy)
    {
        try{readFreeWriteOneYvel.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Yvel = Vy - Ypos; readFreeWriteOneYvel.release(Integer.MAX_VALUE);
    }

    public synchronized void updatePos()
    {
        newXpos( getXpos() + 1 );
        newYpos( getYpos() + 1 );
    }

//only one printer can be called at a time
    public synchronized void paintObject  (Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {
        g2.setColor(Color.WHITE);
        g2.draw(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 5*dotradius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + 5*dotradius, panelHeight), 10*dotradius, 10*dotradius));
                                        
        Arrow.printArrow(g2, (int)Xpos, (int)Ypos, (int)(Xpos+Xvel), (int)(Ypos+Yvel), panelWidth, panelHeight);

        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - dotradius, panelWidth),
                                        DisplayConvert.YforPrint(Ypos + dotradius, panelHeight), 2*dotradius, 2*dotradius));
    }
    public synchronized void paintHighlight(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset)
    {}
}