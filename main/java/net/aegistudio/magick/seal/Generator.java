package net.aegistudio.magick.seal;

import net.aegistudio.magick.Module;

/**
 * A generator is used to generate 2-D matrix
 * signal.
 * 
 * @author aegistudio
 */

public interface Generator extends Module {
	public void generate(Painter painter, String[] arguments);
}
