package space_objects;

import java.awt.*;

public interface SpacePrintable
{
    //each obj defines its painting on its own
    public void paintObject   (Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset);
    public void paintHighlight(Graphics2D g2, int panelWidth, int panelHeight, int Xoffset, int Yoffset);
}