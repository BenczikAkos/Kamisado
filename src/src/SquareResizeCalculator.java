package src;

import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
/**
 * Az megjelenítendõ alakzatok átméretezése a feladata. Megadott számú négyzet alakú mezõt - akik valami n*n-es elrendezésben vannak - 
 * valamint megadott számú (nagykör, kiskör) formátumban tárolt tornyok (bábuk) új méretét számolja ki az ablak méretéhez
 * @author Ákos
 *
 */
public class SquareResizeCalculator extends ResizeCalculator {
	private static final long serialVersionUID = 1L;
	private int rows, towers;
	public SquareResizeCalculator(TablePainter painter, int size, int towers) { 
		super(painter); 
		rows = size;
		this.towers = towers;
	}
	
	/**
	 * Újraméretezi az összes alakzatot, azaz az összes mezõt és összes tornyot.
	 */
	public void resize() {
		resizeAllFields();
		resizeAllTowers();
	}
	
	/**
	 * Újraméretezi az összes mezõt. Felteszi, hogy a mezõk n*n-es elrendezésben vannak, négyzet alakúak
	 * és a lehetõ leginkább ki kell tölteni az ablakot. Ha még a TablePainterben nincsenek mezõket reprezentáló alakzatok,
	 * feltölti azokat (inicializál is kezdetben)
	 */
	private void resizeAllFields() {
		LinkedList<Rectangle2D.Double> gameFields = p.fields;
		Dimension d = p.getSize();
		double square = Math.min(d.height, d.width);
        double lilsquare = square/rows;
        int towidx = 0;
        for(int i = 0; i < rows; ++i) {
        	for(int ii = 0; ii < rows; ++ii) {
        		int idx = i*rows+ii;
        		double bigx = ii*lilsquare; double bigy = i*lilsquare;
        		Rectangle2D.Double rect = new Rectangle2D.Double(bigx, bigy, lilsquare, lilsquare);
        		if(idx < gameFields.size()) {
        			gameFields.set(idx, rect);
        		}
        		else {
        			gameFields.add(rect);
        		}
        	}
        }   
	}
	
	/**
	 * Újraméretezi az összes tornyot (towers darabot). Felteszi hogy a a tornyok (nagykör, kiskör) elrendezésben vannak tárolva
	 */
	private void resizeAllTowers() {
		LinkedList<Ellipse2D.Double> gameTowers = p.towers;
		Dimension d = p.getSize();
		double square = Math.min(d.height, d.width);
        double lilsquare = square/rows;
        for(int i = 0; i < towers; ++i) {
        	Tower currTower = p.game.getTower(i);
        	Field currField = currTower.getCurrField();
 		    int idx = p.game.fieldIndex(currField);
        	double bigx = (idx%rows)*lilsquare; double bigy = idx/rows*lilsquare;
        	double rad = lilsquare / 2;
    		double smallx = bigx+lilsquare/2-rad/2; double smally = bigy+lilsquare/2-rad/2;
			Ellipse2D.Double bigtower = new Ellipse2D.Double(bigx, bigy, lilsquare, lilsquare);
			Ellipse2D.Double smalltower = new Ellipse2D.Double(smallx, smally, rad, rad);
    		if(towers*2-1 < gameTowers.size()) {
    			gameTowers.set(2*i, bigtower);
    			gameTowers.set(2*i+1, smalltower);
    		}
    		else {
    			gameTowers.add(bigtower);
    			gameTowers.add(smalltower);
    		}
        }
	}
}
