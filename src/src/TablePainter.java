package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class TablePainter extends Component {
	public Game game;
	public LinkedList<Rectangle2D> fields;
	public LinkedList<Ellipse2D> towers;
	public LinkedList<Ellipse2D> fieldHighlight;
	public LinkedList<Ellipse2D> towerHighlight;

	private ResizeCalculator resizeCalc;
	
	TablePainter(Game game) { 
		this.game = game;
		fields = new LinkedList<Rectangle2D>();
		towers = new LinkedList<Ellipse2D>(); //A tornyokat tárolja úgy (nagy kör, kis kör) sorozatban, balról jobbra, fentrõl le
		fieldHighlight = new LinkedList<Ellipse2D>();
		towerHighlight = new LinkedList<Ellipse2D>();
		resizeCalc = new SquareResizeCalculator(this, game.tablesize.height, game.towers.size());
		addMouseListener(new MyMouseListener());
		addComponentListener(new ResizeListener());
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.white);
		drawAllFields(g2);
		drawAllTowers(g2);
		
	}
	public void repaint() { 
		paint(this.getGraphics());
	}
	
	public void resizeShapes() {
		resizeCalc.resize();
	}
	
	private void drawAllFields(Graphics2D g) {
		for(Shape s: fields) {
			int indice = fields.indexOf(s);
			Field currField = game.table.get(indice);
			Color currColor = currField.getColor();
			g.setColor(currColor);
			g.fill(s);
		}
	}
	
	private void drawAllTowers(Graphics2D g) {
		for(int i = 0; i < towers.size(); i +=2) {
			Tower currTower = game.towers.get(i/2);
			if(currTower.getDirType().equals(DirType.UP))
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
				g.fill(towers.get(i));
		}
		for(int i = 1; i < towers.size(); i +=2) {
			Tower currTower = game.towers.get(i/2);
			Color currColor = currTower.getColor();
			g.setColor(currColor);
			g.fill(towers.get(i));
		}
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

	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			Tower testTower = game.towers.get(2);
			testTower.moveto(game.table.get(42));
			ArrayList<Field> avaible = testTower.activate();
			repaint();
			highlightFields(avaible);
		}
	}
	
	private class ResizeListener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			TablePainter p = (TablePainter)e.getComponent();
			p.resizeShapes();
			repaint();
			Tower testTower = game.towers.get(2);
			testTower.moveto(game.table.get(42));
			ArrayList<Field> avaible = testTower.activate();
			highlightFields(avaible);
		}
	}
}
