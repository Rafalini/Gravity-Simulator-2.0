package display.buttons;

import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class StartButton extends Button
{
    public StartButton()
    {
        super("Start!");
        ArrayList<Color> myColors = new ArrayList<Color>();

        myColors.add(new Color(200,0,0));
        myColors.add(Color.WHITE);
        myColors.add(new Color(200,0,0));

        setColors(myColors);
    }
    public void performActionOnClick()
    {

    }
}