package cs410.raytracer;

public class Ray {

    RGB pixel;
    Vector L;
    Vector U;

    public Ray(Vector L, Vector U) {
        this.L = L;
        this.U = U;
        pixel = new RGB();
    }

    /**
     * finds where the Ray intersects the plane that the face is in.
     * @param f
     * @return
     */
    private Vector getPointOfIntersection(Face f){
        // find the point of intersection (P) of the plane this face is in
        float d = -f.N.dotProduct(f.verticies[0]);
        //d = Math.abs(d);

        // what if this is zero?
        float nDotU = f.N.dotProduct(U);
        float nDotL = f.N.dotProduct(L);

        /* if(Float.compare(nDotU, 0.0f) == 0){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }*/

        if(nDotU == 0.0f){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }

        if(Float.isNaN(nDotU)){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }

        float t = (-(d + nDotL)) / (nDotU);

        //if(Float.isNaN(t) || Float.isInfinite(t)) System.out.println("t = " + t);
        //t must be > 1 to use
        /* if(Float.compare(t, 1.0f) <= 0 || Float.isNaN(t)){
            //System.out.println("t = " + t);
            return null;
        }*/
        if(t <= 1.0f || Float.isNaN(t)){
            //System.out.println("t = " + t);
            return null;
        }

        Vector P = L.add(U.multiply(t));
        return P;
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

            float result = f.N.dotProduct(Np);
            //System.out.println("result = " + result);
            //if(result < 0.0f) return false;

            // if result >= 0 it is on the correct side
            //if(Float.compare(result, 0.0f) < 0) return false;
            //give it some leeway to fill holes
            if(result < -0.01) return false;

        }

        pixel = new RGB(255, 255, 255);
        return true;
    }
    
    public boolean intersectsSphere(Face f){
        
        float rSquared = -1.0f;
        // determine the radius from the 0th vertex to the farthest vertex of the polygon
        Vector Pc = f.verticies[0];
        for(int i = 1; i < f.verticies.length; i++){
            float radius = f.verticies[i].subtract(Pc).getMagnitude();
            
            if(radius > rSquared){
                rSquared = radius;
            }
        }
        //System.out.println("Radius = " + r);
        rSquared *= rSquared;
        
        Vector T = Pc.subtract(L);
        
        float UdotTSquared = U.dotProduct(T);
        UdotTSquared *= UdotTSquared;
        
        float TdotT = T.dotProduct(T);
        
        float discriminant = (UdotTSquared - TdotT) + rSquared;

        /*if(discriminant >= 0.0){
            
        }else{
            System.out.println("discriminant = " + discriminant);
        }*/
        return discriminant >= 0.0f;
    }

}
