package src;

import java.util.ArrayList;

public class Tower {
	//Melyik mez�n �ll �ppen a torony
	private Field currField;
	//Azt mutatja melyik j�t�kos� a b�bu
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
