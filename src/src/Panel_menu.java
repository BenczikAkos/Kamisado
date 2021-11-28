package src;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Panel_menu extends JPanel{

	public static void main(String[] args) {

		Game g1 = new Game(8,8);
		String[] setups = {"front_init.txt", "back_init.txt", "color_init.txt", "tower_init.txt"};

		JFrame f = new JFrame("Kamisado");
		f.setMinimumSize(new Dimension(725, 780));
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel main = new JPanel(new CardLayout());

		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		JButton play = new JButton("Play!");
		play.setAlignmentX(CENTER_ALIGNMENT);
		JButton load = new JButton("Load");
		load.setAlignmentX(CENTER_ALIGNMENT);
		play.addActionListener(e -> ((CardLayout)main.getLayout()).next(main));
		
		menu.add(play);
		menu.add(load);

		JPanel options = new PanelOptions(g1, setups);

		JPanel gaming = new JPanel(new BorderLayout());
		TablePainter painter = new TablePainter(g1);
		JFileChooser saveMenu = new JFileChooser("Save");
		JButton saveButton = new JButton("Save");
		saveButton.setMaximumSize(new Dimension(100, 20));
		saveButton.addActionListener(e -> {
			int userSelection = saveMenu.showSaveDialog(f);		 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = saveMenu.getSelectedFile();
				try {
					FileOutputStream fOutStr = new FileOutputStream(fileToSave);
					ObjectOutputStream OutStr;
					OutStr = new ObjectOutputStream(fOutStr);
					OutStr.writeObject(g1);
					OutStr.close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		g1.setPainter(painter);
		gaming.add("Center", painter);
		gaming.add("North", saveButton);
		
		main.add(menu, "menu");
		main.add(options, "options");
		main.add(gaming, "gametable");
		f.add(main, "Center");
		f.pack();
		f.setVisible(true);
	}

}