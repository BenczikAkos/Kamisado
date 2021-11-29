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

import javax.swing.JLabel;

import java.awt.Container;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;


@SuppressWarnings("serial")
public class TablePainter extends Component{
	public Game game;
	public LinkedList<Rectangle2D.Double> fields;
	public LinkedList<Ellipse2D.Double> towers;
	public ArrayList<Integer> fieldHighlight;

	private ResizeCalculator resizeCalc;
	
	TablePainter(Game game) { 
		this.game = game;
		fields = new LinkedList<Rectangle2D.Double>();
		towers = new LinkedList<Ellipse2D.Double>(); //A tornyokat t�rolja (nagy k�r, kis k�r) sorozatban, balr�l jobbra, fentr�l le
		fieldHighlight = new ArrayList<Integer>();
		addMouseListener(new UserClickedListener());
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
	/**
	 * �jram�retezi az �sszes alakzatot
	 */
	public void resizeShapes() {
		if(resizeCalc != null)
			resizeCalc.resize();
	}
	
	public void setResizeCalc() {
		resizeCalc = new SquareResizeCalculator(this, game.tablesize.height, 16);
	}
	
	/**
	 * Kirajzolja az �sszes mez�t megfelel� sz�nnel, valamint a kiemelend� mez�kre apr� fekete k�r�ket rajzol
	 * @param g
	 */
	private void drawAllFields(Graphics2D g) {
		for(Shape s: fields) {
			int indice = fields.indexOf(s);
			Field currField = game.getField(indice);
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
			Tower currTower = game.getTower(i/2);
			if(currTower.getDirType().equals(DirType.UP))
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
				g.fill(towers.get(i));
		}
		for(int i = 1; i < towers.size(); i +=2) {
			Tower currTower = game.getTower(i/2);
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
		Rectangle2D.Double actField = fields.get(where);
		double newx = actField.getX(); double newy = actField.getY();
		Ellipse2D bigtower = towers.get(which*2);
		double oldx = bigtower.getX(); double oldy = bigtower.getY();
		double offsetx = oldx-newx; double offsety = oldy-newy; //Amennyivel  arr�bbker�lt a nagy k�r, annyival fog arr�bbker�lni a kicsi is
		double bigsizex = bigtower.getWidth(); double bigsizey = bigtower.getHeight();
		Ellipse2D smalltow = towers.get(which*2+1);
		double smallsizex = smalltow.getWidth(); double smallsizey = smalltow.getHeight();
		double smallnewx = smalltow.getX()-offsetx; double smallnewy = smalltow.getY()-offsety;
		towers.set(which*2, new Ellipse2D.Double(newx, newy, bigsizex, bigsizey));
		towers.set(which*2+1, new Ellipse2D.Double(smallnewx, smallnewy, smallsizex, smallsizey));
	}
	
	/**
	 * Bejelenti a j�t�k gy�ztes�t, az ehhez tartoz� vizu�lis effekteket val�s�tja meg
	 * @param who
	 * 		A j�t�kos aki nyert
	 */
	public void win(DirType who) {
		Container parent = this.getParent();
		String winnerText = who.equals(DirType.UP) ? "White wins" : "Black wins";
		JLabel title = new JLabel(winnerText);
		parent.add("South", title);
		parent.validate();
		this.removeMouseListener(this.getMouseListeners()[0]);	
	}
	
	private class UserClickedListener extends MouseAdapter implements Serializable{
		/**
		 * Eg�rkattint�sra v�grehajtand� esem�nyeket val�s�t meg.
		 * Ha felhaszn�l� k�re j�n �s �rv�nyes mez�re kattint, odal�pteti a tornyot.
		 * Ha a g�pi ellenf�l k�re van, a g�p l�p egyet.
		 */
		public void mouseClicked(MouseEvent e) {
			DirType whoseTurn = game.getWhoseTurn();
			AI currAI = game.ai.get(whoseTurn);
			if(currAI == null) {
				userPlays(e);
			}
			else {
				AIPlays(currAI);
			}
		}
		
		/**
		 * Lej�tszat egy k�rt a felhaszn�l�val, ha nincs akt�v b�bu (a j�t�k els� k�re), kiv�laszthatja.
		 * A v�g�n friss�ti a tornyok �br�it t�rol� adatszerkezetet
		 * @param e
		 * 		A felhaszn�l� kattint�sa
		 */
		private void userPlays(MouseEvent e) {
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
				Tower chosenTower = game.getTower(whichTower);
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
			if(actTower.moveto(game.getField(whichField))) {
				int whichTower = game.towerIndex(actTower);
				towerMoved(whichTower, whichField);
				repaint();				
			}
		}
		
		/**
		 * Lej�tszat egy k�rt az AI-al, friss�ti a tornyok �br�it t�rol� adatszerkezetet
		 * @param currAI
		 * 		A soron k�vetkez� AI
		 */
		private void AIPlays(AI currAI) {
			int[] coords = new int[2];
			coords = currAI.makeMove();
			towerMoved(coords[1], coords[0]);
			repaint();
		}
	}
	
	private class ResizeListener extends ComponentAdapter {
		/**
		 * Ha �jram�retezik az ablakot, �jrasz�molja az �sszes alakzat m�ret�t, helyzet�t
		 */
		public void componentResized(ComponentEvent e) {
			TablePainter p = (TablePainter)e.getComponent();
			p.resizeShapes();
		}
		
		public void componentShown(ComponentEvent e) {
			TablePainter p = (TablePainter)e.getComponent();
			p.resizeShapes();
		}
	}
}
