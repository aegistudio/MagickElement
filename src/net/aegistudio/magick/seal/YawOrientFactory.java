package net.aegistudio.magick.seal;

import org.bukkit.entity.Entity;

public class YawOrientFactory extends WrappedPainterFactory{
	@Override
	public Painter newPainter(Entity entity) {
		return new YawOrientPainter(this.wrapped.newPainter(entity), entity);
	}
}
