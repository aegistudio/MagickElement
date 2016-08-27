package net.aegistudio.magick.seal;

public abstract class TransformPainter implements Painter {
	protected double trans[][];
	protected double actual[][];
	protected final Painter next;
	
	public TransformPainter(Painter next,
			double[][] trans, double[][] actual) {
		this.next = next;
		this.trans = trans;
		this.actual = actual;
	}
	
	public TransformPainter(Painter next, 
			double[][] trans) {
		this(next, trans, new double[][]{
			{1, 0, 0, 0}, 
			{0, 1, 0, 0}, 
			{0, 0, 1, 0}, 
			{0, 0, 0, 1}}
		);
	}
	
	protected double[] vecBuffer = new double[4];
	protected double[] vec = new double[4];
	@Override
	public void paint(double x, double y, double z, String[] arguments) {
		this.vec[0] = x; this.vec[1] = y; this.vec[2] = z; this.vec[3] = 1;
		this.multUnifyVec();
		next.paint(this.vec[0], this.vec[1], this.vec[2], arguments);
	}
	
	protected void setZero() {
		for(int i = 0; i < 4; i ++)
			for(int j = 0; j < 4; j ++)
				actual[i][j] = 0;
	}
	
	protected void addTrans() {
		for(int i = 0; i < 4; i ++)
			for(int j = 0; j < 4; j ++)
					actual[i][j] += this.trans[i][j];
	}
	
	protected void multTrans() {
		double[][] nextActual = new double[4][4];
		for(int i = 0; i < 4; i ++)
			for(int j = 0; j < 4; j ++)
				for(int k = 0; k < 4; k ++)
					nextActual[i][j] += this.trans[i][k] * this.actual[k][j];
		this.actual = nextActual;
	}
	
	public void multUnifyVec() {
		for(int i = 0; i < 4; i ++)
			this.vecBuffer[i] = 0;
		for(int i = 0; i < 4; i ++)
				for(int j = 0; j < 4; j ++)
					this.vecBuffer[i] += this.actual[i][j] * this.vec[j];
		
		for(int i = 0; i < 4; i ++)
			this.vecBuffer[i] /= this.vecBuffer[3];
		
		double[] vecTemp = this.vec;
		this.vec = vecBuffer;
		this.vecBuffer = vecTemp;
	}
}
