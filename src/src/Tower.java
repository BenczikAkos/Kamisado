package src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Tower implements Serializable{
	//A torony színe
	private Color color;
	//Melyik mezõn áll éppen a torony
	private Field currField;
	//Azt mutatja melyik játékosé a bábu (merre mozog)
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
	 * Aktiválja magát; elkéri és megmondja a játékvezetõnek hova tud lépni, beállítja magát az aktuálisan aktív toronynak
	 * @return
	 * 		Egy ArrayList azokkal a mezõkkel ahova lépni tud a torony
	 */
	public ArrayList<Field> activate() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		g.setActiveTower(this);
		g.newAvaibles(avaible);	
		return avaible;
	}
	/**
	 * A paraméterben megadott mezõre lép, ha szabályosan rá tud lépni
	 * @param into
	 * 			A mezõ, ahova léptetni akarjuk
	 * @return
	 * 			true, ha szabályosan oda tud lépni, false ha nem
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
