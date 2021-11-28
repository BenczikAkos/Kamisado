package src;

import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelOptions extends JPanel {
	private static final long serialVersionUID = 1L;

	PanelOptions(Game g1, String[] setups){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
			((CardLayout)this.getParent().getLayout()).next(this.getParent());
		});
		aiVSp.addActionListener(e -> {
			try {
				g1.initGame(setups, true, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)this.getParent().getLayout()).next(this.getParent());
		});
		pVSai.addActionListener(e -> {
			try {
				g1.initGame(setups, false, true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)this.getParent().getLayout()).next(this.getParent());
		});
		pVSp.addActionListener(e -> {
			try {
				g1.initGame(setups, false, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			((CardLayout)this.getParent().getLayout()).next(this.getParent());
		});
		this.add(aiVSai);
		this.add(aiVSp);
		this.add(pVSai);
		this.add(pVSp);
	}
}
