package space_objects;

public class SpaceObject {
    private int Xpos, Ypos;
    private int Xvel, Yvel;

    public SpaceObject (int x, int y, int vx, int vy)
    {
        Xpos = x;  Ypos = y;
        Xvel = vx; Yvel = vy;
    }

    public SpaceObject ()
    { Xpos = 0; Ypos = 0; Xvel = 0; Yvel = 0;}

    public int getXpos () { return Xpos;}
    public int getYpos () { return Ypos;}

    public int getXvel () { return Xvel;}
    public int getYvel () { return Yvel;}

}