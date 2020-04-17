package gravity;

import java.util.ArrayList;
import java.util.concurrent.*;

import display.*;
import space_objects.SpaceObject;

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

            ArrayList<SpaceObject> newList = cloneList(objectsList);
            threads = myMenu.getThreadsValue();
            time = myMenu.getTimeValue();

//actions that impact quantity of space objects
//impacts
            for(int i=0; i<newList.size(); i++)
                newList.get(i).performIteration();

            for(int i=0; i<newList.size()-1; i++)
            {
                for(int j=i+1; j<newList.size(); j++)
                {
                    if(SpaceObject.distance(newList.get(i), newList.get(j)) * 3 < newList.get(i).getRadius() + newList.get(j).getRadius())
                    {
                        newList.get(i).impact(newList.get(j));
                        newList.remove(j);
                        i = 0;
                        j = 0;
                    }
                }
            }

//main multi thread engine
            if(time != 0)
            {
                ArrayList<ArrayList<SpaceObject>> fragments = splitList(newList, threads);
                newListSem = new Semaphore(threads,true);

                for(int i=0; i<threads; i++)
                    new Thread (new GravityThread(fragments.get(i), objectsList, newListSem, time, myMenu)).start();
                
                 newListSem.acquireUninterruptibly(threads);
            }
            objectsList = myMap.updateMergeGet(newList);
            myMap.repaint();
            long EndTime = System.nanoTime();
            if( (EndTime-startTime)/1000000 < 50)
            {
                try{TimeUnit.MILLISECONDS.sleep(50);}catch(Exception e){};
            }
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
            {
                fragments.get(i).add(newList.get(i*listlength+j));
            }
        }

        if(newList.size() > lists*listlength)
            for(int i=lists*listlength; i<newList.size(); i++)
                fragments.get(fragments.size()+lists*listlength-i-1).add(newList.get(i));

        return fragments;
    }
}