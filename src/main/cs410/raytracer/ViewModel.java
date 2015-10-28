package cs410.raytracer;

import java.io.IOException;
import java.util.ArrayList;

public class ViewModel {

    private ArrayList<Model> modelList;
    private ArrayList<LightSource> lightSources;
    private LightSource ambient;
    private CameraModel cameraModel;

    private Image image;


    public ViewModel(ArrayList<Model> modelSet, CameraModel cameraModel, ArrayList<LightSource> lightSources, LightSource ambient) {
        this.modelList = modelSet;
        this.cameraModel = cameraModel;
        this.image = new Image(cameraModel.getHeight(), cameraModel.getWidth(), cameraModel.minu, cameraModel.minv);
        this.lightSources = lightSources;
        this.ambient = ambient;

    }

    public void rayTrace(){
        // may be used to divide work later
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of cores = " + cores);

        Thread[] workers = new Thread[cores];
        int sectionWidth = image.getWidth() / (cores-1);
        int remainder = image.getWidth() % (cores-1);

        int minu = cameraModel.minu;
        int maxu = cameraModel.maxu;
        int minv = cameraModel.minv;
        int maxv = cameraModel.maxv;
        for(int k = 0; k < cores; k++){
            if(k == cores-1 ){
                maxu = minu + remainder-1;   
            }else{
                maxu = minu + sectionWidth-1;      
            }
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

                Ray ray = new Ray(L, U, ambient.color);

                boolean hit = false;


                for(Model m: modelList){
                    for(Face f: m.faces){

                        if(!ray.intersectsSphere(f)){
                            continue;
                        }

                        hit = ray.intersectsPolygon(f);

                        //we know this pixel hit at least one face,
                        //so we don't need to check the rest of the faces
                        if(!hit){
                            //System.out.println("POI: " + P.toString()); 
                            continue;
                        }

                        //Calculate color along ray
                        for(LightSource ls: lightSources){

                            Vector Lp = ray.getLp(ls, f);

                            // check for self-occlusion (light is behind face)
                            if(Lp.dotProduct(f.N) < 0){
                                continue;
                            }
                            
                            // TODO make sure t value with Light ray is non-zero
                            
                            for(Model m2: modelList){
                                for(Face f2: m.faces){
                                    
                                }
                                
                            }
                            


                        }
                        //we know this pixel hit at least one face in this model,
                        //so we don't need to check the rest of the models
                        if(hit){
                            break;
                        }
                    }
                }


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
