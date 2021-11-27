package src;

import java.awt.Dimension;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<Field> table;
	ArrayList<Tower> towers;
	private Tower activeTower = null;
	private DirType whoseTurn = DirType.UP;
	TablePainter painter;
	private HashMap<DirType, AI> ai;
	Dimension tablesize;
	boolean stuck = false;
	
	public Game(int height, int width){
		table = new ArrayList<Field>();
		towers = new ArrayList<Tower>();
		tablesize = new Dimension(height, width);
	}
	public DirType getWhoseTurn() { return whoseTurn; }
	
	public Field getField(int i) { return table.get(i); }
	public Tower getTower(int i) { return towers.get(i); }
	public int fieldIndex(Field f) { return table.indexOf(f); }
	public int towerIndex(Tower t) { return towers.indexOf(t); }
	public void addField(Field f) { table.add(f); }
	public void addTower(Tower t) { towers.add(t); }
	
	public void setActiveTower(Tower t) { activeTower = t; }
	public Tower getActiveTower() { return activeTower; }
	public void turnPassed() {
		if(whoseTurn.equals(DirType.UP)) {
			whoseTurn = DirType.DOWN;			
		}
		else {
			whoseTurn = DirType.UP;			
		}
		if(ai != null && ai.containsKey(whoseTurn)) {
			ai.get(whoseTurn).makeMove();
		}
		
	}
	public void setPainter(TablePainter p) { painter = p; }
	/**
	 * Inicializ�lja a j�t�kot a megadott inicializ�ci�s f�jlok szerint
	 * @param setups Azon f�jlok nevei amib�l kiolvassa az inicializ�l�shoz sz�ks�ges adatokat
	 * (mez�knek kik a szomsz�dai, milyen sz�n�ek, milyen tornyok vannak �s hol kezdik a j�t�kot...)
	 * @throws IOException ha az egyik f�jlt nem tudta megnyitni
	 */
	public void initGame(String[] setups) throws IOException {
		GameFactory gf = new GameFactory(this);
		gf.setupGame(setups);
	}
	
	public void newAvaibles(ArrayList<Field> fields) {
		if(fields.isEmpty()) {
			if(stuck == true) {
				System.out.println("double stuck");
				win(whoseTurn);
				return;
			}
			stuck = true;
			activeTower.getCurrField().entered(activeTower); //"Helyben l�p egyet a beszorult b�bu"
		}
		else {			
			ArrayList<Integer> fieldNums = new ArrayList<Integer>();
			for(Field f: fields) {
				fieldNums.add(table.indexOf(f));
			}
			painter.fieldHighlight = fieldNums;
		}
	}

	public void win(DirType who) {
		painter.fieldHighlight = new  ArrayList<Integer>();
	}
}
