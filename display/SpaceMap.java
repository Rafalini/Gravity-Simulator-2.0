package display;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

import space_objects.*;

@SuppressWarnings("serial")
public class SpaceMap extends JComponent
{   
    public static int width = 1500;
    public static int height = 1000;

    private int Xoffset=0;
    private int Yoffset=0;

    Dimension  screenDim = Toolkit.getDefaultToolkit().getScreenSize();

    Menu myMenu;

    ArrayList<SpaceObject> objectsList;

    public SpaceMap (Menu madeMenu)
    {
        this.myMenu = madeMenu;
        objectsList = new ArrayList<SpaceObject>();
        setPreferredSize(new Dimension(width,height));
        this.addMouseListener(new MapPanelListener(this));
        this.addMouseMotionListener(new MapPanelDrag());
        this.setLayout(null);
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

    }

    class MapPanelListener implements MouseListener
    {
        SpaceMap map;
        public MapPanelListener(SpaceMap map) {this.map=map;}
        public void mouseClicked(MouseEvent e) {}
        public void mousePressed(MouseEvent e)
        {
            objectsList.add(new SpaceObject(DisplayConvert.XforXoY(e.getX(), getWidth()),
                                            DisplayConvert.YforXoY(e.getY(), getHeight())));
            repaint();
        }
        public void mouseReleased(MouseEvent e)
        {
            new ObjectSettingsFrame(map, objectsList, new Point(   DisplayConvert.XforPrint(objectsList.get(objectsList.size()-1).getXpos(), getWidth()),
                                                                    DisplayConvert.YforPrint(objectsList.get(objectsList.size()-1).getYpos(), getHeight())));
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    class MapPanelDrag implements MouseMotionListener
    {
        public void mouseDragged(MouseEvent e)
        {
            //reposition
            objectsList.get(objectsList.size()-1).newXvel(DisplayConvert.XforXoY(e.getX(), getWidth()));
            objectsList.get(objectsList.size()-1).newYvel(DisplayConvert.YforXoY(e.getY(), getHeight()));

            repaint();
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
    }
}