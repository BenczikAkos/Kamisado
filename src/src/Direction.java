package src;

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