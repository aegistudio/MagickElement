package net.aegistudio.magick;

import org.bukkit.potion.PotionEffectType;
import net.aegistudio.magick.effect.RangedSpawn;
import net.aegistudio.magick.effect.FeatherFall;
import net.aegistudio.magick.effect.PotionResistance;
import net.aegistudio.magick.effect.VampireHand;
import net.aegistudio.magick.element.ElementExpression;
import net.aegistudio.magick.entity.Lightning;
import net.aegistudio.magick.entity.NearestGround;
import net.aegistudio.magick.spell.SpellEntry;
import net.aegistudio.magick.spell.SpellRegistry;

public class RegistryInitializer implements Initializer<SpellRegistry> {
	@Override
	public void initial(MagickElement element, SpellRegistry registry) throws Exception {
		{
			SpellEntry lightning = new SpellEntry(element);
			RangedSpawn lightningRound = new RangedSpawn();
			lightning.effect = lightningRound;
			lightning.description = "Ziz iz an epic dizcharge of electric power from zky, zzz...";
			lightningRound.cluster = 3;
			lightningRound.tier = 5;
			lightningRound.delay = 40;
			lightningRound.entity = new NearestGround(new Lightning());
			
			lightning.spellPrice = new ElementExpression();
			lightning.spellPrice.setExpression("electric", "30");
			lightning.spellPrice.setExpression("wind", "30");
			lightning.spellPrice.setExpression("divine", "10");
			lightning.spellPrice.setExpression("evil", "20");
			
			lightning.handlerInfo = new AlgebraExpression("50");
			registry.spellRegistries.put("lightning", lightning);
		}
		
		{
			SpellEntry featherFall = new SpellEntry(element);
			FeatherFall featherFalling = new FeatherFall();
			featherFall.effect = featherFalling;
			featherFall.description = "Slow down your dropping rate to a feather.";
			
			featherFall.spellPrice = new ElementExpression();
			featherFall.spellPrice.setExpression("wind", "80");
			
			featherFall.handlerInfo = new AlgebraExpression("20");
			registry.spellRegistries.put("featherFall", featherFall);
		}
		
		{
			SpellEntry antidote = new SpellEntry(element);
			PotionResistance antidoteSpell 
				= new PotionResistance(PotionEffectType.POISON);
			antidote.effect = antidoteSpell;
			
			antidoteSpell.buffName = "Antidote";
			antidote.description = "An antidote that defeating all harmful proteins.";
			antidote.spellPrice = new ElementExpression();
			antidote.spellPrice.setExpression("terra", "20");
			antidote.spellPrice.setExpression("water", "10");
			
			antidote.handlerInfo = new AlgebraExpression("5");
			registry.spellRegistries.put("antidote", antidote);
		}
		
		{
			SpellEntry vitality = new SpellEntry(element);
			PotionResistance vitalitySpell 
				= new PotionResistance(PotionEffectType.WITHER);
			vitality.effect = vitalitySpell;
			
			vitalitySpell.buffName = "Vitality";
			vitality.description = "Bring you force of living against withering.";
			vitality.spellPrice = new ElementExpression();
			vitality.spellPrice.setExpression("terra", "40");
			
			vitality.handlerInfo = new AlgebraExpression("5");
			registry.spellRegistries.put("vitality", vitality);
		}
		
		{
			SpellEntry determined = new SpellEntry(element);
			PotionResistance determineSpell 
				= new PotionResistance(PotionEffectType.HARM);
			determined.effect = determineSpell;
			
			determineSpell.buffName = "Determined";
			determineSpell.duration = 200;
			
			determined.description = "You have a determined heart to hurt others.";
			determined.spellPrice = new ElementExpression();
			determined.spellPrice.setExpression("divine", "5");
			determined.spellPrice.setExpression("evil", "5");
			determined.spellPrice.setExpression("fire", "5");
			determined.spellPrice.setExpression("electric", "5");
			determined.spellPrice.setExpression("ice", "5");
			determined.spellPrice.setExpression("water", "5");
			determined.spellPrice.setExpression("wind", "5");
			determined.spellPrice.setExpression("terra", "5");
			
			determined.handlerInfo = new AlgebraExpression("15");
			registry.spellRegistries.put("determined", determined);
		}
		
		{
			SpellEntry vampire = new SpellEntry(element);
			VampireHand vampireSpell = new VampireHand();
			vampire.effect = vampireSpell;
			
			vampireSpell.duration = 200;
			
			vampire.description = "If you want to taste the blood of others, then suck.";
			vampire.spellPrice = new ElementExpression();
			vampire.spellPrice.setExpression("evil", "40");
			vampire.spellPrice.setExpression("ice", "10");
			vampire.spellPrice.setExpression("sticky", "10");
			
			vampire.handlerInfo = new AlgebraExpression("20");
			registry.spellRegistries.put("vampire", vampire);
		}
	}
}
