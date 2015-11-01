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
    
    public String printRaw(){
        return String.valueOf(rgb[r]) + ", " + String.valueOf(rgb[g]) + ", " + String.valueOf(rgb[b]);
    }
    
    @Override
    public String toString(){
        return String.format("%3d %3d %3d", Math.round(rgb[r]), Math.round(rgb[g]), Math.round(rgb[b]));
    }

    public float getMinVal(){
        return Math.min(Math.min(rgb[r], rgb[g]), rgb[b]);
    }
    
    public float getMaxVal(){
        return Math.max(Math.max(rgb[r], rgb[g]), rgb[b]);
    }
    
    public RGB add(RGB that){
        float rr = this.rgb[r] + that.rgb[r];
        float gg = this.rgb[g] + that.rgb[g];
        float bb = this.rgb[b] + that.rgb[b];
        
        return new RGB(rr, gg, bb);
    }
    
    public RGB multiply(RGB that){
        float rr = this.rgb[r] * that.rgb[r];
        float gg = this.rgb[g] * that.rgb[g];
        float bb = this.rgb[b] * that.rgb[b];
        
        return new RGB(rr, gg, bb);
    }
    
    public RGB multiply(float s){
        float rr = this.rgb[r] * s;
        float gg = this.rgb[g] * s;
        float bb = this.rgb[b] * s;
        
        return new RGB(rr, gg, bb);
    }
}
