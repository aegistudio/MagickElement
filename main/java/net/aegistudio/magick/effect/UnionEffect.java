package net.aegistudio.magick.effect;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.Parameter;

public class UnionEffect extends CompositeEffect {
	@Override
	public void spell(MagickElement element, Entity sender, Location location, String[] params) {
		Parameter param = new Parameter(params);
		double probability = Math.random();
		for(Entry<String, CompositeEffectEntry> entry : super.subEffects.entrySet()) {
			if(entry.getValue().probability.getDouble(param) >= probability) 
				entry.getValue().effect.spell(element, sender, location, params);
		}
	}
}
