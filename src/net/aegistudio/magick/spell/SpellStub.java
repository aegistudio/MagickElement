package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;

public class SpellStub implements SpellEffect {

	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		sender.sendMessage("Magick stub!");
	}

	@Override
	public void load(ConfigurationSection spellConfig) {
		
	}

	@Override
	public void save(ConfigurationSection spellConfig) {
		
	}
	
}
