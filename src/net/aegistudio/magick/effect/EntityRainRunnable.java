package net.aegistudio.magick.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;

public class EntityRainRunnable implements Runnable {
	
	EntityRain rain; Location location;
	public EntityRainRunnable(EntityRain entityRain, Location location) {
		this.rain = entityRain;
		this.location = location;
	}

	@Override
	public void run() {
		double range = rain.range;
		Location loc = location.add(range * (2 * Math.random() - 1), 
				rain.height, range * (2 * Math.random() - 1));
		
		for(int i = 0; i < rain.cluster; i ++) {
			loc = loc.add(2 * Math.random() - 1, 0, 2 * Math.random() - 1);
			Entity entity = loc.getWorld().spawnEntity(loc, rain.entity);
			if(entity instanceof Fireball) 
				((Fireball) entity).setDirection(new Vector(0, -10, 0));
			if(entity instanceof Explosive)
				((Explosive) entity).setYield((float) rain.explosionPower);
			entity.setVelocity(new Vector(0, -10, 0));
			if(rain.onFire) entity.setFireTicks(10000);
		}
	}
}
