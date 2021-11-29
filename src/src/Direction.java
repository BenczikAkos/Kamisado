package src;

/**
 * A t�bla ir�nyait �sszefoglal� enum, mindegyik ir�nyhoz tartozik egy "j�t�kosazonos�t�",
 * hogy melyik j�t�kos mozgathatja arra a szomsz�dra a b�buj�t 
 * @author �kos
 *
 */
public enum Direction { 
	upleft(DirType.UP),
	upmiddle(DirType.UP),
	upright(DirType.UP),
	left(null),
	right(null),
	downleft(DirType.DOWN),
	downmiddle(DirType.DOWN),
	downright(DirType.DOWN);
	private DirType type;
	private Direction(DirType d) {
		type = d;
	}
	public DirType getType() { return type; }
}