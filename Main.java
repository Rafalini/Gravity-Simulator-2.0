import display.Menu;
import display.SpaceMap;
import display.UpperPanel;

import java.awt.*;
import javax.swing.*;

//Main class to start, create frame and initiate and all elements

public class Main{
	public static void main(String[] args) {
		/*SwingUtilities.invokeLater(()->{
			Main mainpanel = new Main();
			JFrame frame = new JFrame("Grav Sim 2.0");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(mainpanel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});*/
		JFrame frame = new JFrame("Grav Sim 2.0");
		Menu newmenu = new Menu();
		UpperPanel upperPanel = new UpperPanel();
		SpaceMap newmap = new SpaceMap(newmenu, upperPanel);
		JPanel mappanel = new JPanel();

		mappanel.add(upperPanel,BorderLayout.PAGE_START);
		mappanel.add(newmap ,BorderLayout.CENTER);
		mappanel.add(new JPanel() ,BorderLayout.PAGE_END);

		frame.getContentPane().add(mappanel, BorderLayout.CENTER);
		frame.getContentPane().add(newmenu, BorderLayout.LINE_END);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		newmenu.setFocusable(false);
		upperPanel.setFocusable(false);
		newmap.setFocusable(true);
        newmap.requestFocus();
	}
	/*public Main() {
		setLayout(new BorderLayout());
		Menu newmenu = new Menu();
        add(newmenu, BorderLayout.LINE_END);
		add(new SpaceMap(newmenu), BorderLayout.CENTER);
	}*/
}