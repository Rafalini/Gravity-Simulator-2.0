import display.Menu;
import display.SpaceMap;
import display.shapes.*;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial") 
public class Main extends JPanel {
	public static void main(String[] args) {
		new Arrow();
		SwingUtilities.invokeLater(()->{
			Main sizingTest = new Main();
			JFrame frame = new JFrame("Grav Sim 2.0");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(sizingTest);
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