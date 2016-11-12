package net.aegistudio.magick.element;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import net.aegistudio.magick.CommandHandle;
import net.aegistudio.magick.MagickElement;
import org.bukkit.ChatColor;

public class ElementCommand implements CommandHandle {

	@SuppressWarnings("deprecation")
	@Override
	public void handle(MagickElement element, CommandSender sender, String[] arguments) {
		if(arguments.length == 0) {
			sender.sendMessage("You seem not to specify the " + ChatColor.LIGHT_PURPLE + "element" + ChatColor.RESET + " you search for!");
			sender.sendMessage(ChatColor.BOLD + "Usage" + ChatColor.RESET + ": "  + ChatColor.YELLOW + "element <name>");
		}
		else {
			int total = 0;
			for(Entry<ItemDamagePair, ElementDefinition> entry : element.element.element.entrySet()) {
				int elementValue = entry.getValue().getElementValue(arguments[0]);
				if(elementValue > 0)  {
					Material material = entry.getKey().material;
					int damage = entry.getKey().damage;
					sender.sendMessage(ChatColor.BOLD + material.name() + ChatColor.RESET + "(" + ChatColor.GRAY + material.getId() 
							+ ChatColor.WHITE + ")" + (damage == -1?  "" : ":" + damage) 
							+ " = " + elementValue + " " + ChatColor.LIGHT_PURPLE + arguments[0] + ChatColor.RESET);
					total ++;
				}
			}
			if(total == 0) 
				sender.sendMessage("There's no item containing " + ChatColor.LIGHT_PURPLE + arguments[0] + ChatColor.RESET + ".");
		}
	}

	@Override
	public String description() {
		return "Show which item have specific element.";
	}

	@Override
	public boolean visible(CommandSender sender) {
		return true;
	}

}
