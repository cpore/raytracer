package cs410.raytracer;

public class Model {

	public static final int x = 0;
	public static final int y = 1;
	public static final int z = 2;
	public static final int MIN_X = 0;
	public static final int MAX_X = 1;
	public static final int MIN_Y = 2;
	public static final int MAX_Y = 3;
	public static final int MIN_Z = 4;
	public static final int MAX_Z = 5;

	public String fileHeader;

	public Vector[] verticies;
	public Vector meanVertex;
	public float[] boundingBox;
	public Face[] faces;

	public Model(String fileHeader, Vector[] verticies, Face[] faces){
		this.fileHeader = fileHeader;
		this.verticies = verticies;
		this.faces = faces;
		boundingBox = new float[6];
		float totalx = 0;
		float totaly = 0;
		float totalz = 0;

		for(Vector v: verticies){
			// update our mean vertex values
			totalx += v.x;
			totaly += v.y;
			totalz += v.z;

			// check for min/max values
			if(v.x < boundingBox[MIN_X]){
				boundingBox[MIN_X] = v.x;
			}else if(v.x > boundingBox[MAX_X]){
				boundingBox[MAX_X] = v.x;
			}

			if(v.y < boundingBox[MIN_Y]){
				boundingBox[MIN_Y] = v.y;
			}else if(v.y > boundingBox[MAX_Y]){
				boundingBox[MAX_Y] = v.y;
			}

			if(v.z < boundingBox[MIN_Z]){
				boundingBox[MIN_Z] = v.z;
			}else if(v.z > boundingBox[MAX_Z]){
				boundingBox[MAX_Z] = v.z;
			}
		}

		meanVertex = new Vector(totalx / verticies.length, totaly / verticies.length, totalz / verticies.length);

	}

	public void scale(float sx, float sy, float sz) {

	}

	public void translate(float tx, float ty, float tz) {

	}

	public void rotate(float rx, float ry, float rz, float theta) {

	}

	public void printStats(){
		System.out.println("Number of Verticies: " + verticies.length);

		System.out.println("Number of Faces: " + faces.length);

		System.out.println("Mean Vertex: " + meanVertex.toString());

		System.out.println("Bounding Box:");
		System.out.println("\tMin X: " + Utils.prettyPrint(boundingBox[MIN_X]));
		System.out.println("\tMax X: " + Utils.prettyPrint(boundingBox[MAX_X]));
		System.out.println("\tMin Y: " + Utils.prettyPrint(boundingBox[MIN_Y]));
		System.out.println("\tMax Y: " + Utils.prettyPrint(boundingBox[MAX_Y]));
		System.out.println("\tMin Z: " + Utils.prettyPrint(boundingBox[MIN_Z]));
		System.out.println("\tMax Z: " + Utils.prettyPrint(boundingBox[MAX_Z]));
	}



}
