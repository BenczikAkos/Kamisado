package src;

import java.awt.CardLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * Kezdõ felületet megvalósító panel. Van rajta egy játék gomb, illetve egy elmentett játék betöltését lehetõvé tevõ gomb.
 * @author Ákos
 *
 */
public class PanelMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	PanelMenu(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JButton play = new JButton("Play!");
		play.setAlignmentX(CENTER_ALIGNMENT);
		JFileChooser loadMenu = new JFileChooser("Load");
		JButton load = new JButton("Load");
		load.setAlignmentX(CENTER_ALIGNMENT);
		play.addActionListener(e -> ((CardLayout)this.getParent().getLayout()).next(this.getParent()));
		load.addActionListener(e -> {
			int userSelection = loadMenu.showSaveDialog(null);		 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileFromLoad = loadMenu.getSelectedFile();
				try {
					FileInputStream fInStr = new FileInputStream(fileFromLoad);
					ObjectInputStream in = new ObjectInputStream(fInStr);
					Game g = (Game)in.readObject();
					in.close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		this.add(play);
		this.add(load);
	}
}
