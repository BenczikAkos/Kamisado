package src;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
//import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.SwingConstants;
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
		/*JFrame f = new JFrame("Kamisado");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.add(new Panel_menu());
		f.setMinimumSize(new Dimension(880, 620));
		f.pack();
		f.setVisible(true);*/
		Game g1 = new Game(8,8);
		String[] setups = {"front_init.txt", "back_init.txt", "color_init.txt", "tower_init.txt"};
		try {
			g1.initGame(setups);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*
		JFrame f = new JFrame("Table");
		f.setMinimumSize(new Dimension(780, 780));
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JPanel p = new JPanel(new BorderLayout());
		TablePainter painter = new TablePainter(g1);
		p.add("Center", painter);
		f.add("Center", p);
		f.pack();
		f.setVisible(true);
		//*/
	}

}