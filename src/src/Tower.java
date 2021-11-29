package src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
/**
 * A játékban lévõ tornyokat (bábukat) megvalósító osztály
 * @author Ákos
 *
 */
public class Tower implements Serializable{
	/**
	 * A torony színe
	 */
	private Color color;
	/**
	 * Melyik mezõn áll éppen a torony
	 */
	private Field currField;
	/**
	 * Azt mutatja melyik játékosé a bábu (merre mozog)
	 */
	private DirType side;
	/**
	 * Melyik játékban "szerepel" a szóban forgó torony
	 */
	private Game g;
	/**
	 * Létrehoz egy megfelelõ tulajdonságokkal rendelkezõ tornyot
	 * @param c
	 * 		A torony színe
	 * @param start
	 * 		A torny kezdõmezeje
	 * @param which
	 * 		A tornyot irányító játékos azonosítója
	 * @param game
	 * 		A játék amiben szerepel a torony
	 */
	public Tower(Color c, Field start, DirType which, Game game){
		color= c;
		currField = start;
		side = which;
		g = game;
	}
	/**
	 * Melyik mezõn áll épp a torony?
	 * @return
	 * 		A mezõ, amin a torny épp áll
	 */
	public Field getCurrField() { return currField; }
	/**
	 * Visszaadja a torny színét
	 * @return
	 * 		A torony színe
	 */
	public Color getColor() { return color; }
	/**
	 * Melyik játékoshoz tartozik az adott torony
	 * @return
	 * 		A tornyot irányító játékos azonosítója
	 */
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
	/**
	 * Egy véletlenszerû, elérhetõ mezõre lép 
	 * @return
	 * 		Két int-tel tér vissza egy tömbben;
	 * 		az elsõ hogy hányas számú mezõre lépett a játék tárolási logikája szerint
	 * 		a második hogy õ maga hányas számú torony a játék tárolási logikája szerint
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
