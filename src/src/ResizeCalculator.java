package src;

public abstract class ResizeCalculator {
	protected TablePainter p;
	ResizeCalculator(TablePainter painter) { p = painter; }
	protected abstract void resize();
}
