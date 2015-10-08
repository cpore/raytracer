package cs410.raytracer;

import java.io.IOException;
import java.util.Scanner;

public class PA1 {
	

	public PA1() {

	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("You failed specify exactly two arguments.");
			System.out.println("Usage: raytracer.jar <inputfile> <outputfile>");
			System.exit(1);
		}
		
		String inputfile = args[0];
		String outputfile = args[1];
		
		Model model = null;
		try {
			model = RayTracerIO.readModelFile(inputfile);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Scanner scanner = new Scanner(System.in);

		for (;;) {

			System.out.println("Enter a Command (Case Sensitive): ");

			String[] cmd = scanner.nextLine().split("\\s");

			if ("S".equals(cmd[0])) {
				if(cmd.length != 4){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				float[] arg = getArgs(cmd);
								
				model.scale(arg[0], arg[1], arg[2]);

			} else if ("T".equals(cmd[0])) {
				if(cmd.length != 4){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				float[] arg = getArgs(cmd);
				
				model.translate(arg[0], arg[1], arg[2]);

			} else if ("R".equals(cmd[0])) {
				if(cmd.length != 5){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				float[] arg = getArgs(cmd);
				
				model.rotate(arg[0], arg[1], arg[2], arg[3]);

			} else if ("W".equals(cmd[0])) {
				if(cmd.length != 1){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				try {
					RayTracerIO.writeModelFile(model, outputfile);
					System.out.println("File written to: " + outputfile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				scanner.close();
				System.exit(0);

			} else {
				System.out.println("Bad Command. Try again.");
				continue;
			}
			model.printStats();
			System.out.println("Done.");

		}

	}
	
	private static float[] getArgs(String[] args){
		float[] floatArgs = new float[args.length -1];
		
		for(int i = 0; i < floatArgs.length; i++){
			floatArgs[i] = Float.parseFloat(args[i+1]);
		}
		
		return floatArgs;
	}

}
