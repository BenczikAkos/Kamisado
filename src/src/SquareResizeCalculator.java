package src;

import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class SquareResizeCalculator extends ResizeCalculator implements Serializable{
	private int rows, towers;
	public SquareResizeCalculator(TablePainter painter, int size, int towers) { 
		super(painter); 
		rows = size;
		this.towers = towers;
	}
	
	public void resize() {
		resizeAllFields();
		resizeAllTowers();
	}
	
	private void resizeAllFields() {
		LinkedList<Rectangle2D> gameFields = p.fields;
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
	
	private void resizeAllTowers() {
		LinkedList<Ellipse2D> gameTowers = p.towers;
		Dimension d = p.getSize();
		double square = Math.min(d.height, d.width);
        double lilsquare = square/rows;
        for(int i = 0; i < towers; ++i) {
			int idx = p.game.table.indexOf((p.game.towers.get(i)).getCurrField());
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
