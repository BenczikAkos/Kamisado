package src;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * Magát a játéktáblát megjelítõ panel egy mentést lehetõvé tevõ gombbal felül
 * @author Ákos
 *
 */
public class PanelGame extends JPanel {
	private static final long serialVersionUID = 1L;
	PanelGame(Game g1){
		this.setLayout(new BorderLayout(0,0));
		TablePainter painter = new TablePainter(g1);
		JFileChooser saveMenu = new JFileChooser("Save");
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(e -> {
			int userSelection = saveMenu.showSaveDialog(null);		 
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
		if(g1.painter == null)
			g1.setPainter(painter);
		else
			painter = g1.painter;
		this.add("Center", painter);
		this.add("North", saveButton);
	}
}
