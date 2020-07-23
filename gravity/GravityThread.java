package gravity;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.awt.Dimension;

import space_objects.*;
import constants.Constants;
import display.DisplayConvert;
import display.shapes.*;
import display.*;
import functions.*;

//Single thread that calculates next position for some given amount of objects against all.
//Becouse its square complexity problem every single object is influenced by all other. Thread
//has two lists, one complete list of all objects and other, smaller list of objects to update.

public class GravityThread extends Thread
{
    ArrayList<SpaceObject> newList;
    ArrayList<SpaceObject> editable;
    ArrayList<SpaceObject> datalist;
    ArrayList<Star> starlist;
    Semaphore listUpdate;
    Semaphore lightCalc;
    Semaphore finish;
    display.Menu myMenu;
    SpaceMap myMap;
    int time;

    public GravityThread(ArrayList<SpaceObject> newList,
                         ArrayList<SpaceObject> editable,
                   final ArrayList<SpaceObject> datalist, Semaphore listUpdate, Semaphore lightCalc, Semaphore finish, int time, display.Menu myMenu, ArrayList<Star> stars, SpaceMap myMap)
    {
        this.listUpdate = listUpdate;
        listUpdate.acquireUninterruptibly();   //lock on editable list, un locked when calculations and edition is finished
        this.lightCalc = lightCalc;
        this.finish = finish;
        finish.acquireUninterruptibly();

        this.editable = editable;       //list of objects to update
        this.datalist = datalist;       //list of all objects
        this.time = time;               //time step
        this.myMenu = myMenu;           //
        this.newList = newList;
        this.myMap = myMap;
        this.starlist = stars;
    }

    public void run()
    {
        //myMenu.printOnLog("GT "+ editable.size() + " const: " + datalist.size() + " t: " + time);
        
        updatePositionAndSpeed(editable, newList);

        listUpdate.release();  //list processing finished
        lightCalc.acquireUninterruptibly(); //when acquired newList is actual list of all objects

        //calc light
        ArrayList<Dimension>  corners = getCornerPoints();
        ArrayList<Wall> screenBorders = getborderWalls(corners);
        
        for(int i=0; i<starlist.size(); i++)
        {
            Dimension starPos = new Dimension((int)starlist.get(i).getXpos(), (int)starlist.get(i).getYpos());
            TreeMap<Double,Wall> orderedWalls = new TreeMap<Double,Wall>(); //sorted, to paint area corectly
            for(int j=0; j<newList.size(); j++)
                orderedWalls.put(Math.atan2(newList.get(j).getXpos()-starlist.get(i).getXpos(),
                                                    newList.get(j).getYpos()-starlist.get(i).getYpos() ),
                                                    newList.get(j).getRayPoints( (int)starlist.get(i).getXpos(),(int)starlist.get(i).getYpos() ));

            ArrayList<Integer> xCoords = new ArrayList<Integer>();
            ArrayList<Integer> yCoords = new ArrayList<Integer>();

            //int lastBorder = 3; //left border first, next counter clockwise, next border will be 2, then 1 and 0
            int currentBorder = 3;

            for (Wall this_wall : orderedWalls.values())
            {
                Dimension intersectPoint = rayCross(starPos, this_wall.getP1(), screenBorders.get(currentBorder));

                while(intersectPoint == null && currentBorder >= 0)
                {
                    xCoords.add((int)corners.get(currentBorder).getWidth());
                    yCoords.add((int)corners.get(currentBorder).getHeight());
                    currentBorder--;
                    intersectPoint = rayCross(starPos, this_wall.getP1(), screenBorders.get(currentBorder))
                }

                //process first point
                Dimension closerIntersectPoint = intersectPoint;
                for (Wall some_other_wall : orderedWalls.values())     //can be limited by angle (to awoid checking other side)
                {
                    intersectPoint = wallCross(starPos, this_wall.getP1(), some_other_wall);
                    if(intersectPoint != null)  //there is 'closer' planet that casts shadow
                        break;
                    intersectPoint = rayCross(starPos, this_wall.getP1(), some_other_wall);

                    if(GeomFunctions.pointsDistance(closerIntersectPoint, starPos) > GeomFunctions.pointsDistance(intersectPoint, starPos))
                        closerIntersectPoint = intersectPoint;
                }

                //add closer intersection
                //add planet corner

                intersectPoint = rayCross(starPos, this_wall.getP2(), screenBorders.get(currentBorder));

                while(intersectPoint == null && currentBorder >= 0)
                {
                    xCoords.add((int)corners.get(currentBorder).getWidth());
                    yCoords.add((int)corners.get(currentBorder).getHeight());
                    currentBorder--;
                    intersectPoint = rayCross(starPos, this_wall.getP2(), screenBorders.get(currentBorder))
                }

                //process second point

                for (Wall some_other_wall : orderedWalls.values())     //can be limited by angle (to awoid checking other side)
                {
                   
                }

                 //add planet corner
                //add closer intersection
            }

            if(currentBorder != 0)  //fill empty 'sides'
                for(int j=0; j<currentBorder+1; j++)
                {
                    xCoords.add((int)corners.get(currentBorder-j).getWidth());
                    yCoords.add((int)corners.get(currentBorder-j).getHeight());
                }
            
            starlist.get(i).setLightShape(xCoords, yCoords, myMenu, myMap);
        }

        finish.release();
    }

