package display;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ChangeListener;
import java.awt.Toolkit;

import display.SpaceMap.ResetButtonListener;
import gravity.GravityManager;
import space_objects.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

//Main menu, panel with all buttons, layout, button listeners etc


@SuppressWarnings("serial") 
public class Menu extends JPanel {
    public static final int width = 400;
    public static final int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-200;

    GravityManager currentGravManager;
    SpaceMap myMap;

    JPanel SliderPanel;
    JComboBox<String> ObjType;
    JButton TimePlusButton, TimeMinusButton, ThreadPlusButton, ThreadMinusButton, MassPlusButton, MassMinusButton,
            PresetCicrle, PresetSpiral, PresetFireworks, PresetGrid, ResetButton, RadiusPlusButton, RadiusMinusButton;
    JSlider TimeSlider, MassSlider, /*Presets->*/ PlanetsAmount, PlanetsSizeRandom, PlanetsInitialSpeed, RadiusSlider;
    JLabel  GeneralLabel, MassLabel, ThreadsLabel, TimeLabel, RadiusLabel;
    JTextField MassTextField, ThreadsTextField, TimeTextField, RadiusTextField;
    JTextArea LiveLog;
    Box GeneralBox;

    volatile boolean reset=false;
    final int mass_range=10000, radius_range=500, time_limit = 300;
    int timevalue=0, threads=1, mass=581, radius=60;
 
