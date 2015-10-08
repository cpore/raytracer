package cs410.raytracer;

public class CameraModel {

    private Vector fp; // focal point

    private Vector lap; // look at point

    private Vector vup; // vector up

    private float d; // focal length

    private int minu, minv, maxu, maxv; // max/min coordinates of the view plane

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
    }
    
    public int getWidth(){
        // TODO is u width or height?
        return Math.abs(minu) + Math.abs(maxu);
    }
    
    public int getHeight(){
        // TODO is v width or height?
        return Math.abs(minv) + Math.abs(maxv);
    }

}
