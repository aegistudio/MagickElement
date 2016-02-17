package net.aegistudio.magick;

import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import net.aegistudio.magick.effect.EntityRain;
import net.aegistudio.magick.effect.FeatherFall;
import net.aegistudio.magick.effect.PotionResistence;
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
			meteorite.description = "Cause meteorite from outside the world to drop.";
			meteoriteRain.cluster = 1;
			meteoriteRain.tier = 5;
			meteoriteRain.delay = 20;
			meteoriteRain.entity = EntityType.FIREBALL;
			
			meteorite.spellPrice = new ElementDefinition();
			meteorite.spellPrice.setElementPoint("fire", 40);
			meteorite.spellPrice.setElementPoint("divine", 10);
			meteorite.spellPrice.setElementPoint("evil", 20);
			
			meteorite.handlerInfo = 30;
			registry.spellRegistries.put("meteorite", meteorite);
		}
		
		{
			SpellEntry featherFall = new SpellEntry(element);
			FeatherFall featherFalling = new FeatherFall();
			featherFall.effect = featherFalling;
			featherFall.description = "Slow down your dropping rate to a feather.";
			
			featherFall.spellPrice = new ElementDefinition();
			featherFall.spellPrice.setElementPoint("wind", 80);
			
			featherFall.handlerInfo = 20;
			registry.spellRegistries.put("featherFall", featherFall);
		}
		
		{
			SpellEntry antidote = new SpellEntry(element);
			PotionResistence antidoteSpell 
				= new PotionResistence(PotionEffectType.POISON);
			antidote.effect = antidoteSpell;
			
			antidoteSpell.buffName = "Antidote";
			antidote.description = "An antidote that defeating all harmful proteins.";
			antidote.spellPrice = new ElementDefinition();
			antidote.spellPrice.setElementPoint("terra", 20);
			antidote.spellPrice.setElementPoint("water", 10);
			
			antidote.handlerInfo = 5;
			registry.spellRegistries.put("antidote", antidote);
		}
		
		{
			SpellEntry vitality = new SpellEntry(element);
			PotionResistence vitalitySpell 
				= new PotionResistence(PotionEffectType.WITHER);
			vitality.effect = vitalitySpell;
			
			vitalitySpell.buffName = "Vitality";
			vitality.description = "Bring you force of living against withering.";
			vitality.spellPrice = new ElementDefinition();
			vitality.spellPrice.setElementPoint("terra", 40);
			
			vitality.handlerInfo = 5;
			registry.spellRegistries.put("vitality", vitality);
		}
		
		{
			SpellEntry determined = new SpellEntry(element);
			PotionResistence determineSpell 
				= new PotionResistence(PotionEffectType.HARM);
			determined.effect = determineSpell;
			
			determineSpell.buffName = "Determined";
			determineSpell.duration = 200;
			
			determined.description = "You have a determined heart to hurt others.";
			determined.spellPrice = new ElementDefinition();
			determined.spellPrice.setElementPoint("divine", 5);
			determined.spellPrice.setElementPoint("evil", 5);
			determined.spellPrice.setElementPoint("fire", 5);
			determined.spellPrice.setElementPoint("electric", 5);
			determined.spellPrice.setElementPoint("ice", 5);
			determined.spellPrice.setElementPoint("water", 5);
			determined.spellPrice.setElementPoint("wind", 5);
			determined.spellPrice.setElementPoint("terra", 5);
			
			determined.handlerInfo = 15;
			registry.spellRegistries.put("determined", determined);
		}
	}
}
