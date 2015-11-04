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
        System.out.println("Number of CPU cores = " + cores);

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

                RGB I = new RGB();
                //Throw ray from FP to pixel
                Vector L = cameraModel.getPixelPoint(u, v);
                Vector U = cameraModel.getUnit(L);

                Ray ray = new Ray(L, U);

                boolean hit = false;

                for(Model m: modelList){
                    for(Face f: m.faces){

                        // check if ray hits a polygon
                        if(!ray.intersectsSphere(f)){
                            continue;
                        }

                        if(!(hit = ray.intersectsPolygon(f))) continue;

                        //Calculate color along ray
                        //(color of light ray) 
                        RGB lightSum = new RGB();
                        //boolean isOccluded = false;
                        //boolean isShadowed = false;
                        boolean isBehind = false;

                        for(LightSource ls: lightSources){

                            LightRay lightRay = ray.getLightRay(ls, f);

                            // check for self-occlusion (light is behind face)
                            if(lightRay.isOccluded(f)){
                                //System.out.println("occluded");
                                //isOccluded = true;
                                continue;
                            }

                            boolean shadowed = false;
                            for(Model m2: modelList){
                                for(Face f2: m2.faces){
                                    // make sure we don't check face against itself
                                    if(f2.equals(f)) continue;

                                    // check if this is the front polygon to hit the ray
                                    if(ray.intersectsPolygon(f2)){ 
                                        shadowed = ray.getT(f) > ray.getT(f2);
                                    }
                                    
                                    if(shadowed){
                                        isBehind = true;
                                        break;
                                    }
                                    
                                    if(lightRay.intersectsPolygon(f2)){
                                        shadowed = true;
                                        if(lightRay.getT(f) < lightRay.getT(f2)){
                                            //System.out.println("behind");
                                            isBehind = true;
                                        }
                                        break;
                                    }
                                }

                                if(shadowed){
                                    break;
                                }
                            }

                            if(shadowed) continue;

                            // light source is not occluded or shadowed, so we can add the color
                            RGB diffuse = f.Kd.multiply(ls.B.multiply(Math.max(0, lightRay.U.dotProduct(f.N))));
                            RGB specular = ls.B.multiply(Math.pow(Math.max(0, lightRay.V.dotProduct(lightRay.R)), f.alpha)).multiply(f.ks);

                            //if(specular.rgb[RGB.r] != 0.0f || specular.rgb[RGB.g] != 0.0f || specular.rgb[RGB.b] != 0.0f)
                            //    System.out.println("Specular= " + specular.printRaw());
                            //if(diffuse.rgb[RGB.r] != 0.0f || diffuse.rgb[RGB.g] != 0.0f || diffuse.rgb[RGB.b] != 0.0f)
                            //    System.out.println("Diffuse= " + diffuse.printRaw());

                            lightSum = lightSum.add(diffuse.add(specular));
                        }

                        //we know this pixel hit at least one face in this model,
                        //and it is the very front face,
                        //so we don't need to check the rest of the models
                        if(!isBehind){
                            RGB KdBa = f.Kd.multiply(ambient.B);
                            RGB Il = KdBa.add(lightSum);
                            I = I.add(Il);
                            //break;
                        }
                        
                    }
                    //we know this pixel hit at least one face in this model,
                    //so we don't need to check the rest of the models
                    // this prevents double-counting faces but may not be a good idea
                    // when we recurse on the light rays
                    if(hit){
                        break;
                    }
                }
                //Fill in pixel
                image.fillPixel(u, v, I);
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