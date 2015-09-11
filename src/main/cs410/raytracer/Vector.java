package cs410.raytracer;

public class Vector {

	public float x;
	public float y;
	public float z;

	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Adds the given Vector to this Vector
	 * u = u + v
	 * @param v
	 */
	public void add(Vector v){
		x += v.x;
		y += v.y;
		z += v.z;
	}

	/**
	 * Subtracts the given Vector from this Vector
	 * u = u + -v
	 * @param v
	 */
	public void subtract(Vector v){
		x += (-v.x);
		y += (-v.y);
		z += (-v.z);
	}


	/**
	 * Calculates and returns the dot product of this Vector and v
	 * d = u . v
	 * @param v
	 */
	public float dotProduct(Vector v){
		float xVal = x * v.x;
		float yVal = y * v.y;
		float zVal = z * v.z;

		return xVal + yVal + zVal;
	}

	/**
	 * Calculates and returns a Vector that is the cross product of this Vector
	 * and v
	 * d = u x v
	 * 
	 * cx = ay*bz - az*by
	 * cy = az*bx - ax*bz
	 * cz = ax*by - ay*bx	

	 * @param v
	 */
	public Vector crossProduct(Vector v){
		float cx = (y * v.z) - (z * v.y);
		float cy = (z * v.x) - (x * v.z);
		float cz = (x * v.y) - (y * v.x);

		return new Vector(cx, cy, cz);
	}

	/**
	 * Multiplies this Vector by the given scalar
	 * u = s * u
	 * @param v
	 */
	public void multiply(float s){
		x *= s;
		y *= s;
		z += s;
	}

	/**
	 * Divides this Vector by the given scalar
	 * u = s / u
	 * @param v
	 */
	public void divide(float s){
		x /= s;
		y /= s;
		z /= s;
	}

	/**
	 * Calculates the magnitude of this Vector
	 * @return the magnitude of this Vector
	 */
	public float getMagnitude(){
		return (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	/**
	 * Creates a normalized Vector from this Vector
	 * @return a new, normalized version of this Vector
	 */
	public Vector getNormal(){
		float m = getMagnitude();
		return new Vector (x / m, y /m, z /m);
	}

	@Override
	public String toString() {
		return Utils.prettyPrint(x) + " " + Utils.prettyPrint(y) + " " + Utils.prettyPrint(z);
	}


}
