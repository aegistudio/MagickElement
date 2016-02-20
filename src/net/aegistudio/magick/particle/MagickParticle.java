package net.aegistudio.magick.particle;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;

public class MagickParticle {
	private final Sound sound;
	public MagickParticle(Sound sound) {
		this.sound = sound;
	}
	
	protected int tier() {
		return 1;
	}
	
	protected int max() {
		return 1;
	}
	
	public void play(Location location) {
		for(int i = 0; i < tier(); i ++) 
			location.getWorld().playEffect(location, Effect.FLYING_GLYPH, null);
		location.getWorld().playSound(location, sound, volume(), pitch());
	}
	
	public float volume() {
		return 0.4f + 0.6f * tier() / max();
	}
	
	public static final float[] TONAL_PITCH_LOOKUP = new float[] {1.f, 9.0f/8, 5.0f/4, 4.0f/3, 3.0f/2, 5.0f/3, 90f/48, 2.f};
	public float pitch() {
		return TONAL_PITCH_LOOKUP[Math.min(Math.round(8.0f * tier() / max()), 7)];
	}
}
