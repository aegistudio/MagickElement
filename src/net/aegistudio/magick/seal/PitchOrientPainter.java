package net.aegistudio.magick.seal;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class PitchOrientPainter extends TransformPainter {
	protected final double initPhase;
	protected final Entity entity;
	public PitchOrientPainter(Painter next, Entity entity, double initPhase) {
		super(next, new double[][] {
			{1, 0, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 1}}
		);
		this.entity = entity;
		this.calculateTranslation(entity.getLocation());
		this.initPhase = initPhase;
	}

	@Override
	public void end() {
		this.calculateTranslation(entity.getLocation());
		this.next.end();
	}
	
	public static final double FACTOR = Math.PI / 180;
	
	public void calculateTranslation(Location orient) {
		super.setZero();
		
		double cosRotActual = Math.cos((-orient.getPitch() + initPhase) * FACTOR);
		double sinRotActual = Math.sin((-orient.getPitch() + initPhase) * FACTOR);
		actual[0][0] = 1;
		actual[1][1] =  cosRotActual;
		actual[1][2] = -sinRotActual;
		actual[2][1] =  sinRotActual;
		actual[2][2] =  cosRotActual;
		actual[3][3] = 1;
	}
}
