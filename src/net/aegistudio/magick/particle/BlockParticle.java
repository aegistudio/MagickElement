package net.aegistudio.magick.particle;

import org.bukkit.Effect;
import org.bukkit.block.Block;

public class BlockParticle {
	private final Effect effectType;
	private final int tier;
	
	public BlockParticle(Effect effectType, int tier) {
		this.effectType = effectType;
		this.tier = tier;
	}
	
	public void show(Block block) {
		for(int i = 0; i < tier(); i ++) {
			
			// TOP
			block.getWorld().playEffect(block.getLocation()
					.add(1.2 * Math.random(), 1.2, 1.2 * Math.random()),
					effectType, data());
			
			// X
			block.getWorld().playEffect(block.getLocation()
					.add(1.2, Math.random(), Math.random()),
					effectType, data());
			block.getWorld().playEffect(block.getLocation()
					.add(-.2, Math.random(), Math.random()),
					effectType, data());
			
			// Y
			block.getWorld().playEffect(block.getLocation()
					.add(Math.random(), Math.random(), 1.2),
					effectType, data());
			block.getWorld().playEffect(block.getLocation()
					.add(Math.random(), Math.random(), -.2),
					effectType, data());
		}
	}
	
	public int tier() {
		return tier;
	}
	
	public Object data() {
		return null;
	}
}
