package net.aegistudio.magick;

import org.bukkit.potion.PotionEffectType;
import net.aegistudio.magick.effect.RangedSpawn;
import net.aegistudio.magick.effect.FeatherFall;
import net.aegistudio.magick.effect.PotionResistence;
import net.aegistudio.magick.effect.VampireHand;
import net.aegistudio.magick.element.ElementDefinition;
import net.aegistudio.magick.entity.Lightning;
import net.aegistudio.magick.entity.NearestGround;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellRegistry;

public class RegistryInitializer implements Initializer<SpellRegistry> {
	@Override
	public void initial(MagickElement element, SpellRegistry registry) {
		{
			SpellEntry lightning = new SpellEntry(element);
			RangedSpawn lightningRound = new RangedSpawn();
			lightning.effect = lightningRound;
			lightning.description = "Ziz iz an epic dizcharge of electric power from zky, zzz...";
			lightningRound.cluster = 3;
			lightningRound.tier = 5;
			lightningRound.delay = 40;
			lightningRound.entity = new NearestGround(new Lightning());
			
			lightning.spellPrice = new ElementDefinition();
			lightning.spellPrice.setElementPoint("electric", 30);
			lightning.spellPrice.setElementPoint("wind", 30);
			lightning.spellPrice.setElementPoint("divine", 10);
			lightning.spellPrice.setElementPoint("evil", 20);
			
			lightning.handlerInfo = 50;
			registry.spellRegistries.put("lightning", lightning);
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
		
		{
			SpellEntry vampire = new SpellEntry(element);
			VampireHand vampireSpell = new VampireHand();
			vampire.effect = vampireSpell;
			
			vampireSpell.duration = 200;
			
			vampire.description = "If you want to taste the blood of others, then suck.";
			vampire.spellPrice = new ElementDefinition();
			vampire.spellPrice.setElementPoint("evil", 40);
			vampire.spellPrice.setElementPoint("ice", 10);
			vampire.spellPrice.setElementPoint("sticky", 10);
			
			vampire.handlerInfo = 20;
			registry.spellRegistries.put("vampire", vampire);
		}
	}
}
