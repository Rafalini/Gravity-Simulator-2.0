package display;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.Semaphore;

import gravity.GravityManager;

import java.util.ArrayList;

import space_objects.*;
//import gravity.*;

@SuppressWarnings("serial")
public class SpaceMap extends JComponent
{   
    public static int width = 1500;
    public static int height = 1000;

    private int Xoffset=0;
    private int Yoffset=0;

    Dimension  screenDim = Toolkit.getDefaultToolkit().getScreenSize();

    Menu myMenu;

    volatile Boolean doReset=false;
    Semaphore queueSemaphore = new Semaphore(1);
    ArrayList<SpaceObject> objectsList;  //for ready, printable, read-onlny objects (no semaphore on it)
    ArrayList<SpaceObject> objectsQueue; //for objects in creation, to avoid printing unready object

    public SpaceMap (Menu madeMenu)
    {
        this.myMenu = madeMenu;

        objectsList  = new ArrayList<SpaceObject>();    //editable only by GravityManager thread
        objectsQueue = new ArrayList<SpaceObject>();    //editable only by swing thread

        myMenu.sendGravityManager(new GravityManager(myMenu, this, objectsList));

        setPreferredSize(new Dimension(width,height));
        this.addMouseListener(new MapPanelListener(this));
        this.addMouseMotionListener(new MapPanelDrag());
        this.setLayout(null);
        ResetButtonListener ls = new ResetButtonListener();
        myMenu.addResetListener(ls);
    }

    public synchronized ArrayList<SpaceObject> updateMergeGet (ArrayList<SpaceObject> newList) //method to join lists editable by different threads
    {
        if(doReset)
        {
            doReset = false;
            objectsList.removeAll(objectsList);
            objectsQueue.removeAll(objectsQueue);
            repaint();
        }
        else
        {
            objectsList = newList;

            if(queueSemaphore.tryAcquire())
            {
                for(int i=0; i<objectsQueue.size(); i++)
                    objectsList.add(objectsQueue.get(i));

                objectsQueue.removeAll(objectsQueue);
                queueSemaphore.release();
                repaint();  
            }
        }
        return objectsList;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,getHeight()+1000,getWidth()+1000);
        for(int i=0; i<objectsList.size(); i++)
            objectsList.get(i).paintObject(g2, width, height, Xoffset, Yoffset);

        for(int i=0; i<objectsQueue.size(); i++)
            objectsQueue.get(i).paintObject(g2, width, height, Xoffset, Yoffset);

    }

    class MapPanelListener implements MouseListener                     //to be synced
    {
        SpaceMap map;
        public MapPanelListener(SpaceMap map) {this.map=map;}
        public void mouseClicked(MouseEvent e) {}
        public void mousePressed(MouseEvent e)
        {
            queueSemaphore.acquireUninterruptibly();
            
            objectsQueue.add(new SpaceObject(DisplayConvert.XforXoY(e.getX(), getWidth()),
                                             DisplayConvert.YforXoY(e.getY(), getHeight())));
            repaint();
        }
        public void mouseReleased(MouseEvent e)
        {
            switch(String.valueOf(myMenu.ObjType.getSelectedItem()))
            {   
                case "Planet":
                            objectsQueue.set(objectsQueue.size()-1, new Planet( objectsQueue.get(objectsQueue.size()-1), 100));
                            map.repaint();
                break;
                case "Star":
                            map.repaint();
                break;
                case "Comet":
                            map.repaint();
                break;
            }
            queueSemaphore.release();
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    class MapPanelDrag implements MouseMotionListener                     //to be synced
    {
        public void mouseDragged(MouseEvent e)
        {            
            //queueSemaphore is already 'open'
            objectsQueue.get(objectsQueue.size()-1).newXvel(DisplayConvert.XforXoY(e.getX(), getWidth()));
            objectsQueue.get(objectsQueue.size()-1).newYvel(DisplayConvert.YforXoY(e.getY(), getHeight()));

            repaint();
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
    }

    class ResetButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
           doReset = true;
        }
    }
}