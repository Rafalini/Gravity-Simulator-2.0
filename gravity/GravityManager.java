package gravity;

import java.util.ArrayList;
import java.util.concurrent.*;

import display.*;
import space_objects.*;
import constants.*;

//Main thread
//Splits tasks on multiple threads to calculate gravity influence, calls update() and repaint() in infinite loop.

public class GravityManager extends Thread
{
    Menu myMenu;
    SpaceMap myMap;
    //ArrayList<GravityThread> threadList;
    Semaphore listReadySem;
    Semaphore lightReadySem;
    Semaphore calcReadySem;
    ArrayList<SpaceObject> objectsList;
    int threads;
    int time;

    public GravityManager(Menu myMenu, SpaceMap myMap, ArrayList<SpaceObject> objectsList)
    {
        this.myMenu = myMenu;
        this.myMap = myMap;
        this.objectsList = objectsList;
        //threadList = new ArrayList<GravityThread>();
    }

    public void run()
    {
        for(;;)
        {
            long startTime = System.nanoTime();      
//create copy of list, that copy will be split into smaller lists for threads.
            objectsList = impacts(objectsList);

            ArrayList<SpaceObject> newList = cloneList(objectsList);
            ArrayList<Star> stars = getStarsAndIterate(newList);
            threads = myMenu.getThreadsValue();
            time = myMenu.getTimeValue();

//main multi thread engine
            if(time >= 0) //if action not paused, then go:
            {
                ArrayList<ArrayList<Star>> starsFragments = splitStarList(stars,threads);
                ArrayList<ArrayList<SpaceObject>> fragments = splitObjectList(newList, threads);
                listReadySem = new Semaphore(threads,true);
                lightReadySem = new Semaphore(threads,false);
                calcReadySem = new Semaphore(threads,false);
                lightReadySem.acquireUninterruptibly(threads);  //wait with light calculation untill there is update in postiion
//create and start threads
                for(int i=0; i<threads; i++)
                    new Thread (new GravityThread(newList, fragments.get(i), objectsList, listReadySem, lightReadySem, calcReadySem, time, myMenu, starsFragments.get(i), myMap)).start();
//wait for threads to complete their tasks, then merge all lists    
                
                listReadySem.acquireUninterruptibly(threads); //wait for list update
                lightReadySem.release(threads);               //release light processing
                calcReadySem.acquireUninterruptibly(threads); //wait for end of calculation
            }
            objectsList = myMap.updateMergeGet(newList, stars);    //update current list for calculations and send it to paint
            myMap.repaint();
//time elapsed:            
            long EndTime = System.nanoTime();
            if( (EndTime-startTime)/1000000 < 50)   //if calculations are fast, dont calculate too oftem, to avoid increasment in time elapse
                try{TimeUnit.MILLISECONDS.sleep(500);}catch(Exception e){}; //too oftem caltulations = faster animation -> faster time elapse
        }
    }

    private ArrayList<SpaceObject> cloneList(ArrayList<SpaceObject> original)
    {
            ArrayList<SpaceObject> clone = new ArrayList<SpaceObject>();
            for (SpaceObject item : original) clone.add(item.clone());
            return clone;
    }

    private ArrayList<ArrayList<SpaceObject>> splitObjectList(ArrayList<SpaceObject> newList, int lists)
    {
        ArrayList<ArrayList<SpaceObject>> fragments = new ArrayList<ArrayList<SpaceObject>>();
        int listlength = newList.size()/lists;

        for(int i=0; i<lists; i++)
        {
            fragments.add(new ArrayList<SpaceObject>());
            for(int j=0; j<listlength; j++)
                fragments.get(i).add(newList.get(i*listlength+j));
        }

        if(newList.size() > lists*listlength)
            for(int i=lists*listlength; i<newList.size(); i++)
                fragments.get(fragments.size()+lists*listlength-i-1).add(newList.get(i));

        return fragments;
    }

    private ArrayList<ArrayList<Star>> splitStarList(ArrayList<Star> newList, int lists)
    {
        ArrayList<ArrayList<Star>> fragments = new ArrayList<ArrayList<Star>>();
        int listlength = newList.size()/lists;

        for(int i=0; i<lists; i++)
        {
            fragments.add(new ArrayList<Star>());
            for(int j=0; j<listlength; j++)
                fragments.get(i).add(newList.get(i*listlength+j));
        }

        if(newList.size() > lists*listlength)
            for(int i=lists*listlength; i<newList.size(); i++)
                fragments.get(fragments.size()+lists*listlength-i-1).add(newList.get(i));

        return fragments;
    }

    private ArrayList<SpaceObject> impacts(ArrayList<SpaceObject> objectsList)
    {
        for(int i=0; i<objectsList.size()-1; i++)
            {
                for(int j=i+1; j<objectsList.size(); j++)
                {       //distance <= R - r  --> radius on drawing is diameter, so r/2
                    if(SpaceObject.objectDistance(objectsList.get(i), objectsList.get(j)) <=
                        Constants.RadiusOnImpact*Math.max(objectsList.get(i).getRadius()/2, objectsList.get(j).getRadius()/2))
                    {   //get heavier planet
                        if(objectsList.get(i).getMass() >= objectsList.get(j).getMass() )
                        {
                            objectsList.get(i).impact(objectsList.get(j));
                            objectsList.remove(j);
                        }
                        else
                        {
                            objectsList.get(j).impact(objectsList.get(i));
                            objectsList.remove(i);
                        }
                        i = 0;  //for unordered object list one colision may effect in other colisions, so it has to be checked again
                        j = 0;
                    }
                }
            }
        return objectsList;
    }

    private ArrayList<Star> getStarsAndIterate(ArrayList<SpaceObject> objectsList)
    {
        ArrayList<Star> stars = new ArrayList<Star>();
        for(int i=0; i<objectsList.size(); i++) 
        {
            objectsList.get(i).performIteration();
            if(objectsList.get(i) instanceof Star)
                stars.add((Star)objectsList.get(i));
        }
        return stars;
    }
}