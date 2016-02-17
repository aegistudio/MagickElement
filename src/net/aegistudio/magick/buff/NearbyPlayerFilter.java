package net.aegistudio.magick.buff;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.aegistudio.magick.MagickElement;

public class NearbyPlayerFilter implements EntityFilter {
	MagickElement element;
	public static final String RADIUM = "radium"; public float radium = 3.0f;
	public static final String SELF_INCLUDED = "selfIncluded"; public boolean selfIncluded = true;
	
	@Override
	public List<Entity> filter(Entity source) {
		Location location = source.getLocation();
		ArrayList<Entity> entity = new ArrayList<Entity>();
		for(Player player : element.getServer().getOnlinePlayers()) 
			if(player.getWorld().equals(location.getWorld())) 
				if(player.equals(source)) {
					if(selfIncluded) entity.add(player);
				}
				else if(player.getLocation().distance(location) <= radium) 
					entity.add(player);
		
		return entity;
	}

	@Override
	public void loadConfig(MagickElement element, ConfigurationSection configuration) {
		this.element = element;
	}

	@Override
	public void saveConfig(MagickElement element, ConfigurationSection configuration) {
		
	}
}
