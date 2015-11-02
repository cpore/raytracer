package cs410.raytracer;

public class RGB {
    
    public final static int r = 0;
    public final static int g = 1;
    public final static int b = 2;
    double[] rgb;
    
    /**
     * Creates a new black pixel
     */
    public RGB(){
        rgb = new double[3];
    }
    
    public RGB(double r, double g, double b){
        rgb = new double[3];
        rgb[RGB.r] = r;
        rgb[RGB.g] = g;
        rgb[RGB.b] = b;
    }
    
    public String printRaw(){
        return String.valueOf(rgb[r]) + ", " + String.valueOf(rgb[g]) + ", " + String.valueOf(rgb[b]);
    }
    
    @Override
    public String toString(){
        return String.format("%3d %3d %3d", Math.round(rgb[r]), Math.round(rgb[g]), Math.round(rgb[b]));
    }

    public double getMinVal(){
        return Math.min(Math.min(rgb[r], rgb[g]), rgb[b]);
    }
    
    public double getMaxVal(){
        return Math.max(Math.max(rgb[r], rgb[g]), rgb[b]);
    }
    
    public RGB add(RGB that){
        double rr = this.rgb[r] + that.rgb[r];
        double gg = this.rgb[g] + that.rgb[g];
        double bb = this.rgb[b] + that.rgb[b];
        
        return new RGB(rr, gg, bb);
    }
    
    public RGB multiply(RGB that){
        double rr = this.rgb[r] * that.rgb[r];
        double gg = this.rgb[g] * that.rgb[g];
        double bb = this.rgb[b] * that.rgb[b];
        
        return new RGB(rr, gg, bb);
    }
    
    public RGB multiply(double s){
        double rr = this.rgb[r] * s;
        double gg = this.rgb[g] * s;
        double bb = this.rgb[b] * s;
        
        return new RGB(rr, gg, bb);
    }
}
