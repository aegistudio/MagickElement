package net.aegistudio.magick;

import org.bukkit.command.CommandSender;

public interface CommandHandle {
	public void handle(MagickElement element, CommandSender sender, String[] arguments);
	
	public String description();
	
	public boolean visible(CommandSender sender);
}
