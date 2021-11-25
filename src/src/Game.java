package src;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
	ArrayList<Field> table;
	ArrayList<Tower> towers;
	Tower activeTower = null;
	DirType whoseTurn = DirType.UP;
	TablePainter painter;
	Dimension tablesize;
	
	public Game(int height, int width){
		table = new ArrayList<Field>();
		towers = new ArrayList<Tower>();
		tablesize = new Dimension(height, width);
	}
	public DirType getWhoseTurn() { return whoseTurn; }
	public void setActiveTower(Tower t) { activeTower = t; }
	public Tower getActiveTower() { return activeTower; }
	public void turnPassed() {
		if(whoseTurn.equals(DirType.UP))
			whoseTurn = DirType.DOWN;
		else
			whoseTurn = DirType.UP;
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
			System.out.println("Szorulas");
		}
		ArrayList<Integer> fieldNums = new ArrayList<Integer>();
		for(Field f: fields) {
			fieldNums.add(table.indexOf(f));
		}
		painter.fieldHighlight = fieldNums;
	}

}
