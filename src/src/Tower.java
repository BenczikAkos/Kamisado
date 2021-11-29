package src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
/**
 * A j�t�kban l�v� tornyokat (b�bukat) megval�s�t� oszt�ly
 * @author �kos
 *
 */
public class Tower implements Serializable{
	/**
	 * A torony sz�ne
	 */
	private Color color;
	/**
	 * Melyik mez�n �ll �ppen a torony
	 */
	private Field currField;
	/**
	 * Azt mutatja melyik j�t�kos� a b�bu (merre mozog)
	 */
	private DirType side;
	/**
	 * Melyik j�t�kban "szerepel" a sz�ban forg� torony
	 */
	private Game g;
	/**
	 * L�trehoz egy megfelel� tulajdons�gokkal rendelkez� tornyot
	 * @param c
	 * 		A torony sz�ne
	 * @param start
	 * 		A torny kezd�mezeje
	 * @param which
	 * 		A tornyot ir�ny�t� j�t�kos azonos�t�ja
	 * @param game
	 * 		A j�t�k amiben szerepel a torony
	 */
	public Tower(Color c, Field start, DirType which, Game game){
		color= c;
		currField = start;
		side = which;
		g = game;
	}
	/**
	 * Melyik mez�n �ll �pp a torony?
	 * @return
	 * 		A mez�, amin a torny �pp �ll
	 */
	public Field getCurrField() { return currField; }
	/**
	 * Visszaadja a torny sz�n�t
	 * @return
	 * 		A torony sz�ne
	 */
	public Color getColor() { return color; }
	/**
	 * Melyik j�t�koshoz tartozik az adott torony
	 * @return
	 * 		A tornyot ir�ny�t� j�t�kos azonos�t�ja
	 */
	public DirType getDirType() { return side; }
	public boolean activeRound() {
		return side.equals(g.getWhoseTurn());
	}
	/**
	 * Aktiv�lja mag�t; elk�ri �s megmondja a j�t�kvezet�nek hova tud l�pni, be�ll�tja mag�t az aktu�lisan akt�v toronynak
	 * @return
	 * 		Egy ArrayList azokkal a mez�kkel ahova l�pni tud a torony
	 */
	public ArrayList<Field> activate() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		g.setActiveTower(this);
		g.newAvaibles(avaible);	
		return avaible;
	}
	/**
	 * A param�terben megadott mez�re l�p, ha szab�lyosan r� tud l�pni
	 * @param into
	 * 			A mez�, ahova l�ptetni akarjuk
	 * @return
	 * 			true, ha szab�lyosan oda tud l�pni, false ha nem
	 */
	public boolean moveto(Field into) {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		if(avaible.contains(into)) {
			currField.setTower(null);
			currField = into;
			into.entered(this);		
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Egy v�letlenszer�, el�rhet� mez�re l�p 
	 * @return
	 * 		K�t int-tel t�r vissza egy t�mbben;
	 * 		az els� hogy h�nyas sz�m� mez�re l�pett a j�t�k t�rol�si logik�ja szerint
	 * 		a m�sodik hogy � maga h�nyas sz�m� torony a j�t�k t�rol�si logik�ja szerint
	 */
	public int[] moveRandom() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		g.painter.repaint();
		g.newAvaibles(avaible);
		int maxValue = avaible.size();
		int random = (int)(Math.random()*maxValue);
		Field dest = avaible.get(random);
		this.moveto(dest);
		int[] coords = new int[2];
		coords[0] = g.fieldIndex(dest);
		coords[1] = g.towerIndex(this);
		return coords;
	}
}
