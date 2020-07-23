package display.shapes;

import java.awt.*;

public class Wall {
    Dimension P1;
    Dimension P2;
    //Dimension hitP1;
    //Dimension hitP2;

    public Wall (Dimension p1, Dimension p2)
    {
        this.P1 = p1;
        this.P2 = p2;
    }

    public Wall (int x1, int y1, int x2, int y2)
    {
        this.P1 = new Dimension(x1,y1);
        this.P2 = new Dimension(x2,y2);
    }

    //public void addHit1 (int x, int y) {hitP1 = new Dimension(x,y);}
    //public void addHit2 (int x, int y) {hitP2 = new Dimension(x,y);}

    public Dimension getP1() {return P1;}
    public Dimension getP2() {return P2;}
    //public Dimension getHitP1() {return hitP1;}
    //public Dimension getHitP2() {return hitP2;}
}