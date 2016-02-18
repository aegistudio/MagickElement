package net.aegistudio.magick.effect;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

@Deprecated
public class AngelWing implements SpellEffect, Listener {
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		sender.setAllowFlight(true);
		sender.setFlying(true);
		element.getServer().getScheduler().runTaskLater(element, new Runnable() {
			@Override
			public void run() {
				sender.setAllowFlight(false);
				sender.setFlying(false);
			}
		}, 100);
	}
	
	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		
	}

	@Override
	public void after(MagickElement element) {
		element.getServer().getPluginManager().registerEvents(this, element);
	}
}
