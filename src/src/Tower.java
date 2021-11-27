package src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Tower implements Serializable{
	//A torony sz�ne
	private Color color;
	//Melyik mez�n �ll �ppen a torony
	private Field currField;
	//Azt mutatja melyik j�t�kos� a b�bu (merre mozog)
	private DirType side;
	private Game g;
	Tower(Color c, Field start, DirType which, Game game){
		color= c;
		currField = start;
		side = which;
		g = game;
	}
	public Field getCurrField() { return currField; }
	public Color getColor() { return color; }
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
	
	public void moveRandom() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		int maxValue = avaible.size();
		int random = (int)(Math.random()*maxValue);
		this.moveto(avaible.get(random));
	}
}
