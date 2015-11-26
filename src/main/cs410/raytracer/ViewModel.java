package cs410.raytracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
        Random r = new Random();
        for(int u = minu; u <= maxu; u++){
            for(int v = minv; v <= maxv; v++){

                ArrayList<RGB> avg = new ArrayList<RGB>();
                for(int i=0; i<1; i++){
                    double r1 = r.nextDouble() - 0.5;
                    double r2 = r.nextDouble() - 0.5;
                    //Throw ray from FP to pixel
                    Vector L = cameraModel.getPixelPoint(u + r1, v + r2);
                    Vector U = cameraModel.getUnit(L);

                    Ray ray = new Ray(L, U);

                    RGB I = calculateColor(ray, new RGB(), 0);
                    avg.add(i, I);

                }

                //Fill in pixel
                image.fillPixel(u, v, getAverage(avg));
            }
        }
    }
    
    private RGB getAverage(ArrayList<RGB> avg){
        double r = 0.0, g = 0.0, b = 0.0;
        for(RGB rgb: avg){
            r += rgb.rgb[RGB.r];
            g += rgb.rgb[RGB.g];
            b += rgb.rgb[RGB.b];
        }
        return new RGB(r/avg.size(), g/avg.size(), b/avg.size());
    }

    private RGB calculateColor(Ray ray, RGB lightSum, int count) {

        if(count++ == 20) return lightSum;

        Face f = intersects(ray);
        if(f == null) return lightSum;

        if(count == 1) lightSum = f.Kd.multiply(ambient.B);

        for(LightSource ls: lightSources){

            LightRay lightRay = ray.getLightRay(ls, f);

            if(isShadowed(lightRay, f)) continue;

            // light source is not occluded or shadowed, so we can add the color
            RGB diffuse = f.Kd.multiply(ls.B.multiply(Math.max(0, lightRay.U.dotProduct(f.N))));
            RGB specular = ls.B.multiply(Math.pow(Math.max(0, lightRay.V.dotProduct(lightRay.R)), f.alpha)).multiply(f.ks);

            /* if(specular.rgb[RGB.r] != 0.0f || specular.rgb[RGB.g] != 0.0f || specular.rgb[RGB.b] != 0.0f)
                            System.out.println("Specular= " + specular.printRaw());
                        if(diffuse.rgb[RGB.r] != 0.0f || diffuse.rgb[RGB.g] != 0.0f || diffuse.rgb[RGB.b] != 0.0f)
                            System.out.println("Diffuse= " + diffuse.printRaw());*/

            lightSum = lightSum.add(diffuse.add(specular));
        }

        //System.out.println("count = " + count);
        if(f.ks > -0.0001){
            Ray reflectedRay = new Ray(ray.getPointOfIntersection(f), ray.getRv(f));
            lightSum = lightSum.add(calculateColor(reflectedRay, lightSum, count).multiply(f.ks));
        }

        if(lightSum.belowThreshold()) return lightSum;
        
        if(f.kt > -0.0001){
            Ray refractedRay = new Ray(ray.getPointOfIntersection(f), ray.getV().multiply(-1));
            lightSum = lightSum.add(calculateColor(refractedRay, lightSum, count).multiply(f.kt));
        }

        if(lightSum.belowThreshold()) return lightSum;

        return lightSum;
    }

    private Face intersects(Ray ray){

        for(Model m: modelList){
            for(Face f: m.faces){
                // check if ray hits a polygon
                if(!ray.intersectsSphere(f) || !ray.intersectsPolygon(f)){
                    continue;
                }

                if(!isFrontFace(ray, f)) continue;

                return f;
            }
        }

        return null;
    }


    private boolean isFrontFace(Ray ray, Face f) {
        double closestT = Double.MAX_VALUE;
        for(Model m: modelList){
            for(Face f2: m.faces){
                // make sure we don't check face against itself
                if(f2.equals(f)) continue;

                // check if this is the front polygon to hit the ray
                if(!ray.intersectsSphere(f2) || !ray.intersectsPolygon(f2)){
                    continue;
                }

                if(ray.getT(f2) <= closestT){
                    closestT = ray.getT(f2);
                }

            }
        }
        return ray.getT(f) <= closestT;
    }

    private boolean isShadowed(LightRay lightRay, Face f){

        // check for self-occlusion (light is behind face)
        if(lightRay.isSelfOccluded(f)){
            //System.out.println("Self Occluded");
            return true;
        }else{
            //System.out.println("Not Self Occluded");
        }

        //double closestT = Double.MAX_VALUE;

        for(Model m: modelList){
            for(Face f2: m.faces){
                // make sure we don't check face against itself
                if(f2.equals(f)) continue;
                
                //if(!isFrontFace(lightRay, f2)) continue;

                if(!lightRay.intersectsPolygon(f2)){
                    continue;
                }

                if(f2.kt > -0.0001) continue;

                if(lightRay.getT(f2) > lightRay.getT(f)){
                    //System.out.println("Shadowed");
                    return true;
                }
            }
        }

        return false;//closestT <= lightRay.getT(f);
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