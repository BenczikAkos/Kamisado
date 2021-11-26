package src;

import java.awt.Color;
import java.awt.Component;
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
	public ArrayList<Integer> fieldHighlight;
	public LinkedList<Ellipse2D> towerHighlight;

	private ResizeCalculator resizeCalc;
	
	TablePainter(Game game) { 
		this.game = game;
		fields = new LinkedList<Rectangle2D>();
		towers = new LinkedList<Ellipse2D>(); //A tornyokat t�rolja �gy (nagy k�r, kis k�r) sorozatban, balr�l jobbra, fentr�l le
		fieldHighlight = new ArrayList<Integer>();
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
	/**
	 * Kirajzolja az �sszes mez�t megfelel� sz�nnel, valamint a kiemelend� mez�kre apr� fekete k�r�ket rajzol
	 * @param g
	 */
	private void drawAllFields(Graphics2D g) {
		for(Shape s: fields) {
			int indice = fields.indexOf(s);
			Field currField = game.table.get(indice);
			Color currColor = currField.getColor();
			g.setColor(currColor);
			g.fill(s);
			if(fieldHighlight.contains(indice)) {
				highlightField(s, g);
			}
		}
	}
	/**
	 * Kirajzolja az �sszes tornyot ami k�t k�rb�l �ll; egy nagyobb ami fekete vagy feh�r �s a k�zep�n egy kisebb, sz�nes k�r
	 * @param g
	 */
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
	/**
	 * A megadott alakzat k�zep�re egy kis fekete k�rt rajzol, ezzel mutatja hogy r� lehet az adott mez�re l�pni
	 * @param s
	 * 			Az alakzat (mez�t jelent� n�gyzet), aminek a k�zep�re fekete k�rt rajzol
	 * @param g
	 */
	private void highlightField(Shape s, Graphics2D g) {
		Rectangle2D field = s.getBounds2D();
		double centerx = field.getCenterX(); double centery = field.getCenterY();
		Ellipse2D token = new Ellipse2D.Double();
		token.setFrameFromCenter(centerx, centery, centerx-field.getWidth()/8, centery-field.getHeight()/8);
		g.setColor(Color.black);
		g.fill(token);
	}
	/**
	 * Friss�ti a tornyokat t�rol� alakzatokat egy torony l�p�s�vel
	 * @param which
	 * 		Melyik (h�nyadik) torony l�pett (a j�t�k logik�ja szerint, nem a t�rol�s� szerint)
	 * @param where
	 * 		H�nyas sz�m� mez�re l�pett
	 */
	public void towerMoved(int which, int where) {
		double newx = fields.get(where).getX(); double newy = fields.get(where).getY();
		Ellipse2D bigtow = towers.get(which*2);
		double oldx = bigtow.getX(); double oldy = bigtow.getY();
		double offsetx = oldx-newx; double offsety = oldy-newy; //Amennyivel  arr�bbker�lt a nagy k�r, annyival fog arr�bbker�lni a kicsi is
		double bigsizex = bigtow.getWidth(); double bigsizey = bigtow.getHeight();
		Ellipse2D smalltow = towers.get(which*2+1);
		double smallsizex = smalltow.getWidth(); double smallsizey = smalltow.getHeight();
		double smallnewx = smalltow.getX()-offsetx; double smallnewy = smalltow.getY()-offsety;
		towers.set(which*2, new Ellipse2D.Double(newx, newy, bigsizex, bigsizey));
		towers.set(which*2+1, new Ellipse2D.Double(smallnewx, smallnewy, smallsizex, smallsizey));
	}
	
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			Tower actTower = game.getActiveTower();
			if(actTower == null) {
				int whichTower = 7;
				for(int i = 16; i < 32; i += 2) {
					if(towers.get(i).contains(e.getPoint())) {
						whichTower++; break;
					}
					else
						whichTower++;
				}
				Tower chosenTower = game.towers.get(whichTower);
				game.newAvaibles(chosenTower.activate());
				repaint();
				game.setActiveTower(chosenTower);
				return;
			}
			int whichField = -1;
			for(Shape s: fields) {
				if(s.contains(e.getPoint())) {
					whichField++; break;
				}
				else
					whichField++;
			}
			if(actTower.moveto(game.table.get(whichField))) {
				int whichTower = game.towers.indexOf(actTower);
				towerMoved(whichTower, whichField);
				repaint();				
			}
		}
	}
	
	private class ResizeListener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			TablePainter p = (TablePainter)e.getComponent();
			p.resizeShapes();
			repaint();
//			Tower testTower = game.towers.get(2);
//			testTower.moveto(game.table.get(42));
//			ArrayList<Field> avaible = testTower.activate();
			//highlightFields(avaible);
		}
	}
}
