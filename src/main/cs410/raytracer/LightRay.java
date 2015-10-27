package cs410.raytracer;

public class LightRay{
    
    RGB color;
    Vector postion;

    public LightRay(Vector postion, RGB color) {
        this.postion = postion;
        this.color = color;
    }

}
