package src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Field {
	//A mez� sz�ne
	private Color color;
	//A jelenleg mez�n tart�zkod� torony
	private Tower currTower;
	//Sima l�p�s eset�re t�rolja a szomsz�dokat, ir�ny-mez� m�don
	private HashMap<Direction, Field> frontNeighbours;
	//Tol�s eset�re t�rolja a szomsz�dokat, kv�zi tol�j�t�kos-mez� m�don (alapesetben az el�re- �s a h�traszomsz�d)
	private HashMap<DirType, Field> backNeighbours;
	//A mez� sz�n�vel azonos sz�n� tornyokat t�rolja (alapesetben 2-t)
	private ArrayList<Tower> sameColorTowers;
	//Melyik j�t�kban szerepel az adott emz� objektum
	private Game currGame;
	Field(Game g){
		frontNeighbours = new HashMap<Direction, Field>();
		backNeighbours = new HashMap<DirType, Field>();
		sameColorTowers = new ArrayList<Tower>();
		currGame = g;
	}
	/*
	 * Setter a Field currTower nev� v�ltoz�j�hoz
	 * 
	 * @param what
	 *		Tower t�pus� v�ltoz�, amire be kell �ll�tani az attrib�tum �rt�k�t
	 */
	public void setTower(Tower what) { currTower = what; }
	public void setColor(Color c) { color = c; }
	public Color getColor() { return this.color; }
	public Tower getCurrTower() { return currTower; }
	
	private ArrayList<Field> getNeighbour(Direction d) {
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
	/*
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
	public void setNeighbours(LinkedList<Field> frontNeighbours, LinkedList<Field> backNeighbours) {
		
		for(Direction d: Direction.values()) {
			if(frontNeighbours.peekFirst() != null) {
				Field neighbour = frontNeighbours.getFirst();
				this.frontNeighbours.put(d, neighbour);
			}
			frontNeighbours.removeFirst();
		}
		
		for(DirType dt: DirType.values()) {
			if(backNeighbours.peekFirst() != null) {
				this.backNeighbours.put(dt,  backNeighbours.getFirst());
			}
			backNeighbours.removeFirst();
		}
	}
	public void entered(Tower t) {
		if(t.equals(null))
			throw new NullPointerException("Null_Tower lepett mezore");
		else {
			currTower = t;
			for(Tower samecolor: sameColorTowers) {
				samecolor.activate();
			}
		}
	}
}
