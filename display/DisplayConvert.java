package display;

//To convert from XoY where (0,0) is in the middle to window XoY, where (0,0) is in left upper corner, and Y is reversed.
public class DisplayConvert {

    public static final int XforPrint(int X, int width)  {return  X + (int)(0.5*width); }
    public static final int YforPrint(int Y, int height) {return -Y + (int)(0.5*height);}

    public static final double XforPrint(double X, int width)  {return  X + 0.5*width; }
    public static final double YforPrint(double Y, int height) {return -Y + 0.5*height;}

    public static final int XforXoY(int X, int width) {return  X - (int)(0.5*width);}
    public static final int YforXoY(int Y, int height) {return -Y + (int)(0.5*height);}

    public static final double XforXoY(double X, int width) {return  X - 0.5*width;}
    public static final double YforXoY(double Y, int height) {return -Y + 0.5*height;}
}