package cs410.raytracer;

public class RGB {
    
    public final static int r = 0;
    public final static int g = 1;
    public final static int b = 2;
    int[] rgb;
    
    public RGB(){
        rgb = new int[3];
    }
    
    @Override
    public String toString(){
        return String.format("%3d %3d %3d", rgb[r], rgb[g], rgb[b]);
    }

}
