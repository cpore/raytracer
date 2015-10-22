package cs410.raytracer;

import java.io.IOException;
import java.util.HashSet;

public class ViewModel {

    private HashSet<Model> modelSet;
    private CameraModel cameraModel;

    private Image image;


    public ViewModel(HashSet<Model> modelSet, CameraModel cameraModel) {
        this.modelSet = modelSet;
        this.cameraModel = cameraModel;
        this.image = new Image(cameraModel.getHeight(), cameraModel.getWidth(), cameraModel.minu, cameraModel.minv);

    }

    public void rayTrace(){
        // may be used to divide work later
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of cores = " + cores);
        
        Thread[] workers = new Thread[cores];
        int sectionWidth = image.getWidth()/cores;
        int minu = cameraModel.minu;
        int maxu = cameraModel.maxu;
        int minv = cameraModel.minv;
        int maxv = cameraModel.maxv;
        for(int k = 0; k < cores; k++){
            maxu = minu + sectionWidth-1;      
            workers[k] = new Thread(new ImageSection(minu, maxu, minv, maxv));
            minu = maxu + 1;
            workers[k].start();
        }
        
        for(int k = 0; k < workers.length; k++){
            try {
                workers[k].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void rayTraceSection(int minu, int maxu, int minv, int maxv) {
        for(int u = minu; u <= maxu; u++){
            for(int v = minv; v <= maxv; v++){
                //Throw ray from FP to pixel
                Vector L = cameraModel.getPixelPoint(u, v);
                Vector U = cameraModel.getUnit(L);

                Ray ray = new Ray(L, U);

                boolean hit = false;

                for(Model m: modelSet){
                    for(Face f: m.faces){
                        //System.out.println(P.toString());
                        
                        if(!ray.intersectsSphere(f)){
                            continue;
                        }
                        hit = ray.intersectsPolygon(f);

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
                image.fillPixel(u, v, ray.pixel);

            }
        }
    }

    public void writeImageToFile(String outputfile) throws IOException{
        image.writeToFile(outputfile);
    }
    
    public class ImageSection implements Runnable{
        
        private int minu, maxu, minv, maxv;
        
        public ImageSection(int minu, int maxu, int minv, int maxv) {
            this.maxu = maxu;
            this.maxv = maxv;
            this.minu = minu;
            this.minv = minv;
        }

        @Override
        public void run() {
            rayTraceSection(minu, maxu, minv, maxv);
            
        }
        
    }

}
