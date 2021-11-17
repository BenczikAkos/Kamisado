package src;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Panel_menu extends JPanel{
	Panel_menu() {	
		super(new GridLayout(7, 5));
		addEmptyRow();
		addTextCenter("KAMISADO");
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
		addEmptyHalfRow(); add(new JLabel(s, SwingConstants.CENTER)); addEmptyHalfRow();
	}
	
	private void addButtonCenter(String s) {
		addEmptyHalfRow(); add(new JButton(s)); addEmptyHalfRow();
	}


	public static void main(String[] args) {
		JFrame f = new JFrame("Kamisado");
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.add(new Panel_menu());
		f.setMinimumSize(new Dimension(880, 620));
		f.pack();
		f.setVisible(true);

	}

}