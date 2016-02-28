package net.aegistudio.magick.seal;

public class FilteredPainter implements Painter {
	protected final Painter next;
	protected int scanInterval;
	protected int tick;
	protected int counter;
	
	public FilteredPainter(Painter next, int scanInterval) {
		this.next = next;
		this.scanInterval = scanInterval;
		this.tick = 0;
		this.counter = 0;
	}
	
	@Override
	public void paint(double x, double y, double z) {
		if(counter % scanInterval == tick)
			next.paint(x, y, z);
		counter ++;
	}

	@Override
	public void end() {
		tick = (tick + 1) % scanInterval;
		counter = 0;
		this.next.end();
	}
	
}
