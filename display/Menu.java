package display;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import javax.swing.event.*;

@SuppressWarnings("serial") 
public class Menu extends JPanel {
    public static final int width = 400;
    public static final int height = 1000;

    JPanel SliderPanel, EmptyPanel;
    JButton TimePlusButton, TimeMinusButton, ThreadPlusButton, ThreadMinusButton, MassPlusButton, MassMinusButton;
    JSlider TimeSlider, MassSlider;
    JLabel  GeneralLabel, ThreadsLabel, TimeLabel, MassLabel;
    JTextField MassTextField;

    Box GeneralBox;

    int timevalue=0, threads=0, mass=0, mass_range=1000;
 
    public Menu()
    {
        EmptyPanel = new JPanel();
        setPreferredSize(new Dimension(width, height));
        GeneralBox = Box.createVerticalBox();
        setBorder(BorderFactory.createTitledBorder("Main Menu:"));

        setTimeOptions();
        setThreadOptions();
        setMassOptions();

        this.add(GeneralBox);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //TimeLabel.repaint();
    }

    private void setTimeOptions()
    {           
        JPanel TimeBoxH = new JPanel();                                   //  +====== vertical  box ======+
        Box TimeBoxV = Box.createVerticalBox();                           //  |       |  h. box1 |        |
        TimePlusButton = new JButton("Time++");                           //  +---------------------------+             
        TimeMinusButton = new JButton("Time--");                          //  |           Slider          |
        TimeLabel = new JLabel(" 0 ");                                    //  +---------------------------+
        TimeSlider = new JSlider(0, 20, 0);

        TimePlusButton.addActionListener(new TimePlusListener());
        TimeMinusButton.addActionListener(new TimeMinusListener());
        TimeSlider.addChangeListener(new TimeSliderListener());

        TimeBoxH.setLayout(new BoxLayout(TimeBoxH, BoxLayout.X_AXIS));

        TimeMinusButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        TimeBoxH.add(TimeMinusButton);
        TimeBoxH.add(Box.createHorizontalStrut(50));
        TimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        TimeBoxH.add(TimeLabel);
        TimeBoxH.add(Box.createHorizontalStrut(50));
        TimePlusButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        TimeBoxH.add(TimePlusButton);

        TimeBoxV.add(TimeBoxH);
        TimeBoxV.add(Box.createVerticalStrut(20));

        TimeSlider.setMinorTickSpacing(1);
        TimeSlider.setMajorTickSpacing(5);
        TimeSlider.setPaintTicks(true);

        Hashtable<Integer,JLabel> labelTable1 = new Hashtable<>();
        labelTable1.put( 0, new JLabel("Time stands stil...") );
        TimeSlider.setLabelTable( labelTable1 );
        TimeSlider.setPaintLabels(true);

        TimeBoxV.add(TimeSlider);
        GeneralBox.add(TimeBoxV);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    private void setThreadOptions()
    {
        JPanel ThreadBox  = new JPanel(new GridLayout(1, 3));
        ThreadPlusButton = new JButton("Thread++");
        ThreadMinusButton = new JButton("Thread--");
        ThreadsLabel = new JLabel("Threads: 0");

        ThreadPlusButton.addActionListener(new ThreadPlusListener());
        ThreadMinusButton.addActionListener(new ThreadMinusListener());

        ThreadBox.setLayout(new BoxLayout(ThreadBox, BoxLayout.X_AXIS));

        ThreadBox.add(EmptyPanel); 
        ThreadBox.add(ThreadMinusButton);
        ThreadBox.add(Box.createHorizontalStrut(20));
        ThreadBox.add(ThreadsLabel);
        ThreadBox.add(Box.createHorizontalStrut(20));
        ThreadBox.add(ThreadPlusButton);

        GeneralBox.add(ThreadBox);
        GeneralBox.add(Box.createVerticalStrut(60));
    }

    private void setMassOptions()
    {
        Box MassBoxV = Box.createVerticalBox();
        JPanel MassBoxH  = new JPanel(new GridLayout(1, 4));
        MassPlusButton = new JButton("Mass++");
        MassMinusButton = new JButton("Mass--");
        MassLabel = new JLabel(" E20 [KG]");
        MassTextField = new JTextField("580");
        MassSlider = new JSlider(0, mass_range, 1);

        MassPlusButton.addActionListener(new MassPlusListener());
        MassMinusButton.addActionListener(new MassMinusListener());
        MassSlider.addChangeListener(new MassSliderListener());
        MassTextField.addActionListener(new MassTextListener());

        MassBoxH.setLayout(new BoxLayout(MassBoxH, BoxLayout.X_AXIS));

        MassBoxH.add(EmptyPanel); 
        MassBoxH.add(MassMinusButton);
        MassBoxH.add(Box.createHorizontalStrut(10));
        MassBoxH.add(MassTextField);
        MassBoxH.add(MassLabel);
        MassBoxH.add(Box.createHorizontalStrut(10));
        MassBoxH.add(MassPlusButton);

        MassBoxV.add(MassBoxH);
        MassBoxV.add(Box.createVerticalStrut(20));

        MassSlider.setMinorTickSpacing(mass_range/100);
        MassSlider.setMajorTickSpacing(mass_range/10);
        MassSlider.setPaintTicks(true);

        Hashtable<Integer,JLabel> labelTable2 = new Hashtable<>();
        labelTable2.put( 0, new JLabel("No Mass") );
        labelTable2.put( mass_range/2, new JLabel("Mass in [KG]") );
        MassSlider.setLabelTable( labelTable2 );
        MassSlider.setPaintLabels(true);
        MassSlider.setValue(580);

        SliderPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        SliderPanel.add(EmptyPanel);
        SliderPanel.add(MassSlider);
        SliderPanel.add(EmptyPanel);
        MassBoxV.add(SliderPanel);

        GeneralBox.add(MassBoxV);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    class TimePlusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            timevalue++;
            TimeLabel.setText(" "+timevalue+" ");
            TimeSlider.setValue(timevalue);
        }
    }
    class TimeMinusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            timevalue--;
            TimeLabel.setText(" "+timevalue+" ");
            TimeSlider.setValue(timevalue);
        }
    }
    class TimeSliderListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            timevalue = TimeSlider.getValue();
            TimeLabel.setText(" "+timevalue+" ");
        }
    }
    class ThreadPlusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            threads++;
            ThreadsLabel.setText("Threads: "+threads+" ");
        }
    }
    class ThreadMinusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            threads--;
            ThreadsLabel.setText("Threads: "+threads+" ");
        }
    }
    class MassPlusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            mass++;
            MassSlider.setValue(mass);
            MassTextField.setText(Integer.toString(MassSlider.getValue()));
        }
    }
    class MassMinusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            mass--;
            MassSlider.setValue(mass);
            MassTextField.setText(Integer.toString(MassSlider.getValue()));
        }
    }
    class MassSliderListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            mass = MassSlider.getValue();
            MassTextField.setText(Integer.toString(MassSlider.getValue()));
        }
    }
    class MassTextListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try
            { 
                mass = Integer.parseInt(MassTextField.getText());
            }
            catch(NumberFormatException exc)
            {
                mass = MassSlider.getValue();
            }
        }
    }
}