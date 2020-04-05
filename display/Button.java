package display;
//unused//unused//unused//unused//unused//unused//unused//unused//unused
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.awt.*;

public abstract class Button extends Printable{
    String content;
    int Xpos, Ypos;
    int Xsize, Ysize;
    Color myColor;
    Rectangle2D myRec;
    Font font;
    JComponent panel;

//    0---------------0==
//    |  (Xpos,Ypos)  |  Ysize
//    0---------------0==
//    ||              ||
//          Xsize

    public abstract void performActionOnClick();

    public Button(int x, int y, String text, JComponent panel)
    {
        super(panel);
        this.panel = panel;
        myColor = new Color(150,150,150);
        Xpos = x; Ypos = y;
        content = text;

        int size = 36;
        font = new Font("Serif", Font.PLAIN, size);

        Ysize = size;
        Xsize = (int)(size*5);
        
        myRec = new Rectangle2D.Double( XtoPrint((int)(Xpos-0.5*Xsize)), YtoPrint((int)(Ypos+0.5*Ysize)), Xsize, Ysize);
        //panel.addMouseMotionListener(new MapPanelMoveListener());
        //panel.addMouseListener(new MapPanelClickListener());
    }

    public boolean isPointed(int Xclick, int Yclick)
    {
        if(Xpos - 0.5 * Xsize < Xclick && Xclick < Xpos + 0.5 * Xsize)
            if(Ypos - 0.5 * Ysize < Yclick && Yclick < Ypos + 0.5 * Ysize)
                return true;
        return false;
    }

    class MapPanelMoveListener implements MouseMotionListener  
    {
        public void mouseDragged(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e)
        {
            if(isPointed(XtoXoY(e.getX()), YtoXoY(e.getY())))
                 myColor = new Color(200,150,150);
            else
                 myColor = new Color(150,150,150);
            panel.repaint();
        }
    }

    class MapPanelClickListener implements MouseListener
    {
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("clickX: "+e.getX()+"  clickY: "+e.getY()+ "  XOY:: clickX: "+XtoXoY(e.getX())+"  clickY: "+YtoXoY(e.getY()));
            if(isPointed(XtoXoY(e.getX()), YtoXoY(e.getY())))
                performActionOnClick();
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    void printThisButton(Graphics2D g2)
    {
        g2.setColor(myColor);
        g2.fill(myRec);
    }
}