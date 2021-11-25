package src;

import java.awt.Color;
import java.util.ArrayList;

public class Tower {
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
	public ArrayList<Field> activate() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		g.setActiveTower(this);
		g.newAvaibles(avaible);	
		return avaible;
	}
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
}
