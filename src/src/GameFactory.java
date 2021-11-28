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
		initAllTowers(setupFiles[3]);
		setFieldsSameColorTowers();
		setAIs(upAI, downAI);
		setWinningFields(upAI, downAI);
		setTablePainterTowers();
	}
	
	private void setFieldsNeighboursAndColors(String[] setupFileNames) throws IOException {
		ArrayList<String> frontSetupAllLines = FileToStringArrayList(setupFileNames[0]);
		ArrayList<String> backSetupAllLines = FileToStringArrayList(setupFileNames[1]);
		ArrayList<String> colorSetupAllLines = FileToStringArrayList(setupFileNames[2]);
		for(int i = 0; i < g.tablesize.getWidth()*g.tablesize.getHeight(); ++i) {
			String i_front_setup = frontSetupAllLines.get(i);
			String i_back_setup = backSetupAllLines.get(i);
			String i_color = colorSetupAllLines.get(i);
			LinkedList<Field> frontNeighbours = LineToFieldLinkedList(i_front_setup);
			LinkedList<Field> backNeighbours = LineToFieldLinkedList(i_back_setup);
			g.getField(i).setNeighbours(frontNeighbours, backNeighbours);
			g.getField(i).setColor(Color.decode(i_color));
		}
	}
	/**
	 * L�trehozza �s inicializ�lja a j�t�kban l�v� tornyokat (b�bukat) a megadott f�jl utas�t�sai szerint
	 * (mennyi torony, milyen sz�n�ek, melyik j�t�koshoz tartoznak, melyik mez�n kezdik a j�t�kot)
	 * @param setupFileName az inicializ�l�s f�jl neve
	 * @throws IOException ha nem tudja megnyitni a k�rt f�jlt
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
	 * Minden mez�nek be�ll�tja kik azok a tornyok, akiknek ugyanolyan a sz�ne, mint a mez�nek
	 */
	private void setFieldsSameColorTowers() {
		for(Field f: g.table) {
			for(Tower t: g.towers) {
				if(f.getColor().equals(t.getColor()))
					f.setSameColorTower(t);
			}
		}
	}
	
	private void setAIs(boolean up, boolean down) {
		if(up) {
			g.ai.put(DirType.UP, new AI(g));
		}
		if(down) {
			g.ai.put(DirType.DOWN, new AI(g));
		}
	}
	
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
	
	private void setTablePainterTowers() {
		TablePainter p = g.painter;
		LinkedList<Ellipse2D> gameTowers = p.towers;
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
	 * Megnyit egy adott nev� f�jlt �s visszaadja a sorait egy ArrayList-ben
	 * @param setupFileName a f�jl neve
	 * @return Egy ArrayList<String> amiben soronk�nt szerepel a f�jl tartalma
	 * @throws IOException ha nem tudja megnyitni a k�rt f�jlt
	 */
	private ArrayList<String> FileToStringArrayList(String setupFileName) throws IOException{
		File setupFile = new File(setupFileName);
		ArrayList<String> setupAllLines = new ArrayList<String>();
		setupAllLines.addAll(Files.readAllLines(setupFile.toPath(), Charset.forName("UTF-8")));
		return setupAllLines;
	}
	/**
	 * Egy stringet sz�tszed a :-ok ment�n �s eme pici stringek �ltal indexelt mez�ket tartalmaz� LinkedListtel t�r vissza
	 * A 
	 * @param line
	 * @return
	 */
	private LinkedList<Field> LineToFieldLinkedList(String line) {
		LinkedList<Field> neighbours = new LinkedList<Field>();
		String[] splitLine = line.split(":");
		neighbours.addAll(StringArrToFieldLL(splitLine));
		return neighbours;
	}
	
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
