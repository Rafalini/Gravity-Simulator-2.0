package display;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class SpaceMap extends JComponent
{   
    public static final int width = 1200;
    public static final int height = 1000;

    public SpaceMap (Menu myMenu)
    {
        setPreferredSize(new Dimension(width,height));
        //setBorder(BorderFactory.createTitledBorder("SpaceMap"));
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());
        //System.out.println("Space Map: w: " + getWidth() + " h: " + getHeight());
    }
}