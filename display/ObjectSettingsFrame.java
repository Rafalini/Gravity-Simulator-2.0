package display;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import space_objects.*;

@SuppressWarnings("serial")
public class ObjectSettingsFrame extends JFrame
{
    JButton Zastosuj;
    JComboBox<String> ObjType;
    ArrayList<SpaceObject> objects;
    SpaceMap myMap;

    public ObjectSettingsFrame (SpaceMap map, ArrayList<SpaceObject> objects, Point windowCenter)
    {
        this.setSize(new Dimension(500,500));
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocation(windowCenter);
        this.setVisible(true);

        this.objects = objects;
        this.myMap = map;

        JPanel GlobalPanel = new JPanel();
        GlobalPanel.setLayout(new BoxLayout(GlobalPanel, BoxLayout.Y_AXIS));
        Zastosuj = new JButton("Zastosuj");

        Zastosuj.addActionListener(new ButtonListener(this));

        String [] setupoptions = {"Planet", "Star", "Comet"};
        ObjType = new JComboBox<String>(setupoptions);

        GlobalPanel.add(ObjType);
        GlobalPanel.add(Zastosuj);
        this.add(GlobalPanel);
    }

    private class ButtonListener implements ActionListener
    {
        ObjectSettingsFrame frame;
        public ButtonListener(ObjectSettingsFrame fr) {frame = fr;}
        public void actionPerformed(ActionEvent e)
        {
            //objects.set(objects.size()-1, new Planet( objects.get(objects.size()-1)) );
            myMap.repaint();
            frame.dispose();
            //this.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}