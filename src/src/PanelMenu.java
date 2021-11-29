package src;

import java.awt.CardLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * Kezd� oldalt megval�s�t� panel. Van rajta egy j�t�k gomb, illetve egy elmentett j�t�k bet�lt�s�t lehet�v� tev� gomb.
 * @author �kos
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
					JPanel loadGame = new PanelGame(g);
					Container parent = this.getParent();
					parent.add(loadGame, "load");
					((CardLayout)parent.getLayout()).show(parent, "load");
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
