package cs410.raytracer;

public class LightRay extends Ray{

    Vector Lp; // full vector from the first Ray's POI to the light source
    Vector R; // the reflected ray from Lp around the surface normal
    Vector V;

    public LightRay(Vector L, Vector U, Vector R, Vector V) {
        super(L, U.getNormalized());
        this.Lp = new Vector(U);
        this.R = R;
        this.V = V;
    }

    public boolean isSelfOccluded(Face f){
        return U.dotProduct(f.N) < 0.0;
    }

    @Override
    public Vector getPointOfIntersection(Face f){
        double t = getT(f);

        // POI is on or behind image plane
        //t must be > 0 to use
        if(t < 0.01 || t > Lp.getMagnitude() || Double.isNaN(t)){

            return null;
        }

        //System.out.println("t = " + t);

        Vector P = L.add(U.multiply(t));
        return P;
    }
}