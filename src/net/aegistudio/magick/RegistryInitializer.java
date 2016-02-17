package net.aegistudio.magick;

import org.bukkit.entity.EntityType;

import net.aegistudio.magick.effect.EntityRain;
import net.aegistudio.magick.effect.FeatherFall;
import net.aegistudio.magick.element.ElementDefinition;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellRegistry;

public class RegistryInitializer implements Initializer<SpellRegistry> {
	@Override
	public void initial(MagickElement element, SpellRegistry registry) {
		{
			SpellEntry meteorite = new SpellEntry(element);
			EntityRain meteoriteRain = new EntityRain();
			meteorite.effect = meteoriteRain;
			meteoriteRain.cluster = 4;
			meteoriteRain.tier = 8;
			meteoriteRain.delay = 20;
			meteoriteRain.entity = EntityType.FIREBALL;
			
			meteorite.spellPrice = new ElementDefinition();
			meteorite.spellPrice.setElementPoint("fire", 20);
			meteorite.spellPrice.setElementPoint("divine", 10);
			meteorite.spellPrice.setElementPoint("evil", 10);
			
			meteorite.handlerInfo = 30;
			registry.spellRegistries.put("meteorite", meteorite);
		}
		
		{
			SpellEntry featherFall = new SpellEntry(element);
			FeatherFall featherFalling = new FeatherFall();
			featherFall.effect = featherFalling;
			
			featherFall.spellPrice = new ElementDefinition();
			featherFall.spellPrice.setElementPoint("wind", 80);
			
			featherFall.handlerInfo = 20;
			registry.spellRegistries.put("featherFall", featherFall);
		}
	}
}
