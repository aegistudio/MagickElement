package net.aegistudio.magick.spell;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import net.aegistudio.magick.CommandHandle;
import net.aegistudio.magick.MagickElement;

public class SpellCommand implements CommandHandle {
	
	@Override
	public void handle(MagickElement element, CommandSender sender, String[] arguments) {
		if(arguments.length == 0) {
			sender.sendMessage(ChatColor.BOLD + "Listing" + ChatColor.RESET + " all " 
					+ ChatColor.AQUA + "spells" + ChatColor.RESET + " on this server:");
			StringBuilder spellBuilder = new StringBuilder();
			boolean first = true;
			for(String spell : element.registry.spellRegistries.keySet()) {
				if(first) first = false;
				else spellBuilder.append(", ");
				spellBuilder.append(ChatColor.AQUA);
				spellBuilder.append(spell);
				spellBuilder.append(ChatColor.RESET);
			}
			sender.sendMessage("  " + new String(spellBuilder));
			sender.sendMessage(ChatColor.BOLD + "Tips" + ChatColor.RESET + ": use " + ChatColor.YELLOW + "spell <spellName> [<parameter>]" 
					+ ChatColor.RESET + " to view detail of a spell.");
		}
		else {
			SpellEntry spell = element.registry.getSpell(arguments[0]);
			if(spell == null) {
				sender.sendMessage("Spell " + arguments[0] + " is not known to this server.");
				return;
			}
			else {
				sender.sendMessage(ChatColor.BOLD + "Spell" + ChatColor.RESET + ": " + ChatColor.AQUA + arguments[0]);
				sender.sendMessage(ChatColor.BOLD + "Description" + ChatColor.RESET + ": " + spell.description);
				
				TreeMap<String, Integer> price = spell.spellPrice.get(arguments).elementPoint;
				StringBuilder requirement = new StringBuilder(ChatColor.BOLD + "Require " + ChatColor.RESET);
				boolean first = true;
				for(Entry<String, Integer> priceEntry : price.entrySet()) {
					if(first) first = false;
					else requirement.append(", ");
					
					requirement.append(priceEntry.getValue());
					requirement.append(" ");
					requirement.append(ChatColor.LIGHT_PURPLE);
					requirement.append(priceEntry.getKey());
					requirement.append(ChatColor.RESET);
				}
				requirement.append(" to empower.");
				
				sender.sendMessage(new String(requirement));
				sender.sendMessage(element.handler.infoSpell(spell, arguments));
			}
		}
	}

	@Override
	public String description() {
		return "List or show available spells on this server.";
	}

	@Override
	public boolean visible(CommandSender sender) {
		return true;
	}
}
