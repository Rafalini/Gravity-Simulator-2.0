package gravity;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import space_objects.*;
import display.*;

public class GravityThread extends Thread
{
    ArrayList<SpaceObject> editable;
    ArrayList<SpaceObject> datalist;
    Semaphore editableSem;
    Menu myMenu;
    int time;

    public GravityThread(ArrayList<SpaceObject> editable,
                   final ArrayList<SpaceObject> datalist, Semaphore sem, int time, Menu myMenu)
    {
        this.editable = editable;
        this.datalist = datalist;
        editableSem = sem;
        this.time = time;
        this.myMenu = myMenu;
    }

    public void run()
    {
        myMenu.printOnLog("GT "+ editable.size() + " const: " + datalist.size() + " t: " + time);
        /*for(int i=0; i<editable.size(); i++)
        {
            editable.get(i).updatePos();
        }*/
        //impacts
        //next step
        //next vel
        //editableSem.release();
    }
}