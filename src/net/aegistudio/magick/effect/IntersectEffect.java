package net.aegistudio.magick.effect;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;

public class IntersectEffect extends CompositeEffect {
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		double probability = Math.random();
		for(Entry<String, CompositeEffectEntry> entry : super.subEffects.entrySet()) {
			if(entry.getValue().probability >= probability) {
				entry.getValue().effect.spell(element, sender, location, params);
				return;
			}
			else probability -= entry.getValue().probability;
		}
	}
}
