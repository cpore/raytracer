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
                int numRays = 1;
                for(int i=0; i<numRays; i++){
                    double r1 = r.nextDouble() - 0.5;
                    double r2 = r.nextDouble() - 0.5;
                    //Throw ray from FP to pixel
                    if(numRays == 1){
                        r1 = 0.0;
                        r2 = 0.0;
                    }
                    Vector L = cameraModel.getPixelPoint(u + r1, v + r2);
                    Vector U = cameraModel.getUnit(L);

                    Ray ray = new Ray(L, U);

                    RGB I = calculateColor(ray, new RGB(), 0, copyModel());
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

    private RGB calculateColor(Ray ray, RGB lightSum, int count, ArrayList<Model> world) {
        if(count >= 20) return lightSum;
        if(count++ == 20) return lightSum;

        Face f = intersects(ray, world);
        if(f == null) return lightSum;

        lightSum = lightSum.add(f.Kd.multiply(ambient.B));

        for(LightSource ls: lightSources){

            LightRay lightRay = ray.getLightRay(ls, f);

            if(isShadowed(lightRay, f, world)) continue;
            
          
            Vector fN = new Vector(f.N);
            if(f.N.dotProduct(ray.getV()) < 0.0){
                fN = f.N.multiply(-1.0);
            }
            // light source is not occluded or shadowed, so we can add the color
            RGB diffuse = ls.B.multiply(Math.max(0, lightRay.U.dotProduct(fN))).multiply(f.Kd);
            RGB specular = ls.B.multiply(Math.pow(Math.max(0, lightRay.V.dotProduct(lightRay.R)), f.alpha)).multiply(f.ks);

             //if(specular.rgb[RGB.r] != 0.0f || specular.rgb[RGB.g] != 0.0f || specular.rgb[RGB.b] != 0.0f)
             //    System.out.println("Specular= " + specular.printRaw());
                   /*     if(diffuse.rgb[RGB.r] != 0.0f || diffuse.rgb[RGB.g] != 0.0f || diffuse.rgb[RGB.b] != 0.0f)
                            System.out.println("Diffuse= " + diffuse.printRaw());*/

            lightSum = lightSum.add(diffuse.add(specular));
        }

        //System.out.println("count = " + count);
        if(f.ks != 0.0){
            Ray reflectedRay = new Ray(ray.getPointOfIntersection(f), ray.getRv(f));
            RGB product = calculateColor(reflectedRay, lightSum, count, world).multiply(f.ks);
            if(product.belowThreshold()){
                //System.out.println("below");
                return lightSum;
            }
            lightSum = lightSum.add(product);
        }

        if(f.kt != 0.0){
            //System.out.println("sfas");
            Ray refractedRay = new Ray(ray.getPointOfIntersection(f), ray.getV().multiply(-1));
            lightSum = lightSum.add(calculateColor(refractedRay, lightSum, count, world).multiply(f.kt));
        }

        return lightSum;
    }

    private Face intersects(Ray ray, ArrayList<Model> world){

        for(Model m: world){
            for(Face f: m.faces){
                // check if ray hits a polygon
                if(!ray.intersectsPolygon(f)){
                    continue;
                }

                if(!isFirstFace(ray, f, world)) continue;

                return f;
            }
        }

        return null;
    }

    private ArrayList<Model> copyModel(){
        ArrayList<Model> copy = new ArrayList<Model>();
        for(Model m: modelList) copy.add(m);

        return copy;
    }


    private boolean isFirstFace(Ray ray, Face f, ArrayList<Model> world) {
        if(f.kt >= 1.0) return false;

        double closestT = Double.MAX_VALUE;
        for(Model m: world){
            for(Face f2: m.faces){
                // make sure we don't check face against itself
                if(f2.equals(f)) continue;

                // check if this is the front polygon to hit the ray
                if(!ray.intersectsPolygon(f2)){
                    continue;
                }

                if(ray.getT(f2) < closestT && f2.kt < 1.0){
                    closestT = ray.getT(f2);
                }

            }
        }
        return ray.getT(f) <= closestT;
    }

    private boolean isShadowed(LightRay lightRay, Face f, ArrayList<Model> world){

        // check for self-occlusion (light is behind face)
        if(lightRay.isSelfOccluded(f)){
            //System.out.println("Self Occluded");
            return true;
        }else{
            //System.out.println("Not Self Occluded");
        }

        //double closestT = Double.MAX_VALUE;

        for(Model m: world){
            for(Face f2: m.faces){
                // make sure we don't check face against itself
                if(f2.equals(f)) continue;

                //if(!isFrontFace(lightRay, f2)) continue;

                if(!lightRay.intersectsPolygon(f2)){
                    continue;
                }

                if(f2.kt != 0.0) continue;

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