package src;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Mesters�ges intelligenci�t, g�pi ellenfelet megval�s�t� oszt�ly. V�letlenszer�en l�p, de nyertes l�p�st nem hagy ki.
 * @author �kos
 *
 */
public class AI implements Serializable{
	private static final long serialVersionUID = 1L;
	Game game;
	//Azok a mez�k, amikre ha b�but l�ptet, nyer
	ArrayList<Field> winningFields;
	AI(Game game) {  
		this.game = game;
		winningFields = new ArrayList<Field>();
	}
	
	/**
	 * L�p egyet szab�lyosan. Ha nincs b�bu kiv�lasztva (j�t�k els� l�p�se), egy el�re meghat�rozottal l�p.
	 * Ha tud �gy l�pni, hogy megnyerje a j�t�kot, megteszi. Egy�bk�nt v�letlen, szab�lyos mez�re l�p.
	 * @return K�t int-tel t�r vissza: [0]: melyik mez�re l�pett; [1]: melyik toronnyal l�pett
	 * (A mez�k, tornyok a j�t�k logik�ja szerint vannak sz�mozva)
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
