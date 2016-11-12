package net.aegistudio.magick.effect;

import net.aegistudio.magick.AlgebraExpression;
import net.aegistudio.magick.spell.SpellEffect;

public class CompositeEffectEntry {
	public SpellEffect effect;
	
	public AlgebraExpression probability;
	{ 
		try {
			probability = new AlgebraExpression("1.0");
		}
		catch(Exception e) {
			
		}
	}
}
