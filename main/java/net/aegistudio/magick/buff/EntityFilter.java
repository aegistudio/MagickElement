package net.aegistudio.magick.buff;

import java.util.List;

import org.bukkit.entity.Entity;
import net.aegistudio.magick.Module;

/**
 * Could be used to filter entities.
 * A source entity should be provided.
 * 
 * @author aegistudio
 */

public interface EntityFilter extends Module {
	public List<Entity> filter(Entity source);
}
