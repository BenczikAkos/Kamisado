package src;

import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
/**
 * Az megjelen�tend� alakzatok �tm�retez�se a feladata. Megadott sz�m� n�gyzet alak� mez�t - akik valami n*n-es elrendez�sben vannak - 
 * valamint megadott sz�m� (nagyk�r, kisk�r) form�tumban t�rolt tornyok (b�buk) �j m�ret�t sz�molja ki az ablak m�ret�hez
 * @author �kos
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
	 * �jram�retezi az �sszes alakzatot, azaz az �sszes mez�t �s �sszes tornyot.
	 */
	public void resize() {
		resizeAllFields();
		resizeAllTowers();
	}
	
	/**
	 * �jram�retezi az �sszes mez�t. Felteszi, hogy a mez�k n*n-es elrendez�sben vannak, n�gyzet alak�ak
	 * �s a lehet� legink�bb ki kell t�lteni az ablakot. Ha m�g a TablePainterben nincsenek mez�ket reprezent�l� alakzatok,
	 * felt�lti azokat (inicializ�l is kezdetben)
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
	 * �jram�retezi az �sszes tornyot (towers darabot). Felteszi hogy a a tornyok (nagyk�r, kisk�r) elrendez�sben vannak t�rolva
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
