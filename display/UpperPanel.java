package display;

import javax.swing.*;
import java.io.*;
import java.awt.Image;
import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class UpperPanel extends JPanel {

    JCheckBox DesignView;
    JRadioButton HandTool, SelectTool;
    ButtonGroup MouseTools;

    Icon SelectIcon, HandIcon, SelectIconReleased, HandIconReleased; 

    Action doSwitchHand, doSwitchSelect;

    public UpperPanel() {
        
        DesignView = new JCheckBox("Design mode");
        DesignView.setSelected(false);
        add(DesignView);
        DesignView.setFocusable(false);

        /*
         * Icon SelectIcon = new ImageIcon("/graphics/SelectTool.JPG"); SelectTool = new
         * JRadioButton(SelectIcon); Icon HandIcon = new
         * ImageIcon("/graphics/HandTool.JPG"); HandTool = new JRadioButton(HandIcon);
         */

        Image SelectImage=null, SelectImageReleased=null;
        Image HandImage=null, HandImageReleased=null;

        try {
            SelectImage = ImageIO.read(getClass().getResource("/graphics/SelectTool.JPG"));
            HandImage = ImageIO.read(getClass().getResource("/graphics/HandTool.JPG"));
            SelectImageReleased = ImageIO.read(getClass().getResource("/graphics/SelectToolre.JPG"));
            HandImageReleased = ImageIO.read(getClass().getResource("/graphics/HandToolre.JPG"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SelectIcon = new ImageIcon(SelectImage);
        HandIcon = new ImageIcon(HandImage);
        SelectIconReleased = new ImageIcon(SelectImageReleased);
        HandIconReleased = new ImageIcon(HandImageReleased);

        SelectTool = new JRadioButton(SelectIcon);
        HandTool = new JRadioButton(HandIconReleased);

        SelectTool.addActionListener(new MouseToolsListener());
        HandTool.addActionListener(new MouseToolsListener());

        MouseTools = new ButtonGroup();
        MouseTools.add(HandTool);
        MouseTools.add(SelectTool);
        add(HandTool);
        add(SelectTool);
    }

    public boolean isDesignMode() {
        return DesignView.isSelected();
    }

    public boolean isHandModeActive() {
        return HandTool.isSelected();
    }

    class MouseToolsListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == HandTool)
            {
                HandTool.setSelected(true);
                HandTool.setIcon(HandIcon);
                SelectTool.setIcon(SelectIconReleased);
            }
            else
            {
                SelectTool.setSelected(true);
                HandTool.setIcon(HandIconReleased);
                SelectTool.setIcon(SelectIcon);
            }
        }

    }
}