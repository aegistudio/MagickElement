package net.aegistudio.magick;

public interface Initializer<T extends Module> {
	public void initial(MagickElement element, T module) throws Exception;
}
