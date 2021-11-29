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
	HashMap<DirType, AI> ai;
	Dimension tablesize;
	boolean stuck = false;
	
	public Game(int height, int width){
		table = new ArrayList<Field>();
		towers = new ArrayList<Tower>();
		ai = new HashMap<DirType, AI>();
		tablesize = new Dimension(height, width);
	}
	/**
	 * Visszaadja kinek a k�re van �pp
	 * @return
	 * 		Az akt�v j�t�kos, akinek a k�re van (amerre � mozgatja a b�bukat)
	 */
	public DirType getWhoseTurn() { return whoseTurn; }
	
	/**
	 * Getter a j�t�kt�r mez�ihez
	 * @param i
	 * 		H�nyadik mez�t szeretn�nk elk�rni (0-63, jobbr�l balra, fentr�l lefele sz�mozva)
	 * @return
	 * 		A k�rt Field objektum
	 */
	public Field getField(int i) { return table.get(i); }
	/**
	 * Getter a j�t�kban l�v� tornyokhoz
	 * @param i
	 * 		H�nyadik tornyot szeretn�nk elk�rni (0-15 kezd��ll�s szerint, balr�l jobbra, fels� sor/als� sor szerint sz�mozva) 
	 * @return
	 * 		A k�rt Tower objektum
	 */
	public Tower getTower(int i) { return towers.get(i); }
	/**
	 * A param�terben adott Field index�t adja vissza a j�t�k adatszerkezet�ben
	 * @param f
	 * 		A Field objektum, aminek az index�re k�v�ncsiak vagyunk
	 * @return
	 * 		A keresett index
	 */
	public int fieldIndex(Field f) { return table.indexOf(f); }
	/**
	 * A param�terben adott Tower index�t adja vissza a j�t�k adatszerkezet�ben
	 * @param t
	 * 		A Tower, aminek az index�t keress�k
	 * @return
	 * 		A keresett index
	 */
	public int towerIndex(Tower t) { return towers.indexOf(t); }
	/**
	 * Hozz�ad egy �j Field-et a j�t�k mez�ket t�rol� adatszerkezet�nek v�g�hez
	 * @param f
	 * 		A Field, amit hozz� akarunk adni
	 */
	public void addField(Field f) { table.add(f); }
	/**
	 * Hozz�ad egy �j Tower objektumot a j�t�k tornyokat t�rol� adatszerkezet�nek v�g�hez
	 * @param t
	 * 		A Tower, amit hozz� k�v�nunk adni
	 */
	public void addTower(Tower t) { towers.add(t); }
	/**
	 * Be�ll�tja a j�t�k �pp akt�v torny�t
	 * @param t
	 * 		Az �j akt�v torony
	 */
	public void setActiveTower(Tower t) { activeTower = t; }
	/**
	 * Getter a j�t�k �pp akt�v torny�hoz
	 * @return
	 * 		A j�t�k �pp akt�v tornya
	 */
	public Tower getActiveTower() { return activeTower; }
	/**
	 * Egy k�r eltelt�t megval�s�t� f�ggv�ny, megcser�li kinek a k�re van
	 */
	public void turnPassed() {
		if(whoseTurn.equals(DirType.UP)) {
			whoseTurn = DirType.DOWN;			
		}
		else {
			whoseTurn = DirType.UP;			
		}		
	}
	public void setPainter(TablePainter p) { painter = p; }
	/**
	 * Inicializ�lja a j�t�kot a megadott inicializ�ci�s f�jlok szerint
	 * @param setups Azon f�jlok nevei amib�l kiolvassa az inicializ�l�shoz sz�ks�ges adatokat
	 * (mez�knek kik a szomsz�dai, milyen sz�n�ek, milyen tornyok vannak �s hol kezdik a j�t�kot...)
	 * @throws IOException ha az egyik f�jlt nem tudta megnyitni
	 */
	public void initGame(String[] setups, boolean up_ai, boolean down_ai) throws IOException {
		GameFactory gf = new GameFactory(this);
		gf.setupGame(setups, up_ai, down_ai);
	}
	/**
	 * Friss�ti a j�t�khoz tartoz� painterben a kiemelend� mez�ket
	 * Kezeli ha beragadnak a tornyok, �s ha ezzel esetleg valaki megnyeri a j�t�kot
	 * @param fields
	 * 		Az �j mez�k, amik el�rhet�ek az akt�v toronynak �s amiket ki kell emelni
	 * 		Ha �rt�ke null, az azt jeleti, hogy a torony be van szorulva
	 */
	public void newAvaibles(ArrayList<Field> fields) {
		if(fields.isEmpty()) {
			if(stuck == true) {
				turnPassed();
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
	/**
	 * A j�t�k v�g�t, valakinek a gy�zelm�t megval�s�t� f�ggv�ny
	 * @param who
	 * 		A j�t�kos (oldal), aki nyert
	 */
	public void win(DirType who) {
		painter.fieldHighlight = new  ArrayList<Integer>();
		painter.win(who);
	}
}
