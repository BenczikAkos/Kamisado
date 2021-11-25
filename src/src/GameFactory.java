package src;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;

public class GameFactory {
	Game g;
	GameFactory(Game g) { this.g = g; }
	public void setupGame(String[] setupFiles) throws IOException {
		for(int i = 0; i < g.tablesize.getWidth()*g.tablesize.getHeight(); ++i) {
			g.table.add(new Field(g));
		}
		setFieldsNeighboursAndColors(setupFiles);
		initAllTowers(setupFiles[3]);
		setFieldsSameColorTowers();
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
			g.table.get(i).setNeighbours(frontNeighbours, backNeighbours);
			g.table.get(i).setColor(Color.decode(i_color));
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
			Field startField = g.table.get(startFieldNo);
			DirType player = split[2].equals("UP") ? DirType.UP : DirType.DOWN;
			Tower newTower = new Tower(c, startField, player);
			g.towers.add(newTower);
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
				LL.add(g.table.get(whichOther));
			}
		}
		return LL;
	}
}