    private void updatePositionAndSpeed(ArrayList<SpaceObject> editable, ArrayList<SpaceObject> datalist)
    {
        for(int i=0; i<editable.size(); i++)
        {
            double dxVel = 0;
            double dyVel = 0;
            for(int j=0; j<datalist.size(); j++)
                //if( Math.pow(Double.compare(datalist.get(j).getXpos(),editable.get(i).getXpos()), 2) +
                //   Math.pow(Double.compare(datalist.get(j).getYpos(),editable.get(i).getYpos()), 2) != 0)      //if its not the same obj, if distance != 0 go calculate
                if(editable.get(i).getID() != datalist.get(j).getID())
                {
                    double square_range = Math.pow( datalist.get(j).getXpos() - editable.get(i).getXpos(), 2)+
                                          Math.pow( datalist.get(j).getYpos() - editable.get(i).getYpos(), 2);

                    dxVel  += Constants.G_graviti_const * time * datalist.get(j).getMass() * Constants.Mfactor / square_range
                            * (datalist.get(j).getXpos() - editable.get(i).getXpos()) /Math.sqrt(square_range);

                    dyVel  += Constants.G_graviti_const * time * datalist.get(j).getMass() * Constants.Mfactor / square_range
                            * (datalist.get(j).getYpos() - editable.get(i).getYpos()) /Math.sqrt(square_range);
                    //myMenu.printOnLog("range:  "+square_range+ " it: " +j);
                }
            //myMenu.printOnLog("dX:  "+dxVel+ " dY: " +dyVel);
            //editable.
            editable.get(i).updateXvel(dxVel);
            editable.get(i).updateYvel(dyVel);
            editable.get(i).updatePos(time*Constants.StepFactor);
        }
    }

    private ArrayList<Dimension> getCornerPoints()
    {
        ArrayList<Dimension> corners = new ArrayList<Dimension>();
        corners.add(new Dimension(DisplayConvert.XforXoY( 0,                     myMap.getWidth(), myMap.getXoffset(), myMap.getZoom()),   DisplayConvert.YforXoY( 0,                      myMap.getHeight(), myMap.getXoffset(), myMap.getZoom())));
        corners.add(new Dimension(DisplayConvert.XforXoY( (int)myMap.getWidth(), myMap.getWidth(), myMap.getXoffset(), myMap.getZoom()),   DisplayConvert.YforXoY( 0,                      myMap.getHeight(), myMap.getXoffset(), myMap.getZoom())));
        corners.add(new Dimension(DisplayConvert.XforXoY( (int)myMap.getWidth(), myMap.getWidth(), myMap.getXoffset(), myMap.getZoom()),   DisplayConvert.YforXoY( (int)myMap.getHeight(), myMap.getHeight(), myMap.getXoffset(), myMap.getZoom())));
        corners.add(new Dimension(DisplayConvert.XforXoY( 0,                     myMap.getWidth(), myMap.getXoffset(), myMap.getZoom()),   DisplayConvert.YforXoY( (int)myMap.getHeight(), myMap.getHeight(), myMap.getXoffset(), myMap.getZoom())));

        return corners;
    }

    private ArrayList<Wall> getborderWalls(ArrayList<Dimension> corners) //window corners to XOY
    {
        ArrayList<Wall> borders = new ArrayList<Wall>();
        borders.add(new Wall(  corners.get(0), corners.get(1)));  //(0,0) -> (x,0)
        borders.add(new Wall(  corners.get(1), corners.get(2))); //(x,0) -> (x,y)
        borders.add(new Wall(  corners.get(2), corners.get(3))); //(x,y) -> (0,y)
        borders.add(new Wall(  corners.get(3), corners.get(0))); //(0,y) -> (0,0)

        return borders;
    }

    private Dimension rayCross(Wall w1, Wall w2) //w1 is ray -> its infinite
    {
        double x1 = w1.getP1().getWidth(), y1 = w1.getP1().getHeight();
        double x2 = w1.getP2().getWidth(), y2 = w1.getP2().getHeight();
        double x3 = w2.getP1().getWidth(), y3 = w2.getP1().getHeight();
        double x4 = w2.getP2().getWidth(), y4 = w2.getP2().getHeight();

        double den = (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4); //can be equal 0 if P1 = P2 or P3=P4
        if(den==0)
            return null;

        double t = ((x1 -x3)*(y3 - y4) - (y1 - y3)*(x3 - x4))/den;
        double u = -((x1 - x2)*(y1 - y3) - (y1 - y2)*(x1 - x3))/den;
        if(t >=0 && 0 <= u && u<= 1) //infinite ray
            return new Dimension((int)(x1+t*(x2-x1)), (int)(y1+t*(y2-y1)));
        else
            return null;
    }

    private Dimension rayCross(Dimension d1, Dimension d2, Wall w2) { return rayCross(new Wall(d1,d2), w2);} //dimensions are ray

    private Dimension wallCross(Wall w1, Wall w2) //w1 is ray -> its infinite
    {
        double x1 = w1.getP1().getWidth(), y1 = w1.getP1().getHeight();
        double x2 = w1.getP2().getWidth(), y2 = w1.getP2().getHeight();
        double x3 = w2.getP1().getWidth(), y3 = w2.getP1().getHeight();
        double x4 = w2.getP2().getWidth(), y4 = w2.getP2().getHeight();

        double den = (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4);
        if(den==0)
            return null;

        double t = ((x1 -x3)*(y3 - y4) - (y1 - y3)*(x3 - x4))/den;
        double u = -((x1 - x2)*(y1 - y3) - (y1 - y2)*(x1 - x3))/den;
        if(0 <=t && t <= 1 && 0 <= u && u<= 1)
            return new Dimension((int)(x1+t*(x2-x1)), (int)(y1+t*(y2-y1)));
        else
            return null;
    }

    private Dimension wallCross(Dimension d1, Dimension d2, Wall w2) { return wallCross(new Wall(d1,d2), w2);}
}