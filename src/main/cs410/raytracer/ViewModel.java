package cs410.raytracer;

import java.io.IOException;
import java.util.HashSet;

public class ViewModel {

    private HashSet<Model> modelSet;
    private CameraModel cameraModel;


    public ViewModel(HashSet<Model> modelSet, CameraModel cameraModel) {
        this.modelSet = modelSet;
        this.cameraModel = cameraModel;

    }

    public void rayTrace(){
        // may be used to divide work later
        // int cores = Runtime.getRuntime().availableProcessors();
        int totalPixels = cameraModel.getWidth() * cameraModel.getHeight();
        int pixelCounter = 0;
        int pct = 0;
        System.out.println("Working... " +pct+ "% Complete.");
        for(int u = cameraModel.minu; u <= cameraModel.maxu; u++){
            for(int v = cameraModel.minv; v <= cameraModel.maxv; v++){
                pixelCounter++;
                //Throw ray from FP to pixel
                Vector L = cameraModel.getPixelPoint(u, v);
                Vector U = cameraModel.getUnit(L);

                boolean hit = false;

                for(Model m: modelSet){
                    for(Face f: m.faces){

                        Vector P = getPointOfIntersection(L, U, f);

                        // the point of intersection is inside of or behind the camera, so ignore
                        if(P == null){
                            continue;
                        }

                        //System.out.println(P.toString());
                        hit = intersects(P, f);

                        //we know this pixel hit at least one face,
                        //so we don't need to check the rest of the faces
                        if(hit){
                            //System.out.println("POI: " + P.toString()); 
                            break;
                        }
                    }
                    //we know this pixel hit at least one face in this model,
                    //so we don't need to check the rest of the models
                    if(hit){
                        break;
                    }
                }
                //Calculate color along ray

                //Fill in pixel
                RGB color =  hit ? new RGB(255, 255, 255) : new RGB(0, 0, 0);
                cameraModel.fillPixel(u, v, color);


            }
            //update progress
            int newPct = (int) (((float)pixelCounter / (float)totalPixels) * 100f);
            if(newPct > pct+5){
                pct = newPct-1;
                System.out.println("Working... " + (pct) + "% Complete.");

            }
        }
        System.out.println("Finished... 100% Complete.");
    }

    private Vector getPointOfIntersection(Vector L, Vector U, Face f){
        // find the point of intersection (P) of the plane this face is in
        float d = -f.N.dotProduct(f.verticies[0]);
        //d = Math.abs(d);

        // what if this is zero?
        float nDotU = f.N.dotProduct(U);
        float nDotL = f.N.dotProduct(L);

       /* if(Float.compare(nDotU, 0.0f) == 0){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }*/
        
        if(nDotU == 0.0f){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }

        if(Float.isNaN(nDotU)){
            //System.out.println("nDotU = " + nDotU);
            return null;
        }

        float t = (-(d + nDotL)) / (nDotU);

        //if(Float.isNaN(t) || Float.isInfinite(t)) System.out.println("t = " + t);
        //t must be > 1 to use
       /* if(Float.compare(t, 1.0f) <= 0 || Float.isNaN(t)){
            //System.out.println("t = " + t);
            return null;
        }*/
        if(t <= 1.0f || Float.isNaN(t)){
            //System.out.println("t = " + t);
            return null;
        }

        Vector P = L.add(U.multiply(t));
        return P;
    }

    private boolean intersects(Vector P, Face f){
        // determine if the point lies inside the polygon
        int length = f.verticies.length;
        for(int i = 0; i < length; i++){
            Vector A = f.verticies[i];
            Vector B = f.verticies[(i+1) % length];
            //Vector C = f.verticies[(i+2) % length];
            Vector e1 = B.subtract(A);
            //Vector e2 = C.subtract(B);
            Vector epv1 = P.subtract(A);

            //Vector N = e1.crossProduct(e2);

            Vector Np = e1.crossProduct(epv1);

            float result = f.N.dotProduct(Np);
            //System.out.println("result = " + result);
            //if(result < 0.0f) return false;

            // if result >= 0 it is on the correct side
            //if(Float.compare(result, 0.0f) < 0) return false;
            //give it some leeway to fill holes
            if(result < -0.01) return false;

        }

        return true;
    }

    public void writeImageToFile(String outputfile) throws IOException{
        cameraModel.image.writeToFile(outputfile);
    }

}
