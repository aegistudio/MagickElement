package net.aegistudio.magick.effect;

import org.bukkit.Location;

import net.aegistudio.magick.MagickElement;

public class RangedSpawnRunnable implements Runnable {
	
	MagickElement element;
	RangedSpawn spawn; Location location;
	int tiersRemained; String[] arguments;
	public RangedSpawnRunnable(MagickElement element, RangedSpawn entityRain, 
			Location location, String[] arguments) {
		this.element = element;
		this.spawn = entityRain;
		this.location = location;
		this.tiersRemained = spawn.tier;
		this.arguments = arguments;
	}

	@Override
	public void run() {
		for(int i = 0; i < spawn.cluster; i ++) {
			double angle = Math.PI * 2 * Math.random();
			double cos = Math.cos(angle); double sin = Math.sin(angle);
			double dif = (spawn.range - spawn.minRange) * Math.random();
			
			Location loc = location.clone().add((spawn.minRange + dif) * cos, 
					0, (spawn.minRange + dif) * sin);
			spawn.entity.spawn(loc, arguments);
		}
		this.next();
	}
	
	protected void next() {
		if(tiersRemained > 0)
			element.getServer().getScheduler().runTaskLater(element, 
					this, (long)(Math.random() * spawn.delay));
		tiersRemained --;
	}
	
	public void start() {
		if(tiersRemained > 0) {
			element.getServer().getScheduler()
				.runTaskLater(element, this, spawn.lag + (long)(Math.random() * spawn.delay));
			tiersRemained --;
		}
	}
}
