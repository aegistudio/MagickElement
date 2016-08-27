package net.aegistudio.magick.seal;

/**
 * A painter accepts 2-D matrix and reflects
 * it into the world.
 * 
 * @author aegistudio
 */

public interface Painter {
	public void paint(double x, double y, double z, String[] arguments);
	
	public void end();
}
