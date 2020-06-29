package gravity;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import space_objects.*;
import display.*;
import constants.Constants;

//Single thread that calculates next position for some given amount of objects against all.
//Becouse its square complexity problem every single object is influenced by all other. Thread
//has two lists, one complete list of all objects and other, smaller list of objects to update.

public class GravityThread extends Thread
{
    //final double G = 0.00002;
    ArrayList<SpaceObject> editable;
    ArrayList<SpaceObject> datalist;
    Semaphore editableSem;
    Menu myMenu;
    int time;

    public GravityThread(ArrayList<SpaceObject> editable,
                   final ArrayList<SpaceObject> datalist, Semaphore sem, int time, Menu myMenu)
    {
        editableSem = sem;
        editableSem.acquireUninterruptibly();   //lock on editable list, un locked when calculations and edition is finished
        this.editable = editable;       //list of objects to update
        this.datalist = datalist;       //list of all objects
        this.time = time;               //time step
        this.myMenu = myMenu;           //
    }

    public void run()
    {
        //myMenu.printOnLog("GT "+ editable.size() + " const: " + datalist.size() + " t: " + time);
        for(int i=0; i<editable.size(); i++)
        {
            double dxVel = 0;
            double dyVel = 0;
            for(int j=0; j<datalist.size(); j++)
                if( Math.pow(Double.compare(datalist.get(j).getXpos(),editable.get(i).getXpos()), 2) +
                    Math.pow(Double.compare(datalist.get(j).getYpos(),editable.get(i).getYpos()), 2) != 0)      //if its not the same obj, if distance != 0 go calculate
                {
                    double square_range = Math.pow( datalist.get(j).getXpos() - editable.get(i).getXpos(), 2)+
                                          Math.pow( datalist.get(j).getYpos() - editable.get(i).getYpos(), 2);

                    dxVel  += Constants.G_graviti_const * time * datalist.get(j).getMass() * Constants.Mfactor / square_range
                            * (datalist.get(j).getXpos() - editable.get(i).getXpos()) /Math.sqrt(square_range);

                    dyVel  += Constants.G_graviti_const * time * datalist.get(j).getMass() * Constants.Mfactor / square_range
                            * (datalist.get(j).getYpos() - editable.get(i).getYpos()) /Math.sqrt(square_range);
                }
            //editable.
            editable.get(i).updateXvel(dxVel);
            editable.get(i).updateYvel(dyVel);
            editable.get(i).updatePos(time);
        }

        editableSem.release();
    }
}