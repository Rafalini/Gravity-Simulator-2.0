package display;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.Semaphore;

import gravity.GravityManager;

import java.util.ArrayList;

import space_objects.*;

//Main map to draw planets, objects etc, with synchronization on update method.
//Main object list is provided to paint, if user has created something in meanwhile
//new objects will be added, if user is still crating this objects wont be added. After
//update ThreadManager calls paint() method to referesh view.

//When user creates new object there is a lock that is unlocked only when user finishes creating.
//Only then created objects can be added to complete object list and influence other objects.


@SuppressWarnings("serial")
public class SpaceMap extends JComponent
{   
    public static int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-400;
    public static int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100;

    private int Xoffset=0; //for map movement
    private int Yoffset=0;

    private int oldXoffset=0; //for map movement
    private int oldYoffset=0;

    private int XoffsetBegin=0; //for map movement
    private int YoffsetBegin=0;

    private double zoom_amount=1;

    Dimension  screenDim = Toolkit.getDefaultToolkit().getScreenSize();

    Menu myMenu;
    UpperPanel upperPanel;

    volatile Boolean doReset=false;
    Semaphore queueSemaphore = new Semaphore(1);
    ArrayList<SpaceObject> objectsList;  //for ready, printable, read-onlny objects (no semaphore on it)
    ArrayList<SpaceObject> objectsQueue; //for objects in creation, to avoid printing unready object

    public SpaceMap (final Menu madeMenu, final UpperPanel upperPanel)
    {
        this.myMenu = madeMenu;
        this.upperPanel = upperPanel;

        objectsList  = new ArrayList<SpaceObject>();    //editable only by GravityManager thread
        objectsQueue = new ArrayList<SpaceObject>();    //editable only by swing thread

        myMenu.sendGravityManager(new GravityManager(myMenu, this, objectsList));

        setPreferredSize(new Dimension(width,height));
        this.addMouseListener(new MapPanelListener());
        this.addMouseMotionListener(new MapPanelDrag());
        this.addMouseWheelListener(new MapZoom());
        this.setLayout(null);
        final ResetButtonListener ls = new ResetButtonListener();
        myMenu.addResetListener(ls);
    }

    public ArrayList<SpaceObject> updateMergeGet (final ArrayList<SpaceObject> newList) //method to join lists editable by different threads
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

    public void paint(final Graphics g)
    {
        super.paintComponent(g);
        int mode=0;
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
            Zoom = 1/Zoom;
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        
//Background
        if(upperPanel.isDesignMode())
        {
            g2.setColor(Color.WHITE);
            mode = 1;
            g2.fillRect(0,0, getWidth(), getHeight());
            drawDesignView(g2);
        }
        else
        {
            g2.setColor(Color.BLACK);
            mode = 0;
            g2.fillRect(0,0, getWidth(), getHeight());
        }


        for(int i=0; i<objectsList.size(); i++)
            objectsList.get(i).paintObject(g2, getWidth(), getHeight(), Xoffset, Yoffset, Zoom, mode);

        for(int i=0; i<objectsQueue.size(); i++)
            objectsQueue.get(i).paintObject(g2, getWidth(), getHeight(), Xoffset, Yoffset, Zoom, mode);

        // if(objectsList.size() > 0 && myMenu.getTimeValue() != 0)    
        // {
        //     myMenu.printOnLog("last Vel: "+(int)objectsList.get(objectsList.size()-1).getXvel()
        //                               +" "+(int)objectsList.get(objectsList.size()-1).getYvel());
        // }
    }

    class MapPanelListener implements MouseListener  
    {
        public void mouseClicked(final MouseEvent e) {}
        public void mousePressed(final MouseEvent e)
        {
            myMenu.printOnLog("Hand: "+upperPanel.isHandModeActive());
            if(upperPanel.isHandModeActive())
            {
                XoffsetBegin = e.getX();
                YoffsetBegin = e.getY();
                oldXoffset = Xoffset;
                oldYoffset = Yoffset;
            }
            else
            {
                double Zoom = Math.abs(zoom_amount);
                if(zoom_amount<0)
                    Zoom = 1/Zoom;

                queueSemaphore.acquireUninterruptibly();
                
                objectsQueue.add(new SpaceObject( (int)DisplayConvert.XforXoY(e.getX(), getWidth(), Xoffset, Zoom),
                                                  (int)DisplayConvert.YforXoY(e.getY(), getHeight(), Yoffset, Zoom), myMenu.getMassValue(), (int)Math.sqrt(myMenu.getMassValue())));
                repaint();
            }
        }
        public void mouseReleased(final MouseEvent e) 
        {
            if(upperPanel.isHandModeActive())
            {
                Xoffset = e.getX() - XoffsetBegin + oldXoffset;
                Yoffset = YoffsetBegin - e.getY() + oldYoffset;
            }
            else
            {
                switch(myMenu.choosenObjectType())
                {
                    case "Planet":
                        objectsQueue.add(   new Planet  (   objectsQueue.get(objectsQueue.size()-1)    ));
                    break;
                    case "Star":
                        objectsQueue.add(   new Star    (   objectsQueue.get(objectsQueue.size()-1)    ));
                    break;
                    case "Comet":
                        //objectsQueue.add(   new Comet  (   objectsQueue.get(objectsQueue.size()-1)    )));
                    break;
                }
                objectsQueue.remove(objectsQueue.size()-2);
                queueSemaphore.release();
            }
        }
        public void mouseEntered(final MouseEvent e) {}
        public void mouseExited(final MouseEvent e) {}
    }

    class MapPanelDrag implements MouseMotionListener 
    {
        public void mouseDragged(final MouseEvent e)
        {    
            if(upperPanel.isHandModeActive())
            {
                Xoffset = e.getX() - XoffsetBegin + oldXoffset;
                Yoffset = YoffsetBegin - e.getY() + oldYoffset;
            }
            else
            {        
                double Zoom = Math.abs(zoom_amount);
                if(zoom_amount<0)
                    Zoom = 1/Zoom;
                //queueSemaphore is already 'open'
                objectsQueue.get(objectsQueue.size()-1).newXvel(DisplayConvert.XforXoY(e.getX(), getWidth(), Xoffset, Zoom));
                objectsQueue.get(objectsQueue.size()-1).newYvel(DisplayConvert.YforXoY(e.getY(), getHeight(), Yoffset, Zoom));

                repaint();
            }
        }
        public void mouseEntered(final MouseEvent e) {}
        public void mouseExited(final MouseEvent e) {}
        public void mouseMoved(final MouseEvent e) {}
    }

    class MapZoom implements MouseWheelListener
    {
        public void mouseWheelMoved(final MouseWheelEvent e)
        {
            zoom_amount -= 0.1*e.getPreciseWheelRotation();
            if(zoom_amount>0 && zoom_amount<1)
                zoom_amount=-1;
            else if(zoom_amount>-1 && zoom_amount<0)
                zoom_amount=1;
                myMenu.printOnLog("Zoom: "+zoom_amount);
        }
    }

    class ResetButtonListener implements ActionListener
    {
        public void actionPerformed(final ActionEvent e)
        {
           doReset = true;
        }
    }

    private void drawDesignView(final Graphics2D g2)
    {
        g2.setColor(Color.BLACK);
        g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
        for(int i=0; i<1; i++)
        {}
        for(int i=0; i<1; i++)
        {}
    }
}