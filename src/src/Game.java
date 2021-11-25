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
	 * Inicializ�lja a j�t�kot a megadott inicializ�ci�s f�jlok szerint
	 * @param setups Azon f�jlok nevei amib�l kiolvassa az inicializ�l�shoz sz�ks�ges adatokat
	 * (mez�knek kik a szomsz�dai, milyen sz�n�ek, milyen tornyok vannak �s hol kezdik a j�t�kot...)
	 * @throws IOException ha az egyik f�jlt nem tudta megnyitni
	 */
	public void initGame(String[] setups) throws IOException {
		GameFactory gf = new GameFactory(this);
		gf.setupGame(setups);
	}
	

}
