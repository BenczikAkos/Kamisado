package src;

import java.util.ArrayList;

public class Tower {
	//Melyik mezõn áll éppen a torony
	private Field currField;
	//Azt mutatja melyik játékosé a bábu
	private DirType side;
	public void activate() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		
	}
	public void moveto(Field into) {
		currField.setTower(null);
		currField = into;
		into.entered(this);
	}
}
