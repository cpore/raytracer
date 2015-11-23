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
                
                //Throw ray from FP to pixel
                Vector L = cameraModel.getPixelPoint(u, v);
                Vector U = cameraModel.getUnit(L);

                Ray ray = new Ray(L, U);

                RGB I = calculateColor(ray);
                
                //Fill in pixel
                image.fillPixel(u, v, I);
            }
        }
    }

    private RGB calculateColor(Ray ray) {
        RGB I = new RGB();
        boolean isFront = false;
        for(Model m: modelList){
            for(Face f: m.faces){
             // check if ray hits a polygon
                if(!ray.intersectsSphere(f) || !ray.intersectsPolygon(f)){
                    continue;
                }
                //Calculate color along ray
                //(color of light ray) 
                if(isFrontFace(ray, f)){
                    isFront = true;
                    I = reflection(new RGB(), ray, f, 0);
                }
                if(isFront) break;
            }
            if(isFront) break;
        }
        
        return I;
    }
    
    private RGB reflection(RGB lightSum, Ray ray, Face f, int count){
        //TODO need to check ALL faces not just f!!!! 
        
        // check if ray hits a polygon
        if(!ray.intersectsSphere(f) || !ray.intersectsPolygon(f)){
            return lightSum;
        }
        
        if(count == 20) return lightSum;
        
        lightSum = f.Kd.multiply(ambient.B);

        for(LightSource ls: lightSources){

            LightRay lightRay = ray.getLightRay(ls, f);

            if(isShadowed(lightRay, f)) continue;
            
            // light source is not occluded or shadowed, so we can add the color
            RGB diffuse = f.Kd.multiply(ls.B.multiply(Math.max(0, lightRay.U.dotProduct(f.N))));
            RGB specular = ls.B.multiply(Math.pow(Math.max(0, lightRay.V.dotProduct(lightRay.R)), f.alpha)).multiply(f.ks);

            //if(specular.rgb[RGB.r] != 0.0f || specular.rgb[RGB.g] != 0.0f || specular.rgb[RGB.b] != 0.0f)
            //    System.out.println("Specular= " + specular.printRaw());
            //if(diffuse.rgb[RGB.r] != 0.0f || diffuse.rgb[RGB.g] != 0.0f || diffuse.rgb[RGB.b] != 0.0f)
            //    System.out.println("Diffuse= " + diffuse.printRaw());

            lightSum = lightSum.add(diffuse.add(specular));
        }

        Ray reflectedRay = new Ray(ray.getPointOfIntersection(f), ray.getRv(f));
        RGB reflection = reflection(lightSum, reflectedRay, f, ++count).multiply(f.ks);
        
        
        return lightSum.add(reflection);
    }

    private boolean isFrontFace(Ray ray, Face f) {
        for(Model m: modelList){
            for(Face f2: m.faces){

                // check if this is the front polygon to hit the ray
                if(!ray.intersectsSphere(f2)) continue;

                // make sure we don't check face against itself
                if(f2.equals(f)) continue;

                if(ray.intersectsPolygon(f2)){ 
                    if(ray.getT(f) > ray.getT(f2)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isShadowed(LightRay lightRay, Face f){
        // check for self-occlusion (light is behind face)
        if(lightRay.isSelfOccluded(f)) return true;
        
        for(Model m: modelList){
            for(Face f2: m.faces){
                // make sure we don't check face against itself
                if(f2.equals(f)) continue;

                if(!lightRay.intersectsPolygon(f2)) continue;

                if(lightRay.getT(f) < lightRay.getT(f2)){
                    return true;
                }
            }
        }
        return false;
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