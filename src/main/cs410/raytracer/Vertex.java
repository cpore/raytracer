package cs410.raytracer;

public class Vertex {

	public static final int x = 0;
	public static final int y = 1;
	public static final int z = 2;

	public float[] vertex = new float[3];

	public Vertex(float x, float y, float z) {
		vertex[Vertex.x] = x;
		vertex[Vertex.y] = y;
		vertex[Vertex.z] = z;
	}

	public Vertex(float[] values) {
		this.vertex = values;
	}

	@Override
	public String toString() {
		return Utils.prettyPrint(vertex[x]) + " " + Utils.prettyPrint(vertex[y]) + " " + Utils.prettyPrint(vertex[z]);
	}


}
