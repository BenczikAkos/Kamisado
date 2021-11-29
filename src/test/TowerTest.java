package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import src.*;

public class TowerTest {
	Game game;
	Field f1;
	Tower tow;
	
	@Before
	public void setUp() throws Exception {
		game = new Game(2,2);
		f1 = new Field(game);
		tow = new Tower(Color.white, f1, DirType.UP, game);
	}

	@Test
	public void GettersTest() {
		assertEquals(tow.getCurrField(), f1);
		assertEquals(tow.getColor(), Color.white);
		assertEquals(tow.getDirType(), DirType.UP);
		assertTrue(tow.activeRound());
	}
	
	public void ActivateTest() {
		LinkedList<Field> test = new LinkedList<Field>();
		assertArrayEquals(test.toArray(), tow.activate().toArray());
		Field neighbour = new Field(game);
		test.add(neighbour);
		f1.setNeighbours(test);
		assertArrayEquals(test.toArray(), tow.activate().toArray());	
	}
	
	public void MoveToTest() {
		LinkedList<Field> test = new LinkedList<Field>();
		Field neighbour = new Field(game);
		assertFalse(tow.moveto(neighbour));
		test.add(neighbour);
		f1.setNeighbours(test);
		assertTrue(tow.moveto(neighbour));
	}

}
