package cs410.raytracer;

public class Vector {
    final float EPSILON = 0.00001f;
    public static final int x = 0;
    public static final int y = 1;
    public static final int z = 2;
    public static final int w = 3;

    public float[] p = new float[4];

    public Vector(float px, float py, float pz, float pw) {
        p[x] = px;
        p[y] = py;
        p[z] = pz;
        p[w] = pw;
    }

    /*public Vector(Vector v) {
        p[x] = v.p[x];
        p[y] = v.p[y];
        p[z] = v.p[z];
        p[w] = v.p[w];
    }*/

    /**
     * Adds the given Vector to this Vector u = u + v and returns the new vector
     * 
     * @param v
     */
    public Vector add(Vector v) {
        return new Vector(p[x] + v.p[x], p[y] + v.p[y], p[z] + v.p[z], p[w]);
    }

    /**
     * Subtracts the given Vector from this Vector u = u + -v
     * 
     * @param v
     */
    public Vector subtract(Vector v) {
        //Vector newV = ;
        return add(v.multiply(-1));
        //return new Vector(p[x] + newV.p[x], p[y]  + newV.p[y], p[z] + newV.p[z], p[w]);
    }

    /**
     * Calculates and returns the dot product of this Vector and v d = u . v
     * 
     * @param v
     */
    public float dotProduct(Vector v) {
        float xVal = p[x] * v.p[x];
        float yVal = p[y] * v.p[y];
        float zVal = p[z] * v.p[z];
        //float wVal = p[w] * v.p[w];

        return xVal + yVal + zVal;
    }
    
    /**
     * Calculates and returns the dot product of this Vector and v d = u . v
     * 
     * @param v
     */
    public float dotProductTransform(Vector v) {
        float xVal = p[x] * v.p[x];
        float yVal = p[y] * v.p[y];
        float zVal = p[z] * v.p[z];
        float wVal = p[w] * v.p[w];

        return xVal + yVal + zVal + wVal;
    }

    /**
     * Calculates and returns a Vector that is the cross product of this Vector
     * and v. A cross-product is a normal (perpendicular, but not orthonormal) Vector to the two
     * given vectors d = u x v
     * 
     * cx = ay*bz - az*by cy = az*bx - ax*bz cz = ax*by - ay*bx
     * 
     * @param v
     */
    public Vector crossProduct(Vector v) {
        float cx = (p[y] * v.p[z]) - (p[z] * v.p[y]);
        float cy = (p[z] * v.p[x]) - (p[x] * v.p[z]);
        float cz = (p[x] * v.p[y]) - (p[y] * v.p[x]);

        return new Vector(cx, cy, cz, 1);
    }

    /**
     * Multiplies this Vector by the given scalar u = s * u
     * 
     * @param v
     */
    public Vector multiply(float s) {
        return new Vector(p[x] * s, p[y] * s, p[z] * s, p[w]);
    }

    /**
     * Divides this Vector by the given scalar u = s / u
     * 
     * @param v
     */
    public Vector divide(float s) {
        return new Vector(p[x] / s, p[y] / s, p[z] / s, p[w]);
    }

    /**
     * Calculates the magnitude of this Vector
     * 
     * @return the magnitude of this Vector
     */
    public float getMagnitude() {
        return (float) Math.sqrt((p[x] * p[x]) + (p[y] * p[y]) + (p[z] * p[z]));
    }

    /**
     * Creates a normalized Vector from this Vector
     * 
     * @return a new, normalized version of this Vector
     */
    public Vector getNormal() {
        float m = getMagnitude();
        return new Vector(p[x] / m, p[y] / m, p[z] / m, 1);
    }

    /**
     * Normalizes this vector.
     */
    public void normalize() {
        float m = getMagnitude();
        p[x] = p[x] / m;
        p[y] = p[y] / m;
        p[z] = p[z] / m;
        p[w] = 1;
    }

    @Override
    public String toString() {
        return Utils.prettyPrint(p[x]) + " " + Utils.prettyPrint(p[y]) + " "
                + Utils.prettyPrint(p[z]);
    }
    
    public boolean equalTo(Vector v){
        boolean xe = Math.abs(p[x] - v.p[x]) < EPSILON;
        boolean ye = Math.abs(p[y] - v.p[y]) < EPSILON;
        boolean ze = Math.abs(p[z] - v.p[z]) < EPSILON;
        return xe && ye && ze;
    }
    

}
