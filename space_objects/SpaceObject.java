package space_objects;

import java.awt.*;
import java.awt.geom.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Math;

import display.DisplayConvert;
import display.shapes.Arrow;
import constants.Constants;

//Basic draft of space object, contains position, mass, getters and setters and painting information.
//Class is thread safe, everyone can read anytime, but only one can write and locks that value while writting,
//that it cant be read while writting.

public class SpaceObject implements SpacePrintable
{
    private static AtomicLong idCounter = new AtomicLong(0); //unique ID generator 
    protected Long objectId;        //unique ID

    protected double Xpos, Ypos;
    protected double Xvel, Yvel;
    protected int Mass=0;             //can be 0
    protected int radius;             //range of direct influence

    private Semaphore readFreeWriteOneXpos = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneYpos = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneXvel = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneYvel = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneMass = new Semaphore(Integer.MAX_VALUE,true);
    private Semaphore readFreeWriteOneRadi = new Semaphore(Integer.MAX_VALUE,true);

    public SpaceObject (int x, int y, int mass, int radius)
    {
        objectId = idCounter.addAndGet(1);
        Xpos = x;  Ypos = y;
        Xvel = 0; Yvel = 0;
        this.Mass = mass;
        this.radius = radius;
    }

    public SpaceObject (SpaceObject obj) //for clonning
    {
        objectId = obj.getObjId();
        Xpos = obj.getXpos();  Ypos = obj.getYpos();
        Mass = obj.getMass();  radius = obj.getRadius();
        Xvel = obj.getXvel();  Yvel = obj.getYvel();
    }

    public SpaceObject clone() { return new SpaceObject(this); }
    public long getObjId() {return objectId;}
    
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
    public final int getRadius ()
    {  
        readFreeWriteOneRadi.acquireUninterruptibly();
        int tmp = radius;
        readFreeWriteOneRadi.release();
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
    public final void setRadius (int r)
    {
        try{readFreeWriteOneRadi.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        radius = r; readFreeWriteOneRadi.release(Integer.MAX_VALUE);
    }

    public final void updateXvel (double Vx)
    {
        try{readFreeWriteOneXvel.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Xvel += Vx; readFreeWriteOneXvel.release(Integer.MAX_VALUE);
    }
    public final void updateYvel (double Vy)
    {
        try{readFreeWriteOneYvel.acquire(Integer.MAX_VALUE);}
        catch(Exception e) {}
        Yvel += Vy; readFreeWriteOneYvel.release(Integer.MAX_VALUE);
    }

    public synchronized void updatePos(double time)
    {
        newXpos( getXpos() + Constants.VelFactor*getXvel()*time );
        newYpos( getYpos() + Constants.VelFactor*getYvel()*time );
    }

    public void performIteration() {}

    public void impact(SpaceObject obj)
    {
        int massOfThisInAMomment = (int)(this.Mass + obj.Mass*(1-Constants.Mloss));
        this.Xpos = (this.Xpos*this.Mass + obj.getXpos()*obj.getMass())/ massOfThisInAMomment;
        this.Ypos = (this.Ypos*this.Mass + obj.getYpos()*obj.getMass())/ massOfThisInAMomment;
        this.Xvel = (this.Xvel*this.Mass + obj.getXvel()*obj.getMass())/ massOfThisInAMomment;
        this.Yvel = (this.Yvel*this.Mass + obj.getYvel()*obj.getMass())/ massOfThisInAMomment;
        this.radius = (int)(Math.sqrt( Math.pow(this.radius,2)
                                     + Math.pow(obj.getRadius(),2)) );

        this.Mass = massOfThisInAMomment;
    }

//only one printer can be called at a time
    public synchronized void paintObject  (Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset, double zoom_amount, int mode)
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        switch(mode)
        {
            case 0:         g2.setColor(Color.WHITE);  break;
            case 1:         g2.setColor(Color.BLACK); break;
            case 2:         g2.setColor(Color.RED); break;
        }
        g2.draw(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*radius, panelWidth, Xoffset, Zoom),
                                        DisplayConvert.YforPrint(Ypos + 0.5*radius, panelHeight, Yoffset, Zoom), radius*Zoom, radius*Zoom));
                                        
        Arrow.printArrow(g2, (int)(Xpos), (int)(Ypos), (int)(Xpos+Xvel), (int)(Ypos+Yvel), panelWidth, panelHeight, Xoffset, Yoffset, Zoom, mode);

        g2.setColor(new Color(63,216, 212));
        g2.fill(new Ellipse2D.Double(   DisplayConvert.XforPrint(Xpos - 0.5*10*Zoom, panelWidth, Xoffset, Zoom),
                                        DisplayConvert.YforPrint(Ypos + 0.5*10*Zoom, panelHeight, Yoffset, Zoom), 10*Zoom, 10*Zoom));
    }
    public synchronized void paintHighlight(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset, double Zoom, int mode){}

    public static double distance(SpaceObject obj1, SpaceObject obj2)
    {
        return Math.sqrt( Math.pow(obj1.getXpos()-obj2.getXpos(), 2) + Math.pow(obj1.getYpos()-obj2.getYpos(), 2));
    }
}