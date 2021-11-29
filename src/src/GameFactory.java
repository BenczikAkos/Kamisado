package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;

public class GameFactory {
	Game g;
	GameFactory(Game g) { this.g = g; }
	
	public void setupGame(String[] setupFiles, boolean upAI, boolean downAI) throws IOException {
		for(int i = 0; i < g.tablesize.getWidth()*g.tablesize.getHeight(); ++i) {
			g.addField(new Field(g));
		}
		setFieldsNeighboursAndColors(setupFiles);
		initAllTowers(setupFiles[2]);
		setFieldsSameColorTowers();
		setAIs(upAI, downAI);
		setWinningFields(upAI, downAI);
		g.painter.setResizeCalc();
		g.painter.resizeShapes();
	}
	
	/**
	 * A megadott fájlokból inicializálja a mezõk szomszédjait és a mezõk színeit.
	 * @param setupFileNames
	 * 			A fájlok amik tartalmazzák az inicializáláshoz szükséges adatokat
	 * @throws IOException
	 * 			Ha a megadott fájlok nem léteznek/nem elérhetõek
	 */
	private void setFieldsNeighboursAndColors(String[] setupFileNames) throws IOException {
		ArrayList<String> frontSetupAllLines = FileToStringArrayList(setupFileNames[0]);
		ArrayList<String> colorSetupAllLines = FileToStringArrayList(setupFileNames[1]);
		for(int i = 0; i < g.tablesize.getWidth()*g.tablesize.getHeight(); ++i) {
			String i_front_setup = frontSetupAllLines.get(i);
			String i_color = colorSetupAllLines.get(i);
			LinkedList<Field> frontNeighbours = LineToFieldLinkedList(i_front_setup);
			g.getField(i).setNeighbours(frontNeighbours);
			g.getField(i).setColor(Color.decode(i_color));
		}
	}
	/**
	 * Létrehozza és inicializálja a játékban lévõ tornyokat (bábukat) a megadott fájl utasításai szerint
	 * (mennyi torony, milyen színûek, melyik játékoshoz tartoznak, melyik mezõn kezdik a játékot)
	 * @param setupFileName az inicializálós fájl neve
	 * @throws IOException ha nem tudja megnyitni a kért fájlt
	 */
	private void initAllTowers(String setupFileName) throws IOException {
		ArrayList<String> setupAllLines = FileToStringArrayList(setupFileName);
		for(int i = 0; i < setupAllLines.size(); ++i) {
			String[] split = setupAllLines.get(i).split(":");
			Color c = Color.decode(split[0]);
			int startFieldNo = Integer.parseInt(split[1]);
			Field startField = g.getField(startFieldNo);
			DirType player = split[2].equals("UP") ? DirType.UP : DirType.DOWN;
			Tower newTower = new Tower(c, startField, player, g);
			g.addTower(newTower);
			startField.setTower(newTower);
		}
	}
	/**
	 * Minden mezõnek beállítja kik azok a tornyok, akiknek ugyanolyan a színe, mint a mezõnek
	 */
	private void setFieldsSameColorTowers() {
		for(Field f: g.table) {
			for(Tower t: g.towers) {
				if(f.getColor().equals(t.getColor()))
					f.setSameColorTower(t);
			}
		}
	}
	
	/**
	 * Létrehozza és beállítja a játékban résztvevõ gépi ellenfeleket igény szerint 0-t, 1-t vagy 2-t
	 * @param up
	 * 		Kell-e a fehérrel játszó gépi ellenfél? true/false
	 * @param down
	 * 		Kell-e a feketével játszó gépi ellenfél? true/false
	 */
	private void setAIs(boolean up, boolean down) {
		if(up) {
			g.ai.put(DirType.UP, new AI(g));
		}
		if(down) {
			g.ai.put(DirType.DOWN, new AI(g));
		}
	}
	
