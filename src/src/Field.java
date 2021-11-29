package src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A játéktábla mezõit megvalósító osztály
 * @author Ákos
 *
 */ 
@SuppressWarnings("serial")
public class Field implements Serializable{
	/**
	 * A mezõ színe
	 */
	private Color color;
	/**
	 * A jelenleg mezõn tartózkodó torony
	 */
	private Tower currTower;
	/**
	 * Sima lépés esetére tárolja a szomszédokat, irány-mezõ módon
	 */
	private HashMap<Direction, Field> frontNeighbours;
	/**
	 * A mezõ színével azonos színû tornyokat tárolja (alapesetben 2-t)
	 */
	private ArrayList<Tower> sameColorTowers;
	/**
	 * Melyik játékos nyer ha rálép erre a mezõre? Defaultból null, a mezõk többségére rálépve nem nyer senki
	 */
	private DirType winningSide = null;
	/**
	 * Melyik játékban szerepel az adott emzõ objektum
	 */
	private Game currGame;
	public Field(Game g){
		frontNeighbours = new HashMap<Direction, Field>();
		sameColorTowers = new ArrayList<Tower>();
		currGame = g;
	}
	/**
	 * Setter a Field currTower nevû változójához
	 * 
	 * @param what
	 *		Tower típusú változó, amire be kell állítani az attribútum értékét
	 */
	public void setTower(Tower what) { currTower = what; }
	/**
	 * A mezõn aktuálisan álló tornyot adja vissza
	 * @return
	 * 		A mezõn álló torony objektum referenciája, ill. null ha a mezõ üres
	 */
	public Tower getCurrTower() { return currTower; }
	/**
	 * A mezõ tárolja a vele egyszínû tornyokat, ezzel új tornyot lehet a meglévõkhöz hozzáadni
	 * @param what
	 * 		Egy torony, aminek a színe megegyezik a mezõével
	 */
	public void setSameColorTower(Tower what) { sameColorTowers.add(what); }
	/**
	 * Beállítja a mezõ színét
	 * @param c
	 * 		Milyen színû legyen a mezõ
	 */
	public void setColor(Color c) { color = c; }
	/**
	 * Visszaadja a mezõ színét
	 * @return
	 * 		A mezõ színe
	 */
	public Color getColor() { return this.color; }
	/**
	 * Beállítja, hogy az adott mezõ melyik játékosnak "célmezeje", azaz, ha az adott játékos rálép egy
	 * tornyával, megyneri a játékot.
	 * @param who
	 * 		Ki nyeri meg a játékot ha rálép erre a mezõre
	 */
	public void setWinningSide(DirType who) { winningSide = who; }
	
	/**
	 * Rekurzívan elkéri egy adott irányba a szomszédjait, megáll ha bábu van a mezõn vagy pálya széle
	 * @param d
	 * 			Melyik irányba kéri el a szomszédjait
	 * @return
	 * 			Egy ArrayList amiben az adott irányba található összes szomszédja van
	 */
	public ArrayList<Field> getNeighbour(Direction d) {
		if(currTower != null) {
			return null;
		}
		else {
			ArrayList<Field> avaible = new ArrayList<Field>();
			avaible.add(this);
			if (frontNeighbours.get(d) != null) {
				ArrayList<Field> neighbours = frontNeighbours.get(d).getNeighbour(d);
				if(neighbours!=null)
					avaible.addAll(frontNeighbours.get(d).getNeighbour(d));
			}
			return avaible;
		}
	}
	/**
	 * Az összes, a megadott játékos által elérhetõ mezõt adja vissza
	 * @param upOrDown
	 * 			Melyik játékosnak kellenek a szomszédjai
	 * @return
	 * 			Egy ArrayList, benne az összes elérhetõ mezõvel
	 */
	public ArrayList<Field> getNeighbours(DirType upOrDown) {
		ArrayList<Field> avaible = new ArrayList<Field>();
		for(Direction d: frontNeighbours.keySet()) {
			if(d.getType()!=null && d.getType().equals(upOrDown)) {
				ArrayList<Field> d_neighbours = this.frontNeighbours.get(d).getNeighbour(d);
				if(d_neighbours!=null)
					avaible.addAll(d_neighbours);				
			}
		}
		return avaible;
	}
	/**
	 * Egy mezõt inicializál a sima lépéshez használatos szomszédaival és a tolás esetén fontos szomszédaival.
	 * A szomszédokat tároló adatszerkezeteket úgy tölti fel,
	 * hogy csak olyan irányok legyenek benne amerre tényleg szomszédja.
	 * 
	 * @param frontNeighbours
	 * 			A rendes lépésekhez használatos szomszédait tartalmazza bizonyos sorrendben. Amerre nincs szomszédja oda null kerül
	 * 			A szokásos négyzetrács esetén így kell a szomszédok sorrendjét érteni: (a mezõ, akinek a szomszédait nézzük: #)
	 * 			0	1	2
	 * 			3	#	4
	 * 			5	6	7
	 * 
	 */
	public void setNeighbours(LinkedList<Field> frontNeighbours) {
		
		for(Direction d: Direction.values()) {
			if(frontNeighbours.peekFirst() != null) {
				Field neighbour = frontNeighbours.getFirst();
				this.frontNeighbours.put(d, neighbour);
			}
			frontNeighbours.removeFirst();
		}
	}
	
	/**
	 * Egy torony rálépett a mezõre, megnézi hogy ezzel valaki nyert-e, ha nem, lépteti a játékot, beállítja
	 * a rajta álló bábut a paraméterben kapottra és aktiválja a vele egyezõ színû soron következõ bábut
	 * @param t
	 * 			A torony, ami rálépett a mezõre
	 */
	public void entered(Tower t) {
		if(winningSide != null && winningSide.equals(t.getDirType())) {
			currGame.win(winningSide);
			return;
		}
		currGame.turnPassed();
		currTower = t;
		for(Tower samecolor: sameColorTowers) {
			if(samecolor.activeRound())
				samecolor.activate();
		}

	}
}
