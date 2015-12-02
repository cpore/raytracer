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
        Vector fN = new Vector(f.N);
        if(f.N.dotProduct(V) < 0.0){
            fN = f.N.multiply(-1.0);
        }
        return U.dotProduct(fN) < 0.0;
    }

    @Override
    public Vector getPointOfIntersection(Face f){
        double t = getT(f);

        // POI is on or behind image plane
        //t must be > 0 to use
        if(t < 0.00001 || t > Lp.getMagnitude() || Double.isNaN(t)){

            return null;
        }

        //System.out.println("t = " + t);

        Vector P = L.add(U.multiply(t));
        return P;
    }
    
    @Override
    public double getT(Face f){
        Vector fN = new Vector(f.N);
        if(f.N.dotProduct(V) < 0.0){
            fN = f.N.multiply(-1.0);
        }
        
        // find the point of intersection (P) of the plane this face is in
        double d = -fN.dotProduct(f.verticies[0]);
        //d = Math.abs(d);

        // what if this is zero?
        double nDotU = fN.dotProduct(U);
        double nDotL = fN.dotProduct(L);

        if(nDotU == 0.0 || Double.isNaN(nDotU)){
            return Double.NaN;
        }
        
        double t = (-(d + nDotL)) / (nDotU);
        return t;
    }
    
    /*@Override
    public boolean intersectsPolygon(Face f){
        Vector P = getPointOfIntersection(f);

        // the point of intersection is inside of or behind the camera, so ignore
        if(P == null){
            return false;
        }

        // determine if the point lies inside the polygon
        for(int i = 0; i < f.verticies.length; i++){
            Vector A = f.verticies[i];
            Vector B = f.verticies[(i+1) % f.verticies.length];
            //Vector C = f.verticies[(i+2) % length];
            Vector e1 = B.subtract(A);
            //Vector e2 = C.subtract(B);
            Vector epv1 = P.subtract(A);

            //Vector N = e1.crossProduct(e2);

            Vector Np = e1.crossProduct(epv1);
            
            Vector fN = new Vector(f.N);
            if(f.N.dotProduct(V) < 0.0){
                fN = f.N.multiply(-1.0);
            }
            
            double result = fN.dotProduct(Np);

            // if result >= 0 it is on the correct side
            //give it some leeway to fill holes
            if(result < -0.000001) return false;

        }

        return true;
    }*/
}