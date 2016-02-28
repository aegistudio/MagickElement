package net.aegistudio.magick.seal;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class YawOrientPainter extends TransformPainter {
	
	protected final Entity entity;
	public YawOrientPainter(Painter next, Entity entity) {
		super(next, new double[][] {
			{1, 0, 0, 0},
			{0, 1, 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 1}}
		);
		this.entity = entity;
		this.calculateTranslation(entity.getLocation());
	}

	@Override
	public void end() {
		this.calculateTranslation(entity.getLocation());
		this.next.end();
	}
	
	public static final double FACTOR = Math.PI / 180;
	
	public void calculateTranslation(Location orient) {
		for(int i = 0; i < 4; i ++)
			for(int j = 0; j < 4; j ++) 
				actual[i][j] = 0;
		
		double cosRotActual = Math.cos(orient.getYaw() * FACTOR);
		double sinRotActual = Math.sin(orient.getYaw() * FACTOR);
		actual[0][0] =  cosRotActual;
		actual[0][1] = -sinRotActual;
		actual[1][0] =  sinRotActual;
		actual[1][1] =  cosRotActual;
		actual[2][2] = 1;
		actual[3][3] = 1;
	}
}
