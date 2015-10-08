package cs410.raytracer;

import java.util.HashSet;

public class ViewModel {

    private HashSet<Model> modelSet;
    private CameraModel cameraModel;
    private Image image;
    

    public ViewModel(HashSet<Model> modelSet, CameraModel cameraModel, Image image) {
        this.modelSet = modelSet;
        this.cameraModel = cameraModel;
        this.image = image;
    }

}
