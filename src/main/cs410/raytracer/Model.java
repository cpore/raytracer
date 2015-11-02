package cs410.raytracer;

import java.util.UUID;

public class Model {
	
	//used to uniquely identify models
	private UUID uuid = UUID.randomUUID();

	

	public static final int x = 0;
	public static final int y = 1;
	public static final int z = 2;
	public static final int w = 3;
	public static final int MIN_X = 0;
	public static final int MAX_X = 1;
	public static final int MIN_Y = 2;
	public static final int MAX_Y = 3;
	public static final int MIN_Z = 4;
	public static final int MAX_Z = 5;

	public String fileHeader;

	public double[][] verticies;
	public Vector meanVertex;
	public double[] boundingBox;
	public Face[] faces;

	public Model(String fileHeader, double[][] verticies, Face[] faces){
		this.fileHeader = fileHeader;
		this.verticies = verticies;
		this.faces = faces;
		updateStats();

	}
	
	/*public void combine(Model m){
	    double[][] verticies =  new double[4][this.verticies[0].length + m.verticies[0].length];
	    //copy first part
	    for(int i = 0; i < this.verticies[0].length; i++){
	        verticies[0][i] = this.verticies[0][i];
	        verticies[1][i] = this.verticies[1][i];
	        verticies[2][i] = this.verticies[2][i];
	        verticies[3][i] = this.verticies[3][i];
	    }
	    
	    //copy second part
	    for(int i = this.verticies[0].length; i< verticies[0].length; i++){
            verticies[0][i] = m.verticies[0][i];
            verticies[1][i] = m.verticies[1][i];
            verticies[2][i] = m.verticies[2][i];
            verticies[3][i] = m.verticies[3][i];
        }
	    
	    this.verticies = verticies;
	           
	    Face[] faces = new Face[this.faces.length + m.faces.length];
	    System.arraycopy(this.faces, 0, faces, 0, this.faces.length);
	    System.arraycopy(m.faces, 0, faces, this.faces.length, m.faces.length);
	    
	    this.faces = faces;
	    
	    updateStats();
	    
	    
	}*/

	private void updateStats() {
		boundingBox = new double[6];
		boundingBox[MIN_X] = Double.MAX_VALUE;
		boundingBox[MAX_X] = Double.MIN_VALUE;
		boundingBox[MIN_Y] = Double.MAX_VALUE;
		boundingBox[MAX_Y] = Double.MIN_VALUE;
		boundingBox[MIN_Z] = Double.MAX_VALUE;
		boundingBox[MAX_Z] = Double.MIN_VALUE;

		double totalx = 0;
		double totaly = 0;
		double totalz = 0;

		for(int j = 0; j < verticies[1].length; j++){
			Vector v = new Vector(verticies[x][j], verticies[y][j], verticies[z][j], 1);
			// update our mean vertex values
			totalx += v.p[x];
			totaly += v.p[y];
			totalz += v.p[z];

			// check for min/max values
			if(v.p[x] < boundingBox[MIN_X]){
				boundingBox[MIN_X] = v.p[x];
			}
			if(v.p[x] > boundingBox[MAX_X]){
				boundingBox[MAX_X] = v.p[x];
			}
			if(v.p[y] < boundingBox[MIN_Y]){
				boundingBox[MIN_Y] = v.p[y];
			}
			if(v.p[y] > boundingBox[MAX_Y]){
				boundingBox[MAX_Y] = v.p[y];
			}
			if(v.p[z] < boundingBox[MIN_Z]){
				boundingBox[MIN_Z] = v.p[z];
			}
			if(v.p[z] > boundingBox[MAX_Z]){
				boundingBox[MAX_Z] = v.p[z];
			}
		}

		meanVertex = new Vector(totalx / verticies[1].length, totaly / verticies[1].length, totalz / verticies[1].length, 1);
	}
	
	private Transform getScaleTransform(double sx, double sy, double sz){
		double[][] scaleMatrix = new double[][]{
			{ sx, 0, 0, 0 },
			{ 0, sy, 0, 0 },
			{ 0, 0, sz, 0 },
			{ 0, 0, 0, 1 }
		};
		return new Transform(scaleMatrix);
	}

