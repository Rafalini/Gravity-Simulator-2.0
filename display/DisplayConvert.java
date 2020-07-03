package display;

//To convert from XoY where (0,0) is in the middle to window XoY, where (0,0) is in left upper corner, and Y is reversed.
public class DisplayConvert {

    // public static final int XforPrint(int X, int width)  {return  X + (int)(0.5*width); }
    // public static final int YforPrint(int Y, int height) {return -Y + (int)(0.5*height);}

    // public static final double XforPrint(double X, int width)  {return  X + 0.5*width; }
    // public static final double YforPrint(double Y, int height) {return -Y + 0.5*height;}

    public static final double XforPrint(double X, int width, int xoffset, double zoom_amount)
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        return  X*Zoom + 0.5*width + xoffset;
    }
    public static final double YforPrint(double Y, int height, int yoffset, double zoom_amount)
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        return  -Y*Zoom + 0.5*height - yoffset;
    }

    public static final double XforXoY(double X, int width, int xoffset, double zoom_amount) 
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        return  (X - xoffset - 0.5*width)/Zoom;
    }
    public static final double YforXoY(double Y, int height, int yoffset, double zoom_amount)
    {
        double Zoom = Math.abs(zoom_amount);
        if(zoom_amount<0)
             Zoom = 1/Zoom;
        return (-Y - yoffset + 0.5*height)/Zoom;
    }

    public static final int XforPrint(int X, int width, int xoffset, double zoom_amount)    {return   (int)XforPrint((double) X, width, xoffset, zoom_amount);}
    public static final int YforPrint(int Y, int height, int yoffset, double zoom_amount)   { return  (int)YforPrint((double) Y, height, yoffset, zoom_amount);}

    public static final int XforXoY(int X, int width, int xoffset, double zoom_amount)      { return  (int)XforXoY((double) X, width,  xoffset, zoom_amount); }
    public static final int YforXoY(int Y, int height, int yoffset, double zoom_amount)     { return  (int)YforXoY((double) Y, height, yoffset, zoom_amount); }

    // public static final int XforXoY(int X, int width) {return  X - (int)(0.5*width);}
    // public static final int YforXoY(int Y, int height) {return -Y + (int)(0.5*height);}

    // public static final double XforXoY(double X, int width) {return  X - 0.5*width;}
    // public static final double YforXoY(double Y, int height) {return -Y + 0.5*height;}
}