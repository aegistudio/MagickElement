package net.aegistudio.magick.spell;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.magick.MagickElement;

public interface SpellHandler {
	public void handleSpell(Player player, ItemStack magickBook);
	
	public void loadConfig(MagickElement magickElement, ConfigurationSection configuration);
	
	public void loadSpell(SpellEntry entry, ConfigurationSection configuration);
	
	public void saveSpell(SpellEntry entry, ConfigurationSection configuration);
}
