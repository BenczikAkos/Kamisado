package src;

import java.util.ArrayList;

public class AI {
	Game game;
	ArrayList<Field> winningFields;
	AI(Game game) { 
		this.game = game;
		winningFields = new ArrayList<Field>();
	}
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
