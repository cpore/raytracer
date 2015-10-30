package cs410.raytracer;

public class LightSource{
    
    RGB B;
    Vector position;
    boolean isAmbient = false;

    public LightSource(Vector position, RGB brightness) {
        this.position = position;
        this.B = brightness;
        isAmbient = position == null;
    }

}
