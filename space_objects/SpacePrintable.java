package space_objects;

import java.awt.*;

//interface required to make object printable, each object can be painted differently
//paint alghoritms can be complex

public interface SpacePrintable
{
    public void paintObject   (Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset);
    public void paintHighlight(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset);
}