package cs410.raytracer;

import java.io.IOException;
import java.util.HashSet;

public class ViewModel {

    private HashSet<Model> modelSet;
    private Model fullModel;
    private CameraModel cameraModel;



    public ViewModel(HashSet<Model> modelSet, CameraModel cameraModel) {
        this.modelSet = modelSet;
        this.cameraModel = cameraModel;
        this.fullModel = new Model("Full Model\n", new float[4][0], new Face[0]);

        for(Model m: modelSet){
            fullModel.combine(m);
        }

        System.out.println("Full model stats:\n");
        fullModel.printStats();

    }

    public void rayTrace(){
        // may be used to divide work later
        // int cores = Runtime.getRuntime().availableProcessors();
        for(int u = cameraModel.minu; u <= cameraModel.maxu; u++){
            for(int v = cameraModel.minv; v <= cameraModel.maxv; v++){
                //Throw ray from FP to pixel
                Vector L = cameraModel.getPixelPoint(u, v);
                Vector U = cameraModel.getUnit(L);

                for(Face f: fullModel.faces){

                    Vector P = getPointOfIntersection(L, U, f);

                    //Calculate color along ray

                    //Fill in pixel
                    RGB color = intersects(P, f) ? new RGB(255, 255, 255) : new RGB();
                    cameraModel.fillPixel(u, v, color);
                }
            }
        }
    }

    private Vector getPointOfIntersection(Vector L, Vector U, Face f){
        // find the point of intersection (P) of the plane this face is in
        float d = f.N.dotProduct(f.verticies[0]);
        d = Math.abs(d);
        float nDotL = f.N.dotProduct(L);
        float t = (d - nDotL) / (nDotL);

        Vector P = L.add(U.multiply(t));
        return P;
    }

    private boolean intersects(Vector P, Face f){
        // determine if the point lies inside the polygon
        int length = f.verticies.length;
        for(int i = 0; i < length-1; i++){
            Vector V1 = f.verticies[i];
            Vector V2 = f.verticies[(i+1) % length];
            Vector e1 = V2.subtract(V1);
            Vector epv1 = P.subtract(V1);

            Vector Np = e1.crossProduct(epv1);
            
            float result = f.N.dotProduct(Np);
            if(result < 0) return false;

        }


        return true;
    }

    public void writeImageToFile(String outputfile) throws IOException{
        cameraModel.image.writeToFile(outputfile);
    }

}
