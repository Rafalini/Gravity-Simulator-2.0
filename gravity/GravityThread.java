package gravity;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import space_objects.*;
import display.*;
import constants.Constants;

public class GravityThread extends Thread
{
    final double G = 0.00002;
    ArrayList<SpaceObject> editable;
    ArrayList<SpaceObject> datalist;
    Semaphore editableSem;
    Menu myMenu;
    int time;

    public GravityThread(ArrayList<SpaceObject> editable,
                   final ArrayList<SpaceObject> datalist, Semaphore sem, int time, Menu myMenu)
    {
        editableSem = sem;
        editableSem.acquireUninterruptibly();
        this.editable = editable;
        this.datalist = datalist;
        this.time = time;
        this.myMenu = myMenu;
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
                    Math.pow(Double.compare(datalist.get(j).getYpos(),editable.get(i).getYpos()), 2) != 0)
                {
                    double square_range = Math.pow( datalist.get(j).getXpos() - editable.get(i).getXpos(), 2)+
                                          Math.pow( datalist.get(j).getYpos() - editable.get(i).getYpos(), 2);

                    dxVel  += G * time * datalist.get(j).getMass() * Constants.Mfactor / square_range
                            * (datalist.get(j).getXpos() - editable.get(i).getXpos()) /Math.sqrt(square_range);

                    dyVel  += G * time * datalist.get(j).getMass() * Constants.Mfactor / square_range
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