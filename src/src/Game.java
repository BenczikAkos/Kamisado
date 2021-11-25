package src;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
	ArrayList<Field> table;
	ArrayList<Tower> towers;
	TablePainter painter;
	int round;
	Dimension tablesize;
	
	public Game(int height, int width){
		table = new ArrayList<Field>();
		towers = new ArrayList<Tower>();
		round = 0;
		tablesize = new Dimension(height, width);
	}
	public void setPainter(TablePainter p) { painter = p; }
	/**
	 * Inicializálja a játékot a megadott inicializációs fájlok szerint
	 * @param setups Azon fájlok nevei amibõl kiolvassa az inicializáláshoz szükséges adatokat
	 * (mezõknek kik a szomszédai, milyen színûek, milyen tornyok vannak és hol kezdik a játékot...)
	 * @throws IOException ha az egyik fájlt nem tudta megnyitni
	 */
	public void initGame(String[] setups) throws IOException {
		GameFactory gf = new GameFactory(this);
		gf.setupGame(setups);
	}
	

}
