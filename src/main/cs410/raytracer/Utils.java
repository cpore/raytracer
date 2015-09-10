package cs410.raytracer;

public class Utils {

	private Utils() {
		// TODO Auto-generated constructor stub
	}
	
	public static String prettyPrint(float f) {
		int i = (int) f;
		return f == i ? String.valueOf(i) : String.valueOf(f);
	}

}
