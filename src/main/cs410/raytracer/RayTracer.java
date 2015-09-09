package cs410.raytracer;

import java.util.Scanner;

public class RayTracer {
	

	public RayTracer() {

	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("You failed specify exactly two arguments.");
			System.out.println("Usage: rt.jar <inputfile> <outputfile>");
			System.exit(1);
		}
		
		String inputfile = args[0];
		String outputfile = args[1];
		
		Model model = new Model();

		Scanner scanner = new Scanner(System.in);

		for (;;) {

			System.out.println("Enter a Command (Case Sensitive): ");

			String[] command = scanner.nextLine().split("\\s");

			if ("S".equals(command[0])) {
				if(command.length != 4){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				float[] arg = getArgs(command);
								
				model.scale(arg[1], arg[2], arg[3]);

			} else if ("T".equals(command[0])) {
				if(command.length != 4){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				float[] arg = getArgs(command);
				
				model.translate(arg[1], arg[2], arg[3]);

			} else if ("R".equals(command[0])) {
				if(command.length != 5){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				float[] arg = getArgs(command);
				
				model.rotate(arg[1], arg[2], arg[3], arg[4]);

			} else if ("W".equals(command[0])) {
				if(command.length != 1){
					System.out.println("Wrong number of arguments. Try again.");
					continue;
				}
				
				model.write(outputfile);

			} else {
				System.out.println("Bad Command. Try again.");
				continue;
			}

		}

	}
	
	private static float[] getArgs(String[] args){
		float[] floatArgs = new float[4];
		
		floatArgs[1] = Float.parseFloat(args[1]);
		floatArgs[2] = Float.parseFloat(args[2]);
		floatArgs[3] = Float.parseFloat(args[3]);
		
		if(args[4] == null) return floatArgs;
		
		floatArgs[4] = Float.parseFloat(args[4]);
		
		return floatArgs;
	}

}
