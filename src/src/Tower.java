package src;

import java.awt.Color;
import java.util.ArrayList;

public class Tower {
	private Color color;
	//Melyik mezõn áll éppen a torony
	private Field currField;
	//Azt mutatja melyik játékosé a bábu (merre mozog)
	private DirType side;
	Tower(Color c, Field start, DirType which){
		color= c;
		currField = start;
		side = which;
	}
	public Field getCurrField() { return currField; }
	public Color getColor() { return color; }
	public DirType getDirType() { return side; }
	public ArrayList<Field> activate() {
		return currField.getNeighbours(side);
		
	}
	public void moveto(Field into) {
		currField.setTower(null);
		currField = into;
		into.entered(this);
	}
}
