package cs410.raytracer;

import java.io.IOException;
import java.util.ArrayList;

public class RayTracer {


    private RayTracer() { }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("You failed specify at least four arguments.");
            System.out.println("Usage: raytracer.jar <cameramodelfile> <materialfile> <inputfile_1>...<inputfile_n> <outputfile>");
            System.exit(1);
        }

        String camerafile = args[0];
        String materialfile = args[1];
        String outputfile = args[args.length-1];

        System.out.println("Reading Camera Model...");
        CameraModel cameraModel = null;
        try {
            cameraModel = RayTracerIO.readCameraModel(camerafile);
        } catch (InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<Model> models = new ArrayList<Model>();

        System.out.println("Reading model files...");
        for(int i=2; i<args.length-1; i++){

            try {
                Model model = RayTracerIO.readModelFile(args[i]);
                models.add(model);

            } catch (NumberFormatException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InvalidFormatException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.out.println(e1.getMessage());
                System.exit(1);
            }
        }
        
        if(models.size() == 0){
            System.out.println("No models to read. Exiting...");
            System.exit(1);
        }

        System.out.println("Reading material properties...");
        ArrayList<LightSource> lightSources = null;
        try {
            lightSources = RayTracerIO.readMaterialFile(materialfile, models);
        } catch (InvalidFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.out.println(e1.getMessage());
            System.exit(1);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if(lightSources == null){
            System.out.println("Error reading material properties. Exiting...");
            System.exit(1);
        }

        //pull out ambient light source
        LightSource ambient = null;
        for(LightSource ls: lightSources){
            if(ls.isAmbient){
                if(ambient != null){
                    System.out.println("More than one ambient light source was found. Exiting...");
                    System.exit(1);
                }
                ambient = ls;
            }
        }

        if(ambient == null){
            System.out.println("No ambient light source was found. Exiting...");
            System.exit(1);
        }
        
        lightSources.remove(ambient);

        System.out.println("Running ray tracer...");

        ViewModel viewModel = new ViewModel(models, cameraModel, lightSources, ambient);
        viewModel.rayTrace();

        System.out.println("Ray tracer finished.\nWriting image file: " + outputfile);

        try {
            viewModel.writeImageToFile(outputfile);
            System.out.println("Done.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }
}
