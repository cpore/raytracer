package cs410.raytracer;

import java.io.IOException;
import java.util.HashSet;

public class RayTracer {


	private RayTracer() { }

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("You failed specify at least three arguments.");
			System.out.println("Usage: raytracer.jar <cameramodelfile> <inputfile_1>...<inputfile_n> <outputfile>");
			System.exit(1);
		}

		String camerafile = args[0];
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
		
		HashSet<Model> models = new HashSet<Model>();
		
		
		System.out.println("Reading model files...");
		for(int i=1; i<args.length-1; i++){

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
		
		
		ViewModel viewModel = new ViewModel(models, cameraModel);
		
		System.out.println("Running ray tracer...");
		
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
