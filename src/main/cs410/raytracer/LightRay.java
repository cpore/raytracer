package cs410.raytracer;

public class LightRay extends Ray{
    
    Vector Lp; //vector from the first Ray's POI to the light source
    Vector R; // the reflected ray from Lp around the surface normal

    public LightRay(Vector L, Vector U, Vector Lp, Vector R) {
        super(L, U);
        this.Lp = Lp;
        this.R = R;
    }
    
    public boolean isOccluded(LightSource ls, Face f){
        return Lp.dotProduct(f.N) < 0.0;
    }
    
    /**
     * finds where the Ray intersects the plane that the face is in.
     * @param f
     * @return
     */
    @Override
    protected Vector getPointOfIntersection(Face f){
        // find the point of intersection (P) of the plane this face is in
        double d = -f.N.dotProduct(f.verticies[0]);
        //d = Math.abs(d);

        // what if this is zero?
        double nDotU = f.N.dotProduct(U);
        double nDotL = f.N.dotProduct(L);

        if(nDotU == 0.0){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }
        

        if(Double.isNaN(nDotU)){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }

        double t = (-(d + nDotL)) / (nDotU);

        //if(double.isNaN(t) || double.isInfinite(t)) System.out.println("t = " + t);
        //t must be > 1 to use
   
        if(t > 0.0 || Double.isNaN(t)){
            //System.out.println("t = " + t);
            return null;
        }

        Vector P = L.add(U.multiply(t));
        return P;
    }
    
    
    @Override
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

            double result = f.N.dotProduct(Np);
            //System.out.println("result = " + result);
            //if(result < 0.0f) return false;

            // if result >= 0 it is on the correct side
            //give it some leeway to fill holes
            if(result < -0.01) return false;

        }

        return true;
    }

}
