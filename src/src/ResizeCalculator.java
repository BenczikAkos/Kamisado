package src;

import java.io.Serializable;

public abstract class ResizeCalculator implements Serializable{
	private static final long serialVersionUID = 1L;
	protected TablePainter p;
	ResizeCalculator(TablePainter painter) { p = painter; }
	protected abstract void resize();
}
