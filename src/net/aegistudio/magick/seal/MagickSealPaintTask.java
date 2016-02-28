package net.aegistudio.magick.seal;

public class MagickSealPaintTask implements Runnable {
	
	private final MagickSealEffect seal;
	private int ticks;
	private final Painter painter;
	public MagickSealPaintTask(MagickSealEffect seal, Painter painter) {
		this.seal = seal;
		this.ticks = seal.totalPaintTick;
		this.painter = painter;
	}
	
	int task = -1;
	public void start() {
		if(task == -1) {
			task = seal.element.getServer().getScheduler()
					.scheduleSyncRepeatingTask(seal.element, this, 0, seal.repaintTick);
		}
	}
	
	@Override
	public void run() {
		if(ticks > 0) {
			this.seal.generator.generate(painter);
			this.painter.end();
			ticks --;
		}
		else {
			seal.element.getServer().getScheduler().cancelTask(task);
			task = -1;
		}
	}
}
