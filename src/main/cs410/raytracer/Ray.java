package cs410.raytracer;

public class Ray {

    Vector L;
    Vector U;

    public Ray(Vector L, Vector U) {
        this.L = L;
        this.U = U;
    }

    /**
     * finds where the Ray intersects the plane that the face is in.
     * @param f
     * @return
     */
    public Vector getPointOfIntersection(Face f){
        double t = getT(f);
        // POI is on or behind image plane
        //t must be > 1 to use
        if(Double.isNaN(t) || t <= 1.0){
            //System.out.println("t = " + t);
            return null;
        }

        Vector P = L.add(U.multiply(t));
        return P;
    }
    
    public double getT(Face f){
        // find the point of intersection (P) of the plane this face is in
        double d = -f.N.dotProduct(f.verticies[0]);
        //d = Math.abs(d);

        // what if this is zero?
        double nDotU = f.N.dotProduct(U);
        double nDotL = f.N.dotProduct(L);

        if(nDotU == 0.0 || Double.isNaN(nDotU)){
            return Double.NaN;
        }
        
        double t = (-(d + nDotL)) / (nDotU);
        return t;
    }
    
    public LightRay getLightRay(LightSource ls, Face f){
        Vector V = getV();
        
        //todo should we create a copy of f.N or replace it?
        Vector fN = new Vector(f.N);
        if(f.N.dotProduct(V) < 0.0){
            fN = f.N.multiply(-1.0);
        }
        
        Vector P = getPointOfIntersection(f);
        Vector Lp = ls.position.subtract(P);
        
        Vector LpNormal = Lp.getNormalized();
        
        Vector R = fN.multiply(2.0 * LpNormal.dotProduct(fN)).subtract(LpNormal).getNormalized();
        
        return new LightRay(P, Lp, R, V);
        
    }
    
    public Vector getV(){
        return U.multiply(-1.0);
    }
    
    public Vector getRv(Face f){
        Vector V = getV();
        Vector fN = new Vector(f.N);
        if(f.N.dotProduct(V) < 0.0){
            fN = f.N.multiply(-1.0);
        }
        return fN.multiply(2.0 * V.dotProduct(fN)).subtract(V).getNormalized();
    }

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

            // if result >= 0 it is on the correct side
            //give it some leeway to fill holes
            if(result < -0.000001) return false;

        }

        return true;
    }
    
    public boolean intersectsSphere(Face f){
        
        double rSquared = -1.0;
        // determine the radius from the 0th vertex to the farthest vertex of the polygon
        Vector Pc = f.verticies[0];
        for(int i = 1; i < f.verticies.length; i++){
            double radius = f.verticies[i].subtract(Pc).getMagnitude();
            
            if(radius > rSquared){
                rSquared = radius;
            }
        }
        
        rSquared *= rSquared;
        
        Vector T = Pc.subtract(L);
        
        double UdotTSquared = U.dotProduct(T);
        UdotTSquared *= UdotTSquared;
        
        double TdotT = T.dotProduct(T);
        
        double discriminant = (UdotTSquared - TdotT) + rSquared;

        return discriminant >= 0.0;
    }

}
