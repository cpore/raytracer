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
        return Lp.dotProduct(f.N) < 0;
    }


}
