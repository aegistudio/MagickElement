package net.aegistudio.magick.seal;

import java.awt.image.Raster;
import net.aegistudio.magick.Module;

public interface Filter extends Module {
	public boolean pick(Raster image, int x, int y);
}
