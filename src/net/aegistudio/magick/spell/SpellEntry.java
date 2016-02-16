package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.element.ElementDefinition;

public class SpellEntry {
	public SpellEffect effect;
	public ElementDefinition spellPrice = new ElementDefinition();
	public Object handlerInfo;
	
	private final MagickElement magickElement;
	
	public SpellEntry(MagickElement magickElement) {
		this.magickElement = magickElement;
	}
	
	public static final String EFFECT_CLASS = "effectClass";
	public static final String EFFECT_CONFIG = "effectConfig";
	
	public static final String ELEMENT_REQUIRED = "elementRequired";
	
	@SuppressWarnings("unchecked")
	public void load(MagickElement element, ConfigurationSection configuration) throws Exception {
		// Load spell class.
		String effectClazz = configuration.getString(EFFECT_CLASS);
		if(effectClazz == null) return;
		effect = ((Class<? extends SpellEffect>)Class
				.forName(effectClazz)).newInstance();
		
		// Load spell effect config.
		if(!configuration.contains(EFFECT_CONFIG))
			configuration.createSection(EFFECT_CONFIG);
		ConfigurationSection effectConfig = configuration
				.getConfigurationSection(EFFECT_CONFIG);
		effect.load(element, effectConfig);
		
		// Load spell price config.
		if(!configuration.contains(ELEMENT_REQUIRED))
			configuration.createSection(ELEMENT_REQUIRED);
		ConfigurationSection elementConfig = configuration
				.getConfigurationSection(ELEMENT_REQUIRED);
		spellPrice.load(elementConfig);
		
		// Load spell handle config.
		magickElement.handler.loadSpell(this, configuration);
	}
	
	public void save(MagickElement element, ConfigurationSection configuration) throws Exception {
		// Save spell class.
		configuration.set(EFFECT_CLASS, effect.getClass().getName());
		
		// Save spell effect config.
		if(!configuration.contains(EFFECT_CONFIG))
			configuration.createSection(EFFECT_CONFIG);
		ConfigurationSection effectConfig = configuration.getConfigurationSection(EFFECT_CONFIG);
		effect.save(element, effectConfig);
		
		// Save spell price config.
		if(!configuration.contains(ELEMENT_REQUIRED))
			configuration.createSection(ELEMENT_REQUIRED);
		ConfigurationSection elementConfig = configuration
				.getConfigurationSection(ELEMENT_REQUIRED);
		spellPrice.save(elementConfig);
		
		// Save spell handle config.
		magickElement.handler.saveSpell(this, configuration);
	}
	
	public void makeMagick(MagickElement element, Player player, Location location, ItemStack bookUsing, String spellContent) {
		this.effect.spell(element, player, location, spellContent.split(" "));
	}
}
