package net.aegistudio.magick.effect;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;
import net.aegistudio.magick.spell.SpellEffect;

@Deprecated
public class SudoCommand implements SpellEffect {

	public static final String COMMAND = "command"; public String command;
	public static final String NEED_POWER = "needPower"; public boolean needPower = false;
	
	/** Whether getting op is remained to be discussed. **/
	@Override
	public void spell(MagickElement element, Player sender, Location location, String[] params) {
		if(command == null) return;
		boolean resetOp = false;
		if(needPower) resetOp = sender.isOp();
		
		sender.performCommand(command);
		
		if(needPower) {
			sender.setOp(true);
			sender.setOp(resetOp);
		}
	}

	@Override
	public void load(MagickElement element, ConfigurationSection spellConfig) {
		command = spellConfig.getString(COMMAND);
		if(spellConfig.contains(NEED_POWER)) needPower = spellConfig.getBoolean(NEED_POWER);
	}

	@Override
	public void save(MagickElement element, ConfigurationSection spellConfig) {
		spellConfig.set(COMMAND, command);
		spellConfig.set(NEED_POWER, needPower);
	}

	@Override
	public void after(MagickElement element) {
		
	}
}
