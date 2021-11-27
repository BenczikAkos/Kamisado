package src;

import java.util.ArrayList;

public class AI {
	Game game;
	ArrayList<Field> winningFields;
	public void makeMove() {
		try {
			this.wait(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
				return;
			}
		}
		actTower.moveRandom();
	}

}
