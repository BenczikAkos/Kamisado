package src;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Mesterséges intelligenciát, gépi ellenfelet megvalósító osztály. Véletlenszerûen lép, de nyertes lépést nem hagy ki.
 * @author Ákos
 *
 */
public class AI implements Serializable{
	private static final long serialVersionUID = 1L;
	Game game;
	//Azok a mezõk, amikre ha bábut léptet, nyer
	ArrayList<Field> winningFields;
	AI(Game game) {  
		this.game = game;
		winningFields = new ArrayList<Field>();
	}
	
	/**
	 * Lép egyet szabályosan. Ha nincs bábu kiválasztva (játék elsõ lépése), egy elõre meghatározottal lép.
	 * Ha tud úgy lépni, hogy megnyerje a játékot, megteszi. Egyébként véletlen, szabályos mezõre lép.
	 * @return Két int-tel tér vissza: [0]: melyik mezõre lépett; [1]: melyik toronnyal lépett
	 * (A mezõk, tornyok a játék logikája szerint vannak számozva)
	 */
	public int[] makeMove() {
		Tower actTower = game.getActiveTower();
		if(actTower == null) {
			actTower = game.getTower(12);
			game.setActiveTower(actTower);
		}
		for(Field winf: winningFields) {
			if(actTower.moveto(winf)) {
				int field_i = game.fieldIndex(winf);
				int tower_i = game.towerIndex(actTower);
				game.painter.towerMoved(tower_i, field_i);
				int[] coords = new int[2];
				coords[0] = field_i;
				coords[1] = tower_i;
				return coords;
			}
		}
		return actTower.moveRandom();
	}

}
