package cs410.raytracer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Model {

	public static final int MIN_X = 0;
	public static final int MAX_X = 1;
	public static final int MIN_Y = 2;
	public static final int MAX_Y = 3;
	public static final int MIN_Z = 4;
	public static final int MAX_Z = 5;

	private int lineIndex;
	private String fileHeader;

	public int numVerticies;
	public Vertex[] verticies;
	public Vertex meanVertex;
	public float[] boundingBox;
	public int numFaces;
	public Face[] faces;

	public Model(String filename) throws NumberFormatException, IOException, InvalidFormatException {
		readFile(filename);
	}

	public void scale(float sx, float sy, float sz) {

	}

	public void translate(float tx, float ty, float tz) {

	}

	public void rotate(float rx, float ry, float rz, float theta) {

	}

	public void readFile(String filename) throws NumberFormatException, IOException, InvalidFormatException {
		lineIndex = 1;

		BufferedReader br = new BufferedReader(new FileReader(filename));

		fileHeader = readFileHeader(br);

		this.verticies = readVerticies(br);

		this.faces = readFaces(br);
		
		br.close();
		
		printStats();
	}
	
	public void printStats(){
		System.out.println("Number of Verticies: " + numVerticies);
		
		System.out.println("Number of Faces: " + numFaces);
		
		System.out.println("Mean Vertex: " + meanVertex.toString());
		
		System.out.println("Bounding Box:");
		System.out.println("\tMin X: " + Utils.prettyPrint(boundingBox[MIN_X]));
		System.out.println("\tMax X: " + Utils.prettyPrint(boundingBox[MAX_X]));
		System.out.println("\tMin Y: " + Utils.prettyPrint(boundingBox[MIN_Y]));
		System.out.println("\tMax Y: " + Utils.prettyPrint(boundingBox[MAX_Y]));
		System.out.println("\tMin Z: " + Utils.prettyPrint(boundingBox[MIN_Z]));
		System.out.println("\tMax Z: " + Utils.prettyPrint(boundingBox[MAX_Z]));
	}

	private Face[] readFaces(BufferedReader br) throws IOException, InvalidFormatException {
		Face[] faces = new Face[numFaces];
		int idx = 0;

		String line = null;
		// read the Face lines
		while ((line = br.readLine()) != null) {

			// Deal with the line
			String[] parts = line.split("\\s");
			
			if(parts.length == 0){
				throw new InvalidFormatException("Empty line at line: " + lineIndex);
			}
			
			int[] indicies = new int[parts.length];

			for(int i=0; i<indicies.length; i++){
				try {
					indicies[i] = Integer.parseInt(parts[i]);
				} catch (NumberFormatException nfe) {
					throw new InvalidFormatException("Bad index at line: " + lineIndex);
				}
				
				if(indicies[i] > numVerticies){
					throw new InvalidFormatException("Index out of range (" + indicies[i] + ") at line: " + lineIndex);
				}
			}


			lineIndex++;

			faces[idx++] = new Face(indicies);

			if (idx == numFaces) break;
		}

		return faces;
	}

	private Vertex[] readVerticies(BufferedReader br) throws IOException, InvalidFormatException {
		boundingBox = new float[6];
		float totalx = 0f;
		float totaly = 0f;
		float totalz = 0f;

		Vertex[] verticies = new Vertex[numVerticies];
		int idx = 0;

		String line = null;

		// read the Vertex lines
		while ((line = br.readLine()) != null) {
			float[] values = new float[3];
			// Deal with the line
			String[] parts = line.split("\\s");

			if(parts.length == 0){
				throw new InvalidFormatException("Empty line at line: " + lineIndex);
			}
			// read the values from the line
			try {
				values[Vertex.x] = Float.parseFloat(parts[Vertex.x]);
			} catch (NumberFormatException nfe) {
				throw new InvalidFormatException("Bad x value at line: " + lineIndex);
			}

			try {
				values[Vertex.y] = Float.parseFloat(parts[Vertex.y]);
			} catch (NumberFormatException nfe) {
				throw new InvalidFormatException("Bad y value at line: " + lineIndex);
			}

			try {
				values[Vertex.z] = Float.parseFloat(parts[Vertex.z]);
			} catch (NumberFormatException nfe) {
				throw new InvalidFormatException("Bad z value at line: " + lineIndex);
			}

			// add the newly read values as a vertex
			verticies[idx++] = new Vertex(values);

			// we were able to get the values, so increment out line counter
			lineIndex++;

			// check for min/max values
			if(values[Vertex.x] < boundingBox[MIN_X]){
				boundingBox[MIN_X] = values[Vertex.x];
			}else if(values[Vertex.x] > boundingBox[MIN_X]){
				boundingBox[MAX_X] = values[Vertex.x];
			}

			if(values[Vertex.y] < boundingBox[MIN_Y]){
				boundingBox[MIN_Y] = values[Vertex.y];
			}else if(values[Vertex.y] > boundingBox[MAX_Y]){
				boundingBox[MAX_Y] = values[Vertex.y];
			}

			if(values[Vertex.z] < boundingBox[MIN_Z]){
				boundingBox[MIN_Z] = values[Vertex.z];
			}else if(values[Vertex.z] > boundingBox[MAX_Z]){
				boundingBox[MAX_Z] = values[Vertex.z];
			}

			// update our mean vertex values
			totalx += values[Vertex.x];
			totaly += values[Vertex.y];
			totalz += values[Vertex.z];


			// if we read all the verticies, exit and calculate the mean vertex
			if (idx == numVerticies) {
				meanVertex = new Vertex(totalx / numVerticies, totaly / numVerticies, totalz / numVerticies);
				break;
			}
		}

		return verticies;
	}

	private String readFileHeader(BufferedReader br) throws IOException, InvalidFormatException {
		StringBuilder headerBuilder = new StringBuilder();
		String line = null;

		// read the header lines
		while ((line = br.readLine()) != null) {
			// Save with the line
			headerBuilder.append(line + "\n");

			if (line.contains("element vertex")) {
				String[] parts = line.split("\\s");
				String numVerticies = parts[parts.length - 1];
				this.numVerticies = Integer.parseInt(numVerticies);
			}

			if (line.contains("element face")) {
				String[] parts = line.split("\\s");
				String numFaces = parts[parts.length - 1];
				this.numFaces = Integer.parseInt(numFaces);
			}

			lineIndex++;

			if (line.equals("end_header")){
				if(this.numFaces == 0){
					throw new InvalidFormatException("Could not find the number of faces.");
				}
				if(this.numVerticies == 0){
					throw new InvalidFormatException("Could not find the number of verticies.");
				}
				break;
			}

		}

		return headerBuilder.toString();
	}

	public void writeFile(String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		
		fw.write(fileHeader);
		
		for(Vertex v: verticies){
			fw.write(v.toString() + "\n");
		}
		
		for(Face f: faces){
			fw.write(f.toString() + "\n");
		}
		
		fw.close();

	}

	public class InvalidFormatException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidFormatException(String message) {
			super(message);
		}

	}

}
