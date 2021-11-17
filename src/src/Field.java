package src;

import java.util.ArrayList;
import java.util.HashMap;

public class Field {
	private Tower currTower;
	private HashMap<Direction, Field> forwardNeighbours;
	private Field backNeighbour;
	private ArrayList<Tower> sameColorTowers;
	
	private ArrayList<Field> getNeighbour(Direction d) {
		if(currTower != null || !forwardNeighbours.containsKey(d)) {
			return null;
		}
		else {
			ArrayList<Field> avaible = new ArrayList<Field>();
			avaible.add(this);
			avaible.addAll(forwardNeighbours.get(d).getNeighbour(d));
			return avaible;
		}
	}
	public ArrayList<Field> getNeighbours(DirType upOrDown) {
		ArrayList<Field> avaible = new ArrayList<Field>();
		for(Direction d: Direction.values()) {
			if(d.getType().equals(upOrDown)) {
				avaible.addAll(getNeighbour(d));				
			}
		}
		return avaible;
	}
	public void entered(Tower t) {
		if(t.equals(null))
			throw new NullPointerException("Null_Tower");
		else {
			currTower = t;
			for(Tower samecolor: sameColorTowers) {
				samecolor.activate();
			}
		}
	}
	public void left() {
		currTower = null;
	}
	
}
