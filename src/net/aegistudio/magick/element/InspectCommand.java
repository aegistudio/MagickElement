package net.aegistudio.magick.element;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.aegistudio.magick.CommandHandle;
import net.aegistudio.magick.MagickElement;

public class InspectCommand implements CommandHandle {
	@SuppressWarnings("deprecation")
	@Override
	public void handle(MagickElement element, CommandSender sender, String[] arguments) {
		Player player = (Player) sender;
		ItemStack item = player.getItemInHand();
		if(item == null || item.getType() == Material.AIR) 
			sender.sendMessage("You're not holding anything!");
		else {
			ElementDefinition definition = element.element.definition(item);
			Set<Entry<String, Integer>> elements = definition.elementPoint.entrySet();
			if(elements.isEmpty()) sender.sendMessage("This item does not contains any " 
					+ ChatColor.LIGHT_PURPLE + "element" + ChatColor.RESET + " power.");
			else {
				boolean first = true;
				StringBuilder result = new StringBuilder();
				Material material = item.getType();
				int damage = item.getDurability();
				
				result.append(ChatColor.BOLD + material.name() + ChatColor.RESET);
				result.append("(" + ChatColor.GRAY + material.getId() + ChatColor.WHITE + ")");
				result.append(":" + damage);
				result.append(" = ");
				
				for(Entry<String, Integer> elementEntry : elements) {
					if(first) first = false;
					else result.append(" + ");
					
					result.append(elementEntry.getValue());
					result.append(" ");
					result.append(ChatColor.LIGHT_PURPLE + elementEntry.getKey() + ChatColor.RESET);
				}
				ItemDamagePair transform = element.element.transform(item);
				
				if(transform != null){
					if(first) first = false;
					else result.append(" + ");
					
					material = transform.material;
					damage = transform.damage;
					
					result.append(ChatColor.BOLD + material.name() + ChatColor.RESET
						+ "(" + ChatColor.GRAY + material.getId() 
						+ ChatColor.WHITE + ")" + (damage == -1 || damage == 0?  "" : ":" 
						+ (damage == -1? item.getDurability() : damage)) + ChatColor.RESET);
				}
				
				sender.sendMessage(new String(result));
			}
		}
	}

	@Override
	public String description() {
		return "Tell how magickal is your current holding item.";
	}

	@Override
	public boolean visible(CommandSender sender) {
		return (sender instanceof Player);
	}

}
