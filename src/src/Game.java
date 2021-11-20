package src;

//import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
import java.util.LinkedList;

public class Game {
	ArrayList<Field> table;
	ArrayList<Tower> towers;
	int round;
	int tablesize;
	
	public Game(int size){
		table = new ArrayList<Field>();
		towers = new ArrayList<Tower>();
		round = 0;
		tablesize = size;
	}
	
	public void initGame(String frontSetupFileName, String backSetupFileName, String towerSetupFileName) throws IOException {
		//létrehozunk annyi mezõt amennyi kell
		for(int i = 0; i < tablesize; ++i) {
			table.add(new Field(this));
		}
		initAllFields(frontSetupFileName, backSetupFileName);
		initAllTowers(towerSetupFileName);
	}
	
	private void initAllFields(String frontSetupFileName, String backSetupFileName) throws IOException {
		ArrayList<String> frontSetupAllLines = FileToStringArrayList(frontSetupFileName);
		ArrayList<String> backSetupAllLines = FileToStringArrayList(backSetupFileName);
		for(int i = 0; i < tablesize; ++i) {
			String i_front_setup = frontSetupAllLines.get(i);
			String i_back_setup = backSetupAllLines.get(i);
			LinkedList<Field> frontNeighbours = LineToFieldLinkedList(i_front_setup);
			LinkedList<Field> backNeighbours = LineToFieldLinkedList(i_back_setup);
			if(frontNeighbours.size() != 8) {
				System.out.printf("Ennyiedik nem jo: %d\nTartalma ", i);
				System.out.println(Arrays.toString(i_front_setup.split(":")));
			}
			table.get(i).setNeighbours(frontNeighbours, backNeighbours);
		}
	}
	
	private ArrayList<String> FileToStringArrayList(String setupFileName) throws IOException{
		File setupFile = new File(setupFileName);
		ArrayList<String> setupAllLines = new ArrayList<String>();
		setupAllLines.addAll(Files.readAllLines(setupFile.toPath(), Charset.forName("UTF-8")));
		if(setupAllLines.size() != tablesize) {
			throw new IOException("Nem megfelelo szamu sorbol all " + setupFileName + " elvart:" + tablesize + " aktual:" + setupAllLines.size());
		}
		return setupAllLines;
	}
	
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
				LL.add(table.get(whichOther));
			}
		}
		return LL;
	}

	private void initAllTowers(String setupFileName) {
		
	}
}
