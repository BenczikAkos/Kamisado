package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;

import src.*;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
	
	Game game;
	Field f1, f2;
	Tower t1, t2;
	

	@Before
	public void setUp() throws Exception {
		game = new Game(8, 8);
		f1 = new Field(game); f2 = new Field(game);
		t1 = new Tower(Color.black, f1, DirType.UP, game); t2 = new Tower(Color.blue, f2, DirType.DOWN, game);
	}

	@Test
	public void TowerManipTest() {
		assertEquals(0, game.getTowersCount());
		game.addTower(t1);
		assertEquals(1, game.getTowersCount());
		game.addTower(t2);
		assertEquals(1, game.towerIndex(t2));
		assertEquals(t1, game.getTower(0));
	}
	
	@Test
	public void FieldManipTest() {
		assertEquals(0, game.getFieldsCount());
		game.addField(f1);
		assertEquals(f1, game.getField(0));
		game.addField(f2);
		assertEquals(1, game.fieldIndex(f2));
		assertEquals(2, game.getFieldsCount());
	}
	
	@Test(expected=IOException.class) 
	public void WrongInitTest() throws IOException {
		String[] wrongSetups = {"fffront_init.txt", "ccolor_init.txt", "ttower_init.txt"};
		game.initGame(wrongSetups, false, false);
	}
	
	@Test
	public void CorrectInitTest() throws IOException {
		String[] correctSetups = {"front_init.txt", "color_init.txt", "tower_init.txt"};
		TablePainter painter = new TablePainter(game);
		game.setPainter(painter);
		game.initGame(correctSetups, false, false);
		assertEquals(64, game.getFieldsCount());
		assertEquals(16, game.getTowersCount());
	}
}
