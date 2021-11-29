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
	 * Visszaadja kinek a köre van épp
	 * @return
	 * 		Az aktív játékos, akinek a köre van (amerre õ mozgatja a bábukat)
	 */
	public DirType getWhoseTurn() { return whoseTurn; }
	
	/**
	 * Getter a játéktér mezõihez
	 * @param i
	 * 		Hányadik mezõt szeretnénk elkérni (0-63, jobbról balra, fentrõl lefele számozva)
	 * @return
	 * 		A kért Field objektum
	 */
	public Field getField(int i) { return table.get(i); }
	/**
	 * Getter a játékban lévõ tornyokhoz
	 * @param i
	 * 		Hányadik tornyot szeretnénk elkérni (0-15 kezdõállás szerint, balról jobbra, felsõ sor/alsó sor szerint számozva) 
	 * @return
	 * 		A kért Tower objektum
	 */
	public Tower getTower(int i) { return towers.get(i); }
	/**
	 * A paraméterben adott Field indexét adja vissza a játék adatszerkezetében
	 * @param f
	 * 		A Field objektum, aminek az indexére kíváncsiak vagyunk
	 * @return
	 * 		A keresett index
	 */
	public int fieldIndex(Field f) { return table.indexOf(f); }
	/**
	 * A paraméterben adott Tower indexét adja vissza a játék adatszerkezetében
	 * @param t
	 * 		A Tower, aminek az indexét keressük
	 * @return
	 * 		A keresett index
	 */
	public int towerIndex(Tower t) { return towers.indexOf(t); }
	/**
	 * Hozzáad egy új Field-et a játék mezõket tároló adatszerkezetének végéhez
	 * @param f
	 * 		A Field, amit hozzá akarunk adni
	 */
	public void addField(Field f) { table.add(f); }
	/**
	 * Hozzáad egy új Tower objektumot a játék tornyokat tároló adatszerkezetének végéhez
	 * @param t
	 * 		A Tower, amit hozzá kívánunk adni
	 */
	public void addTower(Tower t) { towers.add(t); }
	/**
	 * Beállítja a játék épp aktív tornyát
	 * @param t
	 * 		Az új aktív torony
	 */
	public void setActiveTower(Tower t) { activeTower = t; }
	/**
	 * Getter a játék épp aktív tornyához
	 * @return
	 * 		A játék épp aktív tornya
	 */
	public Tower getActiveTower() { return activeTower; }
	/**
	 * Egy kör elteltét megvalósító függvény, megcseréli kinek a köre van
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
	 * Inicializálja a játékot a megadott inicializációs fájlok szerint
	 * @param setups Azon fájlok nevei amibõl kiolvassa az inicializáláshoz szükséges adatokat
	 * (mezõknek kik a szomszédai, milyen színûek, milyen tornyok vannak és hol kezdik a játékot...)
	 * @throws IOException ha az egyik fájlt nem tudta megnyitni
	 */
	public void initGame(String[] setups, boolean up_ai, boolean down_ai) throws IOException {
		GameFactory gf = new GameFactory(this);
		gf.setupGame(setups, up_ai, down_ai);
	}
	/**
	 * Frissíti a játékhoz tartozó painterben a kiemelendõ mezõket
	 * Kezeli ha beragadnak a tornyok, és ha ezzel esetleg valaki megnyeri a játékot
	 * @param fields
	 * 		Az új mezõk, amik elérhetõek az aktív toronynak és amiket ki kell emelni
	 * 		Ha értéke null, az azt jeleti, hogy a torony be van szorulva
	 */
	public void newAvaibles(ArrayList<Field> fields) {
		if(fields.isEmpty()) {
			if(stuck == true) {
				turnPassed();
				win(whoseTurn);
				return;
			}
			stuck = true;
			activeTower.getCurrField().entered(activeTower); //"Helyben lép egyet a beszorult bábu"
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
	 * A játék végét, valakinek a gyõzelmét megvalósító függvény
	 * @param who
	 * 		A játékos (oldal), aki nyert
	 */
	public void win(DirType who) {
		painter.fieldHighlight = new  ArrayList<Integer>();
		painter.win(who);
	}
}