	private Transform getTranslateTransform(double tx, double ty, double tz){
		double[][] translateMatrix = new double[][]{
			{ 1, 0, 0, tx },
			{ 0, 1, 0, ty },
			{ 0, 0, 1, tz },
			{ 0, 0, 0, 1 }
		};
		return new Transform(translateMatrix);
	}

	private Transform getZaxisRotationTransform(double theta){
		double theta2 = (double) Math.toRadians(theta);
		double[][] zRotation = new double[][]{
			{ (double) Math.cos(theta2), (double) -Math.sin(theta2), 0, 0 },
			{ (double) Math.sin(theta2), (double) Math.cos(theta2) , 0, 0 },
			{ 0                       , 0                        , 1, 0 },
			{ 0                       , 0                        , 0, 1 }
		};

		return new Transform(zRotation);
	}

	@SuppressWarnings("unused")
    private Transform getIdentityTransform(){
		double[][] identityMatrix = new double[][]{
			{ 1, 0, 0, 0 },
			{ 0, 1, 0, 0 },
			{ 0, 0, 1, 0 },
			{ 0, 0, 0, 1 }
		};
		return new Transform(identityMatrix);
	}

	public void scale(double sx, double sy, double sz) {
		Transform scaleTransform = getScaleTransform(sx, sy, sz);
		scaleTransform.apply(this);

	}

	public void translate(double tx, double ty, double tz) {
		Transform translateTransform = getTranslateTransform(tx, ty, tz);
		translateTransform.apply(this);

	}

	private Transform createRotationMatrix(double rx, double ry, double rz){
		//normalize the axis of rotation
		Vector w = new Vector(rx, ry, rz, 1);
		w.normalize();
		
		// create an axis m not parallel to w
		// by setting the smallest term in w to 1
		// and renormalizing
		double smallest = w.p[x];
		int idx = x;
		if(w.p[y] < smallest){
			smallest = w.p[y];
			idx = y;
		}
		if(w.p[z] < smallest){
			smallest = w.p[z];
			idx = z;
		}
		
		Vector m = new Vector(w.p[x], w.p[y], w.p[z], 1f);
		m.p[idx] = 1.0;
		m.normalize();
		
		Vector u = w.crossProduct(m);
		u.normalize();
		Vector v = w.crossProduct(u);
		v.normalize();
		
		double[][] rotationMatrix =  new double[][]{
			{ u.p[x], u.p[y], u.p[z], 0 },
			{ v.p[x], v.p[y], v.p[z], 0 },
			{ w.p[x], w.p[y], w.p[z], 0 },
			{ 0     , 0     , 0     , 1 }
		};
		
		return new Transform(rotationMatrix);		
	}

	public void rotate(double rx, double ry, double rz, double theta) {
		Transform zRotation = getZaxisRotationTransform(theta);
		Transform rotation = createRotationMatrix(rx, ry, rz);
		Transform rotationTranspose = rotation.getTranspose();
		
		Transform r1 = zRotation.multiply(rotation);
		Transform r2 = rotationTranspose.multiply(r1);
		r2.apply(this);

	}

	public void printStats(){
		updateStats();
		System.out.println("Number of Verticies: " + verticies[1].length);

		System.out.println("Number of Faces: " + faces.length);

		System.out.println("Mean Vertex: " + meanVertex.toString());

		System.out.println("Bounding Box:");
		System.out.println("\tMin X: " + Utils.prettyPrint(boundingBox[MIN_X]));
		System.out.println("\tMax X: " + Utils.prettyPrint(boundingBox[MAX_X]));
		System.out.println("\tMin Y: " + Utils.prettyPrint(boundingBox[MIN_Y]));
		System.out.println("\tMax Y: " + Utils.prettyPrint(boundingBox[MAX_Y]));
		System.out.println("\tMin Z: " + Utils.prettyPrint(boundingBox[MIN_Z]));
		System.out.println("\tMax Z: " + Utils.prettyPrint(boundingBox[MAX_Z]));
		
		double x = (boundingBox[MIN_X] + boundingBox[MAX_X]) / 2;
		double y = (boundingBox[MIN_Y] + boundingBox[MAX_Y]) / 2;
		double z = (boundingBox[MIN_Z] + boundingBox[MAX_Z]) / 2;
		
		System.out.println("Bounding box center: " + new Vector(x, y, z, 1).toString());
		System.out.println();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
