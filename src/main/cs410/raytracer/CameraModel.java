package cs410.raytracer;

public class CameraModel {
    
    Image image;

    Vector fp; // focal point

    Vector lap; // look at point

    Vector vup; // vector up

    float d; // focal length
    
    float fpdn;

    int minu, minv, maxu, maxv; // max/min coordinates of the view plane

    public CameraModel(Vector fp, Vector lap, Vector vup, float d, int minu, int minv, int maxu,
            int maxv) {

        this.fp = fp;
        this.lap = lap;
        this.vup = vup;
        this.d = d;
        this.minu = minu;
        this.minv = minv;
        this.maxu = maxu;
        this.maxv = maxv;
        
        this.fpdn =  fp.p[Vector.z] + d;
        
        this.image = new Image(getHeight(), getWidth());
        
    }
    
    public int getWidth(){
        // TODO is u width or height?
        return Math.abs(minu) + Math.abs(maxu);
    }
    
    public int getHeight(){
        // TODO is v width or height?
        return Math.abs(minv) + Math.abs(maxv);
    }
    
    public void fillPixel(int u, int v, RGB color){
        int x = v - minv; 
        int y = u - minu;
        
        image.pixels[x][y] = color;
    }
    
    public Vector getPixelPoint(int u, int v){
        
        return new Vector(u, v, fpdn, 1);
    }

    public Vector getUnit(Vector L) {
        Vector E = fp;
        
        Vector unit = L.subtract(E);
        unit.normalize();
        return unit;
    }

}
