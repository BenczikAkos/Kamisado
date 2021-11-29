package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.Before;

import src.*;

public class FieldTest {
	
	Game game;
	Field field;
	Tower tower;
	
	@Before
	public void Init() {
		game = new Game(10, 10);
		field = new Field(game);
		tower = new Tower(Color.black, field, DirType.UP, game);
	}

	@Test
	public void Getter_Setter_test() {
		field.setTower(tower);
		field.setColor(Color.cyan);
		assertEquals("Rossz a currTower setter/getter", field.getCurrTower(), tower);
		assertEquals("Rossz a FieldColor setter/getter", field.getColor(), Color.cyan);
	}
	
	
	public void TowerEnterTest() {
		DirType oldTurn = game.getWhoseTurn();
		field.entered(tower);
		DirType newTurn = game.getWhoseTurn();
		assertEquals(tower, field.getCurrTower());
		assertNotEquals(oldTurn, newTurn);
	}
	
	public void SetNeighbourTest() {
		Field field1 = new Field(game);
		Field field2 = new Field(game);
		LinkedList<Field> neighbours = new LinkedList<Field>();
		neighbours.add(null);
		neighbours.add(field1);
		neighbours.add(field2);
		for(Direction d: Direction.values()) {
			assertNull(field.getNeighbour(d));
		}
		field.setNeighbours(neighbours);
		assertNull(field.getNeighbour(Direction.upleft));
		assertEquals(field1, field.getNeighbour(Direction.upmiddle));
		assertEquals(field2, field.getNeighbour(Direction.upright));
	}

}
