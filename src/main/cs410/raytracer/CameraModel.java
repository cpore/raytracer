package cs410.raytracer;

public class CameraModel {
    
    Image image;

    Vector fp; // focal point

    Vector lap; // look at point

    Vector vup; // vector up

    float d; // focal length
    
    Vector fpdn;
    
    Vector U; // the x axis in the basis
    Vector V; // the y axis in the basis
    Vector W; // the z axis in the basis

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
        
        System.out.println("FP = " + fp);
        System.out.println("lap = " + lap);
        System.out.println("vup = " + vup);
        System.out.println("d = " + d);
        System.out.println("minu = " + minu);
        System.out.println("minv = " + minv);
        System.out.println("maxu = " + maxu);
        System.out.println("maxv = " + maxv);
        
        W = lap.subtract(fp).getNormal();
        U = vup.crossProduct(W).getNormal();
        V = W.crossProduct(U).getNormal();
        
        System.out.println("W = " + W.toString());
        System.out.println("U = " + U.toString());
        System.out.println("V = " + V.toString());
        
        this.fpdn =  fp.add(W.multiply(d));
        
        System.out.println("fpdn = " + fpdn.toString());
        
        this.image = new Image(getHeight(), getWidth());
        
    }
    
    public int getWidth(){
        return Math.abs(minu) + Math.abs(maxu) + 1;
    }
    
    public int getHeight(){
        return Math.abs(minv) + Math.abs(maxv) + 1;
    }
    
    public void fillPixel(int u, int v, RGB color){
        int x = v - minv; 
        int y = u - minu;
        
        image.pixels[x][y] = color;
    }
    
    public Vector getPixelPoint(int u, int v){
        //float u = (float) (((float)minu + ((float)maxu-(float)minu) * ((float)i+0.5)) / (float)getWidth());
        //float v = (float) (((float)minv + ((float)maxv-(float)minv) * ((float)j+0.5)) / (float)getHeight());
        
        Vector uU = U.multiply(u);
        Vector vV = V.multiply(v);
        
        return fpdn.add(uU.add(vV));
    }

    public Vector getUnit(Vector L) {
        Vector unit = L.subtract(fp);
        unit.normalize();
        return unit;
    }

}
