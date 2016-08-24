package net.aegistudio.magick.spell;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Module;
import net.aegistudio.magick.element.ElementExpression;

public class SpellEntry implements Module {
	public SpellEffect effect;
	public ElementExpression spellPrice = new ElementExpression();
	public Object handlerInfo;
	public String description;
	
	private final MagickElement magickElement;
	
	public SpellEntry(MagickElement magickElement) {
		this.magickElement = magickElement;
	}
	
	public static final String SPELL_DESCRIPTION = "description";
	public static final String EFFECT_CLASS = "effectClass";
	public static final String EFFECT_CONFIG = "effectConfig";
	
	public static final String ELEMENT_REQUIRED = "elementRequired";
	
	public void load(MagickElement element, ConfigurationSection configuration) throws Exception {
		// Load spell class.
		if(!configuration.contains(EFFECT_CLASS)) return;
		effect = element.loadInstance(SpellEffect.class, configuration, 
				EFFECT_CLASS, null, EFFECT_CONFIG, null);
		
		// Load spell price config.
		if(!configuration.contains(ELEMENT_REQUIRED))
			configuration.createSection(ELEMENT_REQUIRED);
		ConfigurationSection elementConfig = configuration
				.getConfigurationSection(ELEMENT_REQUIRED);
		spellPrice.load(element, elementConfig);
		
		// Load description.
		if(configuration.contains(SPELL_DESCRIPTION))
			description = configuration.getString(SPELL_DESCRIPTION);
		
		// Load spell handle config.
		magickElement.handler.loadSpell(this, configuration);
	}
	
	public void save(MagickElement element, ConfigurationSection configuration) throws Exception {
		// Save spell class.
		element.saveInstance(effect, configuration, EFFECT_CLASS, EFFECT_CONFIG);
		
		// Save spell price config.
		element.saveConfig(spellPrice, configuration, ELEMENT_REQUIRED);
		
		// Save description.
		configuration.set(SPELL_DESCRIPTION, description);
		
		// Save spell handle config.
		magickElement.handler.saveSpell(this, configuration);
	}
	
	public void makeMagick(MagickElement element, Player player, Location location, ItemStack bookUsing, String spellContent) {
		this.effect.spell(element, player, location, spellContent.split(" "));
	}

	@Override
	public void after(MagickElement element) {
		this.effect.after(element);
		this.spellPrice.after(element);
	}
}
