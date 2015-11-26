package cs410.raytracer;

public class CameraModel {

    Vector fp; // focal point

    Vector lap; // look at point

    Vector vup; // vector up

    double d; // focal length
    
    Vector fpdn;
    
    Vector U; // the x axis in the basis
    Vector V; // the y axis in the basis
    Vector W; // the z axis in the basis

    int minu, minv, maxu, maxv; // max/min coordinates of the view plane

    public CameraModel(Vector fp, Vector lap, Vector vup, double d, int minu, int minv, int maxu,
            int maxv) {

        this.fp = fp;
        this.lap = lap;
        this.vup = vup;
        this.d = d;
        this.minu = minu;
        this.minv = minv;
        this.maxu = maxu;
        this.maxv = maxv;
        
        W = lap.subtract(fp).getNormalized();
        U = W.crossProduct(vup).getNormalized();
        V = W.crossProduct(U).getNormalized();
        
        this.fpdn = fp.add(W.multiply(d));

        
        System.out.println("FP = " + this.fp);
        System.out.println("lap = " + this.lap);
        System.out.println("vup = " + this.vup);
        System.out.println("d = " + this.d);
        System.out.println("minu = " + this.minu);
        System.out.println("minv = " + this.minv);
        System.out.println("maxu = " + this.maxu);
        System.out.println("maxv = " + this.maxv);
        System.out.println("W = " + W.toString());
        System.out.println("U = " + U.toString());
        System.out.println("V = " + V.toString());
        System.out.println("fpdn = " + fpdn.toString());
        
    }
    
    public int getWidth(){
        return Math.abs(minu) + Math.abs(maxu) + 1;
    }
    
    public int getHeight(){
        return Math.abs(minv) + Math.abs(maxv) + 1;
    }
    
    
    
    public Vector getPixelPoint(double u, double v){
        //double u1 = (double) (((double)minu + ((double)maxu-(double)minu) * ((double)u+0.5)) / (double)getWidth());
        //double v1 = (double) (((double)minv + ((double)maxv-(double)minv) * ((double)v+0.5)) / (double)getHeight());
        
        Vector uU = U.multiply(u);
        Vector vV = V.multiply(v);
        
        return fpdn.add(uU.add(vV));
    }

    public Vector getUnit(Vector L) {
        Vector unit = L.subtract(fp).getNormalized();
        //unit.normalize();
        return unit;
    }

}
