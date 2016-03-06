package net.aegistudio.magick.compat;

import org.bukkit.Sound;

import net.aegistudio.magick.MagickElement;

public enum CompatibleSound {
	LEVEL_UP(new String[][]{
		{"1.8", "LEVEL_UP"},
		{"1.9", "ENTITY_PLAYER_LEVELUP"}
	}),
	ORB_PICKUP(new String[][] {
		{"1.8", "ORB_PICKUP"},
		{"1.9", "ENTITY_EXPERIENCE_ORB_PICKUP"}
	}),
	FIZZ(new String[][] {
		{"1.8", "FIZZ"},
		{"1.9", "ENTITY_CREEPER_PRIMED"}
	}),
	NOTE_BASS(new String[][] {
		{"1.8", "NOTE_BASS"},
		{"1.9", "BLOCK_NOTE_BASS"}
	}),
	ENDERDRAGON_WING(new String[][] {
		{"1.8", "ENDERDRAGON_WINGS"},
		{"1.9", "ENTITY_ENDERDRAGON_FLAP"}
	});
	
	private final String[][] mapping;
	
	private CompatibleSound(String mapping[][]) {
		this.mapping = mapping;
	}
	
	public Sound get(MagickElement element) {
		String version = element.getServer().getVersion();
		for(int i = 0; i < this.mapping.length - 1; i ++) {
			String[] entry = this.mapping[i];
			String[] next = this.mapping[i + 1];
			if(entry[0].compareTo(version) < 0 &&
					next[0].compareTo(version) > 0) {
				return Sound.valueOf(entry[1]);
			}
		}
		
		if(this.mapping.length - 1 > 0) 
			return Sound.valueOf(this.mapping[this.mapping.length - 1][1]);
		return null;
	}
}
