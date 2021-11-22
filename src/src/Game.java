package src;

import java.awt.Color;
//import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
//import java.util.Arrays;
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
	
	public void initGame(String frontSetupFileName, String backSetupFileName, String colorSetupFileName, String towerSetupFileName) throws IOException {
		//létrehozunk annyi mezõt amennyi kell
		for(int i = 0; i < tablesize; ++i) {
			table.add(new Field(this));
		}
		initAllFields(frontSetupFileName, backSetupFileName, colorSetupFileName);
		initAllTowers(towerSetupFileName);
	}
	
	private void initAllFields(String frontSetupFileName, String backSetupFileName, String colorSetupFileName) throws IOException {
		ArrayList<String> frontSetupAllLines = FileToStringArrayList(frontSetupFileName);
		ArrayList<String> backSetupAllLines = FileToStringArrayList(backSetupFileName);
		ArrayList<String> colorSetupAllLines = FileToStringArrayList(colorSetupFileName);
		for(int i = 0; i < tablesize; ++i) {
			String i_front_setup = frontSetupAllLines.get(i);
			String i_back_setup = backSetupAllLines.get(i);
			String i_color = colorSetupAllLines.get(i);
			LinkedList<Field> frontNeighbours = LineToFieldLinkedList(i_front_setup);
			LinkedList<Field> backNeighbours = LineToFieldLinkedList(i_back_setup);
			table.get(i).setNeighbours(frontNeighbours, backNeighbours);
			table.get(i).setColor(Color.decode(i_color));
		}
	}
	
	private ArrayList<String> FileToStringArrayList(String setupFileName) throws IOException{
		File setupFile = new File(setupFileName);
		ArrayList<String> setupAllLines = new ArrayList<String>();
		setupAllLines.addAll(Files.readAllLines(setupFile.toPath(), Charset.forName("UTF-8")));
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

	private void initAllTowers(String setupFileName) throws IOException {
		ArrayList<String> setupAllLines = FileToStringArrayList(setupFileName);
		for(int i = 0; i < setupAllLines.size(); ++i) {
			String[] split = setupAllLines.get(i).split(":");
			Color c = Color.decode(split[0]);
			int startFieldNo = Integer.parseInt(split[1]);
			Field startField = table.get(startFieldNo);
			DirType player = split[2].equals("UP") ? DirType.UP : DirType.DOWN;
			Tower newTower = new Tower(c, startField, player);
			towers.add(newTower);
			startField.setTower(newTower);
		}
	}
}
