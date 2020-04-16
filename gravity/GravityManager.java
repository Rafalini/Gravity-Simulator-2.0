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

            ArrayList<ArrayList<SpaceObject>> fragments = splitList(newList, threads);
            newListSem = new Semaphore(threads,true);

        if(time != 0){
            myMenu.printOnLog("GM th: "+threads);

            for(int i=0; i<threads; i++)
            {
                new Thread (new GravityThread(fragments.get(i), objectsList, newListSem, time, myMenu)).start();
            }
        }
            newListSem.acquireUninterruptibly(threads);
            objectsList = myMap.updateMergeGet(newList);
            myMap.repaint();

            long EndTime = System.nanoTime();

            if( (EndTime-startTime)/1000000 < 500)
            {
                try{TimeUnit.MILLISECONDS.sleep(1000);}catch(Exception e){};
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