package display;

import javax.swing.*;

@SuppressWarnings("unused")
public class Printable {

    private JComponent myPanel;

    public Printable(JComponent panel)  {   myPanel = panel; }

    public int XtoPrint(int X){ return (int)(X + 0.5 * 400);}
    public int YtoPrint(int Y){ return (int)(0.5 * 1000 - Y);}

    public int XtoXoY(int X) {return (int)(X - 0.5 * 400); }
    public int YtoXoY(int Y) {return (int)(0.5 * 1000 - Y);}
}