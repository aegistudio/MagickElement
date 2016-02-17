package net.aegistudio.magick.particle;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class PlayerParticle {
	private final Effect effectType;
	public PlayerParticle(Effect effectType) {
		this(effectType, 1);
	}
	
	private final int tier;
	public PlayerParticle(Effect effectType, int tier) {
		this.effectType = effectType;
		this.tier = tier;
	}
	
	public void show(Player player) {
		for(int i = 0; i < tier(); i ++)
			player.getWorld().playEffect(player.getLocation().add(Math.random() - 0.5, 
					2 * Math.random(), Math.random() - 0.5), effectType, data());
	}
	
	protected int tier() {
		return tier;
	}
	
	public Object data() {
		return null;
	}
}