	/**
	 * Beállítja a megfelelõ mezõket, hogy melyik játékosoknak õk a célmezõi. Ezzel párhuzamosan beállítja a megfelelõ színnel 
	 * játszó gépi ellenfélnek is, hogy ezek azok a mezõk, amelyekre lépve nyer	
	 * @param up
	 * 		Van-e fehérrel játszó gépi ellenfél? true/false
	 * @param down
	 * 		Van-e feketével játszó gépi ellenfél? true/false
	 */
	private void setWinningFields(boolean up, boolean down) {
		for(int i = 0; i < 8; ++i) {
			Field currField = g.getField(i);
			currField.setWinningSide(DirType.UP);
			if(up) {
				AI upAI = g.ai.get(DirType.UP);
				upAI.winningFields.add(currField);
			}
		}
		for(int i = 56; i < 64; ++i) {
			Field currField = g.getField(i);
			currField.setWinningSide(DirType.DOWN);
			if(down) {
				AI downAI = g.ai.get(DirType.DOWN);
				downAI.winningFields.add(currField);
			}
		}
	}
	
	/**
	 * Feltölti a TablePainter tornyait tartalmazó adatszerkezetét a megfelelõ kezdõalakzatokkal
	 * (nagykör, kiskör) párokban tárolja a tornyokat
	 */
	private void setTablePainterTowers() {
		TablePainter p = g.painter;
		LinkedList<Ellipse2D.Double> gameTowers = p.towers;
		Dimension d = p.getSize();
		double square = Math.min(d.height, d.width);
        double lilsquare = square/8;
        for(int i = 0; i < 16; ++i) {
        	Tower currTower = p.game.getTower(i);
        	Field currField = currTower.getCurrField();
			int idx = p.game.fieldIndex(currField);
        	double bigx = (idx%8)*lilsquare; double bigy = idx/8*lilsquare;
        	double rad = lilsquare / 2;
    		double smallx = bigx+lilsquare/2-rad/2; double smally = bigy+lilsquare/2-rad/2;
			Ellipse2D.Double bigtower = new Ellipse2D.Double(bigx, bigy, lilsquare, lilsquare);
			Ellipse2D.Double smalltower = new Ellipse2D.Double(smallx, smally, rad, rad);
			gameTowers.add(bigtower);
			gameTowers.add(smalltower);
        }
	}
	
	/**
	 * Megnyit egy adott nevû fájlt és visszaadja a sorait egy ArrayList-ben
	 * @param setupFileName a fájl neve
	 * @return Egy ArrayList<String> amiben soronként szerepel a fájl tartalma
	 * @throws IOException ha nem tudja megnyitni a kért fájlt
	 */
	private ArrayList<String> FileToStringArrayList(String setupFileName) throws IOException{
		File setupFile = new File(setupFileName);
		ArrayList<String> setupAllLines = new ArrayList<String>();
		setupAllLines.addAll(Files.readAllLines(setupFile.toPath(), Charset.forName("UTF-8")));
		return setupAllLines;
	}
	/**
	 * Egy stringet szétszed a :-ok mentén és eme pici stringek által indexelt mezõket tartalmazó LinkedListtel tér vissza
	 * @param line
	 * 		String, ami :-kal elválasztott számokat tartalmaz
	 * @return
	 * 		A Field setNeighbours() metódusának megfelelõ LinkedList-tel tér vissza
	 */
	private LinkedList<Field> LineToFieldLinkedList(String line) {
		LinkedList<Field> neighbours = new LinkedList<Field>();
		String[] splitLine = line.split(":");
		neighbours.addAll(StringArrToFieldLL(splitLine));
		return neighbours;
	}
	
	/**
	 * Egy StrinArrayban tárolt számokat alakít a mezõk szomszédjait beállító LinkedList-té
	 * @param arr
	 * 		String tömb, amiben "számok" találhatóak
	 * @return
	 * 		 Field setNeighbours() metódusának megfelelõ LinkedList-tel tér vissza
	 */
	private LinkedList<Field> StringArrToFieldLL(String[] arr){
		LinkedList<Field> LL = new LinkedList<Field>();
		for(int i = 0; i < arr.length; ++i) {
			if(arr[i].equals(" "))
				LL.add(null);
			else {
				int whichOther = Integer.parseInt(arr[i]);
				LL.add(g.getField(whichOther));
			}
		}
		return LL;
	}
}
