package src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Field {
	//A mezõ színe
	private Color color;
	//A jelenleg mezõn tartózkodó torony
	private Tower currTower;
	//Sima lépés esetére tárolja a szomszédokat, irány-mezõ módon
	private HashMap<Direction, Field> frontNeighbours;
	//Tolás esetére tárolja a szomszédokat, kvázi tolójátékos-mezõ módon (alapesetben az elõre- és a hátraszomszéd)
	private HashMap<DirType, Field> backNeighbours;
	//A mezõ színével azonos színû tornyokat tárolja (alapesetben 2-t)
	private ArrayList<Tower> sameColorTowers;
	//Melyik játékban szerepel az adott emzõ objektum
	private Game currGame;
	Field(Game g){
		frontNeighbours = new HashMap<Direction, Field>();
		backNeighbours = new HashMap<DirType, Field>();
		sameColorTowers = new ArrayList<Tower>();
		currGame = g;
	}
	/*
	 * Setter a Field currTower nevû változójához
	 * 
	 * @param what
	 *		Tower típusú változó, amire be kell állítani az attribútum értékét
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
	 * @param backNeighbours
	 * 			Toláshoz használatos szomszédokat tárol a DirType szerinti sorrendben (most UP, DOWN)
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
