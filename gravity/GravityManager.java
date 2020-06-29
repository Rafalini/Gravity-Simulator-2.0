package gravity;

import java.util.ArrayList;
import java.util.concurrent.*;

import display.*;
import space_objects.SpaceObject;
import constants.*;

//Main thread
//Splits tasks on multiple threads to calculate gravity influence, calls update() and repaint() in infinite loop.

public class GravityManager extends Thread
{
    Menu myMenu;
    SpaceMap myMap;
    //ArrayList<GravityThread> threadList;
    Semaphore newListSem;
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
//actions that impact quantity of space objects

            for(int i=0; i<objectsList.size(); i++)
                objectsList.get(i).performIteration();
//impacts
            for(int i=0; i<objectsList.size()-1; i++)
            {
                for(int j=i+1; j<objectsList.size(); j++)
                {       //distance <= R - r  --> radius on drawing is diameter, so r/2
                    if(SpaceObject.distance(objectsList.get(i), objectsList.get(j)) <=
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
//create copy of list, that copy will be split into smaller lists for threads.
            ArrayList<SpaceObject> newList = cloneList(objectsList);
            threads = myMenu.getThreadsValue();
            time = myMenu.getTimeValue();


//main multi thread engine
            if(time != 0) //if action not paused, then go:
            {
                ArrayList<ArrayList<SpaceObject>> fragments = splitList(newList, threads);
                newListSem = new Semaphore(threads,true);
//create and start threads
                for(int i=0; i<threads; i++)
                    new Thread (new GravityThread(fragments.get(i), objectsList, newListSem, time, myMenu)).start();
//wait for threads to complete their tasks, then merge all lists    
                 newListSem.acquireUninterruptibly(threads);
            }
            objectsList = myMap.updateMergeGet(newList);    //update current list for calculations and send it to paint
            myMap.repaint();
//time elapsed:            
            long EndTime = System.nanoTime();
            if( (EndTime-startTime)/1000000 < 50)   //if calculations are fast, dont calculate too oftem, to avoid increasment in time elapse
                try{TimeUnit.MILLISECONDS.sleep(50);}catch(Exception e){}; //too oftem caltulations = faster animation -> faster time elapse
        }
    }

    private ArrayList<SpaceObject> cloneList(ArrayList<SpaceObject> original)
    {
            ArrayList<SpaceObject> clone = new ArrayList<SpaceObject>();
            for (SpaceObject item : original) clone.add(item.clone());
            return clone;
    }

    public ArrayList<ArrayList<SpaceObject>> splitList(ArrayList<SpaceObject> newList, int lists)
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
}