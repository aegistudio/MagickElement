package net.aegistudio.magick.cauldron;

import org.bukkit.Server;
import org.bukkit.block.Block;

import net.aegistudio.magick.MagickElement;

public abstract class CauldronTickHandler implements Runnable{
	private int tick;
	private final long period;
	protected final Server server;
	protected final MagickElement element;
	protected final Block cauldron;
	public CauldronTickHandler(MagickElement element, Block cauldron, long period, int tick) {
		this.tick = tick;
		this.period = period;
		this.server = element.getServer();
		this.element = element;
		this.cauldron = cauldron;
		
		this.server.getScheduler().runTaskLater(element, this, period);
	}
	
	@Override
	public void run() {
		if(tick > 0) {
			this.doTick();
			this.server.getScheduler().runTaskLater(element, this, period);
			tick --;
		}
		else this.endTick();
	}
	
	protected abstract void doTick();
	
	protected abstract void endTick();
}
