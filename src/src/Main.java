package src;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Main extends JPanel{

	public static void main(String[] args) {
		Game g1 = new Game(8,8);
		String[] setups = {"front_init.txt", "color_init.txt", "tower_init.txt"};

		JFrame f = new JFrame("Kamisado");
		f.setMinimumSize(new Dimension(725, 780));
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel main = new JPanel(new CardLayout());
		JPanel menu = new PanelMenu();
		JPanel options = new PanelOptions(g1, setups);
		JPanel gaming = new PanelGame(g1);
		main.add(menu, "menu");
		main.add(options, "options");
		main.add(gaming, "gametable");
		f.add(main, "Center");
		f.pack();
		f.setVisible(true);
	}

}