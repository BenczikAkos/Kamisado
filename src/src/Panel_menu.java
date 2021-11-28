package src;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Panel_menu extends JPanel{
	Panel_menu() {	
		super(new GridLayout(7, 5));
		addEmptyRow();
		addTextCenter("title.jpg");
		addEmptyRow();
		addButtonCenter("Play!");
		addEmptyRow();
		addButtonCenter("Load");
		addEmptyRow();

	}

	private void addEmptyRow() {
		int columns = (int)((GridLayout) this.getLayout()).getColumns();
		for(int i = 0; i < columns; ++i) {
			add(new Canvas());
		}
	}

	private void addEmptyHalfRow() {
		int columns = (int)((GridLayout) this.getLayout()).getColumns();
		for(int i = 0; i < columns/2; ++i) {
			add(new Canvas());
		}
	}

	private void addTextCenter(String s) {
		addEmptyHalfRow(); add(new JLabel(new ImageIcon(s))); addEmptyHalfRow();
	}
	
	private void addButtonCenter(String s) {
		addEmptyHalfRow(); add(new JButton(s)); addEmptyHalfRow();
	}


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

		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		JButton aiVSai = new JButton("AI vs. AI");
		aiVSai.setAlignmentX(CENTER_ALIGNMENT);
		JButton aiVSp = new JButton("AI vs. Player");
		aiVSp.setAlignmentX(CENTER_ALIGNMENT);
		JButton pVSai = new JButton("Player vs. AI");
		pVSai.setAlignmentX(CENTER_ALIGNMENT);
		JButton pVSp = new JButton("Player vs. Player");
		pVSp.setAlignmentX(CENTER_ALIGNMENT);
		aiVSai.addActionListener(e -> {
			try {
				g1.initGame(setups, true, true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)main.getLayout()).next(main);
		});
		aiVSp.addActionListener(e -> {
			try {
				g1.initGame(setups, true, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)main.getLayout()).next(main);
		});
		pVSai.addActionListener(e -> {
			try {
				g1.initGame(setups, false, true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)main.getLayout()).next(main);
		});
		pVSp.addActionListener(e -> {
			try {
				g1.initGame(setups, false, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)main.getLayout()).next(main);
		});
		options.add(aiVSai);
		options.add(aiVSp);
		options.add(pVSai);
		options.add(pVSp);



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
		main.add(options);
		main.add(gaming, "gametable");
		f.add(main, "Center");
		f.pack();
		f.setVisible(true);
	}

}