    public Menu()
    {
        setPreferredSize(new Dimension(width, height));
        GeneralBox = Box.createVerticalBox();
        setBorder(BorderFactory.createTitledBorder("Main Menu:"));

        setTimeOptions();
        setThreadOptions();
        setMassOptions();
        //setSizeOptions();
        setObjTypePreset();
        setPresetButtons();
        setEventLog();
        setMainButtons();

        printOnLog("Screen Width:  "+(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        printOnLog("Screen Height: "+(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    
        GeneralBox.add(Box.createVerticalStrut(10));
        this.add(GeneralBox);
    }

    public void setTime(int k) {timevalue=0;}

    public int getMassValue() {return mass;}

    public int getRadiusValue() {return radius;}

    public void addResetListener(ResetButtonListener ls) {ResetButton.addActionListener(ls);}

    public void addMap(SpaceMap map) {myMap=map;}

    public int getTimeValue() {return timevalue;}

    public int getThreadsValue() {return threads;}

    public boolean tryReset()
    { 
        if(reset)
        {
            reset = false;
            return true;
        }
        return false;
    }

    public void sendGravityManager(GravityManager manager)
    {
        currentGravManager = manager;
        new Thread(currentGravManager).start();
    }

    private void setMainButtons()
    {
        JPanel setMainButtons = new JPanel();
        setMainButtons.setLayout(new BoxLayout(setMainButtons, BoxLayout.X_AXIS));
        setMainButtons.setBorder(BorderFactory.createTitledBorder("Top1 buttons:"));

        ResetButton = new JButton("Clear map");
        //ResetButton.addActionListener(new ResetButtonListener());

        setMainButtons.add(ResetButton);
        GeneralBox.add(setMainButtons);
        GeneralBox.add(Box.createVerticalStrut(10));
    }

    private void setObjTypePreset()
    {
        JPanel PresetsPanel = new JPanel();
        JLabel preset = new JLabel("<html>Default object type that is created: <br/>'User' option sets indywidualny each object</html>");
        PresetsPanel.setBorder(BorderFactory.createTitledBorder("  Space object to create:"));
        PresetsPanel.setLayout(new BoxLayout(PresetsPanel, BoxLayout.X_AXIS));
        String [] setupoptions = {"Planet", "Star", "Comet"};
        ObjType = new JComboBox<String>(setupoptions);

        PresetsPanel.add(preset);
        PresetsPanel.add(ObjType);

        GeneralBox.add(PresetsPanel);
        GeneralBox.add(Box.createVerticalStrut(10));
    }

    private void setTimeOptions()
    {           
        JPanel TimeBoxH = new JPanel();                                 
        Box TimeBoxV = Box.createVerticalBox();                           
        TimePlusButton = new JButton("Time++");                    
        TimeMinusButton = new JButton("Time--"); 
        TimeLabel = new JLabel("Actual Time: ");
        TimeTextField = new JTextField("0     ");                                   
        TimeSlider = new JSlider(0, time_limit, 0);

        TimePlusButton.addActionListener(new TimePlusListener(this));
        TimeMinusButton.addActionListener(new TimeMinusListener());
        TimeSlider.addChangeListener(new TimeSliderListener());

        TimeTextField.setEditable(false);
        TimeBoxH.setLayout(new BoxLayout(TimeBoxH, BoxLayout.X_AXIS));

        TimeMinusButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        TimeBoxH.add(TimeMinusButton);
        TimeBoxH.add(Box.createHorizontalStrut(50));
        TimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        TimeBoxH.add(TimeLabel);
        TimeBoxH.add(TimeTextField);
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
        JPanel ThreadBox  = new JPanel();
        ThreadPlusButton = new JButton("Thread++");
        ThreadMinusButton = new JButton("Thread--");
        ThreadsLabel = new JLabel("Threads: ");
        ThreadsTextField = new JTextField("1");

        ThreadPlusButton.addActionListener(new ThreadPlusListener());
        ThreadMinusButton.addActionListener(new ThreadMinusListener());

        ThreadBox.setLayout(new BoxLayout(ThreadBox, BoxLayout.X_AXIS));
        ThreadsTextField.setEditable(false);

        ThreadBox.add(ThreadMinusButton);
        ThreadBox.add(Box.createHorizontalStrut(20));
        ThreadBox.add(ThreadsLabel);
        ThreadBox.add(ThreadsTextField);
        ThreadBox.add(Box.createHorizontalStrut(20));
        ThreadBox.add(ThreadPlusButton);

        GeneralBox.add(ThreadBox);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    private void setMassOptions()
    {
        Box MassBoxV = Box.createVerticalBox();
        JPanel MassBoxH  = new JPanel();
        MassPlusButton = new JButton("Mass++");
        MassMinusButton = new JButton("Mass--");
        MassLabel = new JLabel(" E20 [KG]");
        MassTextField = new JTextField(""+mass);
        MassSlider = new JSlider(0, mass_range, 1);

        MassPlusButton.addActionListener(new MassPlusListener());
        MassMinusButton.addActionListener(new MassMinusListener());
        MassSlider.addChangeListener(new MassSliderListener());
        MassTextField.addActionListener(new MassTextListener());

        MassBoxH.setLayout(new BoxLayout(MassBoxH, BoxLayout.X_AXIS));

        MassBoxH.add(MassMinusButton);
        MassBoxH.add(Box.createHorizontalStrut(10));
        MassBoxH.add(MassTextField);
        MassBoxH.add(MassLabel);
        MassBoxH.add(Box.createHorizontalStrut(10));
        MassBoxH.add(MassPlusButton);

        MassBoxV.add(MassBoxH);
        MassBoxV.add(Box.createVerticalStrut(10));

        MassSlider.setMinorTickSpacing(mass_range/100);
        MassSlider.setMajorTickSpacing(mass_range/10);
        MassSlider.setPaintTicks(true);

        Hashtable<Integer,JLabel> labelTable2 = new Hashtable<>();
        labelTable2.put( 0, new JLabel("No Mass") );
        labelTable2.put( mass_range/2, new JLabel("Mass in [KG]") );
        MassSlider.setLabelTable( labelTable2 );
        MassSlider.setPaintLabels(true);
        MassSlider.setValue(mass);

        SliderPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        SliderPanel.add(MassSlider);
        MassBoxV.add(SliderPanel);

        GeneralBox.add(MassBoxV);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    private void setSizeOptions()
    {
        Box RadiusBoxV = Box.createVerticalBox();
        JPanel RadiusPanelH  = new JPanel();
        RadiusPlusButton = new JButton("Radius++");
        RadiusMinusButton = new JButton("Radius--");
        RadiusLabel = new JLabel(" E2 [KM]");
        RadiusTextField = new JTextField(""+radius);
        RadiusSlider = new JSlider(0, radius_range, 1);

        RadiusPlusButton.addActionListener(new RadiusPlusListener());
        RadiusMinusButton.addActionListener(new RadiusMinusListener());
        RadiusSlider.addChangeListener(new RadiusSliderListener());
        RadiusTextField.addActionListener(new RadiusTextListener());

        RadiusPanelH.setLayout(new BoxLayout(RadiusPanelH, BoxLayout.X_AXIS));

        RadiusPanelH.add(RadiusMinusButton);
        RadiusPanelH.add(Box.createHorizontalStrut(10));
        RadiusPanelH.add(RadiusTextField);
        RadiusPanelH.add(RadiusLabel);
        RadiusPanelH.add(Box.createHorizontalStrut(10));
        RadiusPanelH.add(RadiusPlusButton);

        RadiusBoxV.add(RadiusPanelH);
        RadiusBoxV.add(Box.createVerticalStrut(10));

        RadiusSlider.setMinorTickSpacing(radius_range/100);
        RadiusSlider.setMajorTickSpacing(radius_range/10);
        RadiusSlider.setPaintTicks(true);

        Hashtable<Integer,JLabel> labelTable2 = new Hashtable<>();
        labelTable2.put( 0, new JLabel("0 KM") );
        labelTable2.put( mass_range/2, new JLabel("Radius in [KM]") );
        RadiusSlider.setLabelTable( labelTable2 );
        RadiusSlider.setPaintLabels(true);
        RadiusSlider.setValue(radius);

        SliderPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        SliderPanel.add(RadiusSlider);
        RadiusBoxV.add(SliderPanel);

        GeneralBox.add(RadiusBoxV);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    private void setPresetButtons()
    {
        JPanel PresetsPanel = new JPanel();
        PresetsPanel.setBorder(BorderFactory.createTitledBorder("Presets:"));
        PresetsPanel.setLayout(new BoxLayout(PresetsPanel, BoxLayout.X_AXIS));
        Box VpresetBox = Box.createVerticalBox();
        PresetCicrle =      new JButton("Circles");
        PresetSpiral =      new JButton("Spiral");
        PresetFireworks =   new JButton("Fireworks");
        PresetGrid =        new JButton("Grid");

        PlanetsAmount =         new JSlider(JSlider.VERTICAL, 0, 100, 30);
        PlanetsSizeRandom =     new JSlider(JSlider.VERTICAL, 0, 100, 30);
        PlanetsInitialSpeed =   new JSlider(JSlider.VERTICAL, 0, 200, 60);

        PlanetsAmount.setPaintLabels(true);
        PlanetsSizeRandom.setPaintLabels(true);
        PlanetsInitialSpeed.setPaintLabels(true);

        Hashtable<Integer,JLabel> PlanetsAmountLabel = new Hashtable<>();
        PlanetsAmountLabel.put( 0, new JLabel("0 Planets") );
        PlanetsAmountLabel.put( 50, new JLabel("Quantity") );
        PlanetsAmountLabel.put( 100, new JLabel("100 Planets") );
        PlanetsAmount.setLabelTable( PlanetsAmountLabel );

        Hashtable<Integer,JLabel> PlanetsRandomLabel = new Hashtable<>();
        PlanetsRandomLabel.put( 0, new JLabel("0%") );
        PlanetsRandomLabel.put( 50, new JLabel("Random V") );
        PlanetsRandomLabel.put( 100, new JLabel("100%") );
        PlanetsSizeRandom.setLabelTable( PlanetsRandomLabel );

        Hashtable<Integer,JLabel> PlanetsSpeedLabel = new Hashtable<>();
        PlanetsSpeedLabel.put( 0, new JLabel("0 [KM/h]") );
        PlanetsSpeedLabel.put( 100, new JLabel("Speed") );
        PlanetsSpeedLabel.put( 200, new JLabel("200 [KM/h]") );
        PlanetsInitialSpeed.setLabelTable( PlanetsSpeedLabel );

        PresetCicrle.addActionListener(new PresetCirclesListener());
        PresetSpiral.addActionListener(new PresetSpiralListener());
        PresetGrid.addActionListener(new PresetGridListener());
        PresetFireworks.addActionListener(new PresetFireworksListener());

        VpresetBox.add(PresetFireworks);
        VpresetBox.add(Box.createVerticalStrut(20));
        VpresetBox.add(PresetCicrle);
        VpresetBox.add(Box.createVerticalStrut(20));
        VpresetBox.add(PresetSpiral);
        VpresetBox.add(Box.createVerticalStrut(20));
        VpresetBox.add(PresetGrid);
        VpresetBox.add(Box.createVerticalStrut(20));

        PresetsPanel.add(VpresetBox);
        PresetsPanel.add(Box.createHorizontalStrut(20));
        PresetsPanel.add(PlanetsAmount);
        PresetsPanel.add(PlanetsSizeRandom);
        PresetsPanel.add(PlanetsInitialSpeed);

        GeneralBox.add(PresetsPanel);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    private void setEventLog()
    {
        JPanel LogPanel = new JPanel();
        LogPanel.setBorder(BorderFactory.createTitledBorder("Events log:"));
        LogPanel.setLayout(new BoxLayout(LogPanel, BoxLayout.Y_AXIS));

        LiveLog = new JTextArea();
        //LiveLog.setPreferredSize(new Dimension (30,30));
        LiveLog.setText("LiveLog activated!\n");
        LiveLog.setLineWrap(true);
        LiveLog.setWrapStyleWord(true);
        JScrollPane scrollbar = new JScrollPane(LiveLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollbar.setPreferredSize(new Dimension (150,200));

        LogPanel.add(scrollbar);
        GeneralBox.add(LogPanel);
        GeneralBox.add(Box.createVerticalStrut(20));
    }

    public void printOnLog(String message)
    {
        LiveLog.append(message+"\n");
        LiveLog.setCaretPosition(LiveLog.getDocument().getLength());
    }

    public int threadValue() { return Integer.parseInt(ThreadsTextField.getText()); }

    public String choosenObjectType()  { return ObjType.getSelectedItem().toString(); }

    class TimePlusListener implements ActionListener
    {
        Menu myMenu;
        TimePlusListener (Menu menu) {myMenu = menu;}
        public void actionPerformed(ActionEvent e)
        {
            if(timevalue == 0)
            {
                    //new Thread(currentGravManager).start();
            }
            timevalue++;
            TimeTextField.setText(" "+timevalue+" ");
            TimeSlider.setValue(timevalue);
        }
    }
    class TimeMinusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(timevalue > 0)
                 timevalue--;
            TimeTextField.setText(" "+timevalue+" ");
            TimeSlider.setValue(timevalue);
        }
    }
    class TimeSliderListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            timevalue = TimeSlider.getValue();
            TimeTextField.setText(" "+timevalue+" ");
        }
    }
    class ThreadPlusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            threads++;
            ThreadsTextField.setText(String.valueOf(threads));
        }
    }
    class ThreadMinusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(threads > 1)
                threads--;
            ThreadsTextField.setText(String.valueOf(threads));
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
            if(mass > 0)
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
    class RadiusPlusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            radius++;
            RadiusSlider.setValue(radius);
            RadiusTextField.setText(Integer.toString(RadiusSlider.getValue()));
        }
    }
    class RadiusMinusListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(radius > 0)
                radius--;
            RadiusSlider.setValue(radius);
            RadiusTextField.setText(Integer.toString(RadiusSlider.getValue()));
        }
    }
    class RadiusSliderListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            radius = RadiusSlider.getValue();
            RadiusTextField.setText(Integer.toString(RadiusSlider.getValue()));
        }
    }
    class RadiusTextListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try
            { 
                radius = Integer.parseInt(RadiusTextField.getText());
            }
            catch(NumberFormatException exc)
            {
                radius = RadiusSlider.getValue();
            }
        }
    }
    class PresetCirclesListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

        }
    }
    class PresetSpiralListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {}
    }
    class PresetGridListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<SpaceObject> myList = new ArrayList<SpaceObject>();
            double planets_in_row = Math.sqrt(PlanetsAmount.getValue());
            Dimension mapSize = myMap.getMapSize();

            int Xdistance = (int)(mapSize.getWidth()/planets_in_row);
            int Ydistance = (int)(mapSize.getHeight()/planets_in_row);

            printOnLog("planets: "+planets_in_row);

            myList.add(new Star(0,0,0,0, MassSlider.getValue()*10, (int)Math.sqrt(MassSlider.getValue()*10)));
            
            for(int i=0; i<planets_in_row; i++)                 //Y: 0 |  X: 0-------> 1000
            for(int j=0; j<planets_in_row+1; j++)               //     |             #
            {                                                   //1000 v  ############      
                myList.add(new Planet((int)(90+i*Xdistance-0.5*mapSize.getWidth()), (int)(50+j*Ydistance-0.5*mapSize.getHeight()),    //Xpos, Ypos
                                        0,0, (int)(MassSlider.getValue()*0.5), (int)Math.sqrt(MassSlider.getValue()*0.5)));     //Xvel, Yvel, Mass, Radius
            }
            myMap.mergeObjectLists(myList);
        }
    }
    class PresetFireworksListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {}
    }
}