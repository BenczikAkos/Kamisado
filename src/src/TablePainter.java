package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TablePainter extends Component {
	private Game game;
	TablePainter(Game game) { 
		this.game = game;
		addMouseListener(new MyMouseListener());
	}
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension size = getSize();
        g2.setBackground(Color.white);
        int square = Math.min(size.height, size.width);
        int lilsquare = square/8;
        for(int i = 0; i < game.tablesize.getHeight(); i++) {
        	for(int ii = 0; ii < game.tablesize.getWidth(); ii++) {
        		int fieldNum = i*8+ii;
        		Field currField = game.table.get(fieldNum);
        		Color FieldColor = currField.getColor();
        		int x = ii*lilsquare; int y = i*lilsquare;
        		g2.setColor(FieldColor); 
        		g2.fillRect(x, y, lilsquare, lilsquare);
        		Tower currTower = currField.getCurrTower();
        		if(currTower != null) {
                	Color playerColor;
        			if(currTower.getDirType().equals(DirType.UP)) {
                		playerColor = Color.white;
                	}
                	else {
                		playerColor = Color.black;
                	}
        			g2.setColor(playerColor);
                	g2.fillOval(x, y, lilsquare, lilsquare);
                	g2.setColor(currTower.getColor());
                	double rad = lilsquare/2;
                	g2.fill(new Ellipse2D.Double(x+rad/2, y+rad/2, rad, rad));
        		}
        	}
        }
	}
	public void repaint() {
		paint(this.getGraphics());
	}
	
	public void highlightFields(ArrayList<Field> fields) {
		for(Field f: fields) {
			int fieldId = game.table.indexOf(f);
			int row = fieldId/8;
			int column = fieldId%8;
			Graphics2D g2 = (Graphics2D)this.getGraphics();
			Dimension size = getSize();
	        int square = Math.min(size.height, size.width);
	        int lilsquare = square/8;
	        double rad = lilsquare/5;
	        double x = (column+0.5)*lilsquare-rad/2; double y = (row+0.5)*lilsquare-rad/2;
    		g2.setColor(Color.black); 
    		g2.fill(new Ellipse2D.Double(x, y, rad, rad));
		};
	}

	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Tower testTower = game.towers.get(2);
			testTower.moveto(game.table.get(42));
			ArrayList<Field> avaible = testTower.activate();
			repaint();
			highlightFields(avaible);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
