package src;

import java.util.ArrayList;

public class Tower {
	private Field currField;
	private DirType side;
	public void activate() {
		ArrayList<Field> avaible = currField.getNeighbours(side);
		
	}
}
