package cs410.raytracer;

public class LightSource{
    
    RGB color;
    Vector position;
    boolean isAmbient = false;

    public LightSource(Vector position, RGB color) {
        this.position = position;
        this.color = color;
        isAmbient = position == null;
    }

}
