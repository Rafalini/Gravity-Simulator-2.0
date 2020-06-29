import display.Menu;
import display.SpaceMap;
import display.shapes.*;

import java.awt.*;
import javax.swing.*;

//Main class to start, create frame and initiate and all elements

@SuppressWarnings("serial") 
public class Main extends JPanel {
	public static void main(String[] args) {
		new Arrow();
		SwingUtilities.invokeLater(()->{
			Main mainpanel = new Main();
			JFrame frame = new JFrame("Grav Sim 2.0");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(mainpanel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
	
	public Main() {
		setLayout(new BorderLayout());
		Menu newmenu = new Menu();
        add(newmenu , BorderLayout.LINE_END);
		add(new SpaceMap(newmenu), BorderLayout.CENTER);
	}
}