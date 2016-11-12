package net.aegistudio.magick.seal;

import org.bukkit.entity.Entity;
import net.aegistudio.magick.Module;

public interface PainterFactory extends Module {
	public Painter newPainter(Entity entity);
}
