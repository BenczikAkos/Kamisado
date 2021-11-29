package src;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Field implements Serializable{
	//A mez� sz�ne
	private Color color;
	//A jelenleg mez�n tart�zkod� torony
	private Tower currTower;
	//Sima l�p�s eset�re t�rolja a szomsz�dokat, ir�ny-mez� m�don
	private HashMap<Direction, Field> frontNeighbours;
	//A mez� sz�n�vel azonos sz�n� tornyokat t�rolja (alapesetben 2-t)
	private ArrayList<Tower> sameColorTowers;
	//Melyik j�t�kos b�buja nyer ha r�l�p
	private DirType winningSide = null;
	//Melyik j�t�kban szerepel az adott emz� objektum
	private Game currGame;
	Field(Game g){
		frontNeighbours = new HashMap<Direction, Field>();
		sameColorTowers = new ArrayList<Tower>();
		currGame = g;
	}
	/**
	 * Setter a Field currTower nev� v�ltoz�j�hoz
	 * 
	 * @param what
	 *		Tower t�pus� v�ltoz�, amire be kell �ll�tani az attrib�tum �rt�k�t
	 */
	public void setTower(Tower what) { currTower = what; }
	public Tower getCurrTower() { return currTower; }
	public void setSameColorTower(Tower what) { sameColorTowers.add(what); }
	public void setColor(Color c) { color = c; }
	public Color getColor() { return this.color; }
	public void setWinningSide(DirType who) { winningSide = who; }
	
	/**
	 * Rekurz�van elk�ri egy adott ir�nyba a szomsz�djait, meg�ll ha b�bu van a mez�n vagy p�lya sz�le
	 * @param d
	 * 			Melyik ir�nyba k�ri el a szomsz�djait
	 * @return
	 * 			Egy ArrayList amiben az adott ir�nyba tal�lhat� �sszes szomsz�dja van
	 */
	protected ArrayList<Field> getNeighbour(Direction d) {
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
	 * Az �sszes, a megadott j�t�kos �ltal el�rhet� mez�t adja vissza
	 * @param upOrDown
	 * 			Melyik j�t�kosnak kellenek a szomsz�djai
	 * @return
	 * 			Egy ArrayList, benne az �sszes el�rhet� mez�vel
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
	 * Egy mez�t inicializ�l a sima l�p�shez haszn�latos szomsz�daival �s a tol�s eset�n fontos szomsz�daival.
	 * A szomsz�dokat t�rol� adatszerkezeteket �gy t�lti fel,
	 * hogy csak olyan ir�nyok legyenek benne amerre t�nyleg szomsz�dja.
	 * 
	 * @param frontNeighbours
	 * 			A rendes l�p�sekhez haszn�latos szomsz�dait tartalmazza bizonyos sorrendben. Amerre nincs szomsz�dja oda null ker�l
	 * 			A szok�sos n�gyzetr�cs eset�n �gy kell a szomsz�dok sorrendj�t �rteni: (a mez�, akinek a szomsz�dait n�zz�k: #)
	 * 			0	1	2
	 * 			3	#	4
	 * 			5	6	7
	 * @param backNeighbours
	 * 			Tol�shoz haszn�latos szomsz�dokat t�rol a DirType szerinti sorrendben (most UP, DOWN)
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
	 * Egy torony r�l�pett a mez�re, megn�zi hogy ezzel valaki nyert-e, ha nem, l�pteti a j�t�kot, be�ll�tja
	 * a rajta �ll� b�but a param�terben kapottra �s aktiv�lja a vele egyez� sz�n� soron k�vetkez� b�but
	 * @param t
	 * 			A torony, ami r�l�pett a mez�re
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
