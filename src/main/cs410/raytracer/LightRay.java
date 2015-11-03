package cs410.raytracer;

public class LightRay extends Ray{
    
    Vector Lp; //vector from the first Ray's POI to the light source
    Vector R; // the reflected ray from Lp around the surface normal
    Vector V;

    public LightRay(Vector L, Vector U, Vector Lp, Vector R) {
        super(L, U);
        this.Lp = Lp;
        this.R = R;
        this.V = U.multiply(-1);
    }
    
    public boolean isOccluded(LightSource ls, Face f){
        return Lp.dotProduct(f.N) < 0.0;
    }
    
    public double getT(Vector P, Vector ls, Face f){
        // find the point of intersection (P) of the plane this face is in
        double d = -f.N.dotProduct(f.verticies[0]);
        //d = Math.abs(d);

        // what if this is zero?
        double nDotU = f.N.dotProduct(Lp);
        double nDotL = f.N.dotProduct(P);

        if(nDotU == 0.0 || Double.isNaN(nDotU)){
            return Double.NaN;
        }
        
        double t = (-(d + nDotL)) / (nDotU);
        
        return t;
    }
    
    public Vector getPointOfIntersection(Vector P, Vector lightSource, Face f){
        double t = getT(P, lightSource, f);
        // POI is on or behind image plane
        //t must be > 1 to use
        if(Double.isNaN(t) || t == 0.0){
            //System.out.println("t = " + t);
            return null;
        }

        Vector P2 = P.add(lightSource.multiply(t));
        return P2;
    }
    
    public boolean intersectsPolygon(Vector P, Vector lightSource, Face f){
        Vector P2 = getPointOfIntersection(P, lightSource, f);

        // the point of intersection is inside of or behind the camera, so ignore
        if(P2 == null){
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

            // if result >= 0 it is on the correct side
            //give it some leeway to fill holes
            if(result < -0.001) return false;

        }

        return true;
    }

}
