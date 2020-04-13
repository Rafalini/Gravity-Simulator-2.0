package display.buttons;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class Button extends JPanel
{
    String content;
    int Xpos, Ypos;
    int Xsize, Ysize;
    ArrayList<Color> rectangleColor;
    ArrayList<RoundRectangle2D> rectangles;
    Font font;
//    0---------------0==
//    |  (Xpos,Ypos)  |  Ysize
//    0---------------0==
//    ||              ||
//          Xsize

    public abstract void performActionOnClick();

    public Button(String text)
    {
        super();
        this.setSize(new Dimension(100,50));
        rectangleColor = new ArrayList<Color>();
        rectangles     = new ArrayList<RoundRectangle2D>();

        content = text;

        font = new Font("Serif", Font.PLAIN, 30);

        this.addMouseMotionListener(new MapPanelMoveListener());
        this.addMouseListener(new MapPanelClickListener());
    }

    /*public boolean isPointed(int Xclick, int Yclick)
    {
        if(Xpos - 0.5 * Xsize < Xclick && Xclick < Xpos + 0.5 * Xsize)
            if(Ypos - 0.5 * Ysize < Yclick && Yclick < Ypos + 0.5 * Ysize)
                return true;
        return false;
    }*/

    class MapPanelMoveListener implements MouseMotionListener  
    {
        public void mouseDragged(MouseEvent e)  {}
        public void mouseEntered(MouseEvent e)  {}     //highlight
        public void mouseExited(MouseEvent e)   {}    //down highlight
        public void mouseMoved(MouseEvent e)    {}
    }

    class MapPanelClickListener implements MouseListener
    {
        public void mouseClicked(MouseEvent e)  {performActionOnClick();}
        public void mousePressed(MouseEvent e)  {}      //down highlight
        public void mouseReleased(MouseEvent e) {}     //highlight
        public void mouseEntered(MouseEvent e)  {}
        public void mouseExited(MouseEvent e)   {}
    }

    public void setColors(ArrayList<Color> colorlist)
    {
        for(int i=0; i<colorlist.size(); i++)
        {
            System.out.println("settuje color "+getWidth()+" "+getHeight());
            rectangleColor.add(colorlist.get(i));
            rectangles.add(new RoundRectangle2D.Float());
            rectangles.get(i).setRoundRect(i*10, i*10, getWidth()-i*10, getHeight()-i*10, 0.1*getWidth(), 0.1*getHeight());
        }
    }

    public int XtoPrint(int X){ return (int)(X + 0.5 * getWidth());}
    public int YtoPrint(int Y){ return (int)(0.5 * getWidth() - Y);}

    public int XtoXoY(int X) {return (int)(X - 0.5 * getWidth()); }
    public int YtoXoY(int Y) {return (int)(0.5 * getHeight() - Y);}

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for(int i=0; i<rectangles.size(); i++)
        {
            g2.setColor(rectangleColor.get(i));
            g2.fill(rectangles.get(i));
        }

        g2.setColor(Color.BLACK);
        g2.setFont(font);
        g2.drawString(content, 5, 5);
    }
}