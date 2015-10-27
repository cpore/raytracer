package cs410.raytracer;

public class RGB {
    
    public final static int r = 0;
    public final static int g = 1;
    public final static int b = 2;
    float[] rgb;
    
    /**
     * Creates a new black pixel
     */
    public RGB(){
        rgb = new float[3];
    }
    
    public RGB(float r, float g, float b){
        rgb = new float[3];
        rgb[RGB.r] = r;
        rgb[RGB.g] = g;
        rgb[RGB.b] = b;
    }
    
    @Override
    public String toString(){
        return String.format("%3d %3d %3d", rgb[r], rgb[g], rgb[b]);
    }

}
