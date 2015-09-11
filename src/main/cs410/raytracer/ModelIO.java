package cs410.raytracer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModelIO {

	private ModelIO() {	}


	public static Model readFile(String filename)
			throws NumberFormatException, IOException, InvalidFormatException {
		Integer lineIndex = new Integer(1);

		BufferedReader br = new BufferedReader(new FileReader(filename));

		String fileHeader = readFileHeader(br, lineIndex);

		int numVerticies = parseNumVerticies(fileHeader);
		int numFaces = parseNumFaces(fileHeader);
		if (numFaces == 0) {
			throw new InvalidFormatException("Could not find the number of faces.");
		}
		if (numVerticies == 0) {
			throw new InvalidFormatException("Could not find the number of verticies.");
		}

		Vector[] verticies = readVerticies(br, numVerticies, lineIndex);

		Face[] faces = readFaces(br, numFaces, numVerticies, lineIndex);

		br.close();

		Model model = new Model(fileHeader, verticies, faces);

		model.printStats();

		return model;
	}

	private static int parseNumVerticies(String header) {
		int numVerticies = 0;
		for (String line : header.split("\n")) {
			if (line.contains("element vertex")) {
				String[] parts = line.split("\\s");
				String numVerts = parts[parts.length - 1];
				numVerticies = Integer.parseInt(numVerts);
				break;
			}
		}
		return numVerticies;
	}

	private static int parseNumFaces(String header) {
		int numFaces = 0;
		for (String line : header.split("\n")) {
			if (line.contains("element face")) {
				String[] parts = line.split("\\s");
				String numFcs = parts[parts.length - 1];
				numFaces = Integer.parseInt(numFcs);
				break;
			}
		}
		return numFaces;
	}


	private static String readFileHeader(BufferedReader br, Integer lineIndex) throws IOException, InvalidFormatException {
		StringBuilder headerBuilder = new StringBuilder();
		String line = null;

		// read the header lines
		while ((line = br.readLine()) != null) {
			// Save with the line
			headerBuilder.append(line + "\n");

			lineIndex++;

			if (line.equals("end_header")) {
				break;
			}

		}

		return headerBuilder.toString();
	}

	private static Face[] readFaces(BufferedReader br, int numFaces, int numVerticies, Integer lineIndex) throws IOException, InvalidFormatException {
		Face[] faces = new Face[numFaces];
		int idx = 0;

		String line = null;
		// read the Face lines
		while ((line = br.readLine()) != null) {

			// Deal with the line
			String[] parts = line.split("\\s");

			if (parts.length == 0) {
				throw new InvalidFormatException("Empty line at line: " + lineIndex);
			}

			int[] indicies = new int[parts.length];

			for (int i = 0; i < indicies.length; i++) {
				try {
					indicies[i] = Integer.parseInt(parts[i]);
				} catch (NumberFormatException nfe) {
					throw new InvalidFormatException("Bad index at line: " + lineIndex);
				}

				if (indicies[i] > numVerticies) {
					throw new InvalidFormatException("Index out of range (" + indicies[i] + ") at line: " + lineIndex);
				}
			}

			lineIndex++;

			faces[idx++] = new Face(indicies);

			if (idx == numFaces)
				break;
		}

		return faces;
	}

	private static Vector[] readVerticies(BufferedReader br, int numVerticies, Integer lineIndex) throws IOException, InvalidFormatException {

		Vector[] verticies = new Vector[numVerticies];
		int idx = 0;

		String line = null;

		// read the Vertex lines
		while ((line = br.readLine()) != null) {
			float x;
			float y;
			float z;
			// Deal with the line
			String[] parts = line.split("\\s");

			if (parts.length == 0) {
				throw new InvalidFormatException("Empty line at line: " + lineIndex);
			}
			// read the values from the line
			try {
				x = Float.parseFloat(parts[Model.x]);
			} catch (NumberFormatException nfe) {
				throw new InvalidFormatException("Bad x value at line: " + lineIndex);
			}

			try {
				y = Float.parseFloat(parts[Model.y]);
			} catch (NumberFormatException nfe) {
				throw new InvalidFormatException("Bad y value at line: " + lineIndex);
			}

			try {
				z = Float.parseFloat(parts[Model.z]);
			} catch (NumberFormatException nfe) {
				throw new InvalidFormatException("Bad z value at line: " + lineIndex);
			}

			// add the newly read values as a vertex
			verticies[idx++] = new Vector(x, y, z);

			// we were able to get the values, so increment out line counter
			lineIndex++;

			// we read all the verticies
			if (idx == numVerticies) {
				break;
			}
		}

		return verticies;
	}

	public static synchronized void writeFile(Model model, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);

		fw.write(model.fileHeader);

		for (Vector v : model.verticies) {
			fw.write(v.toString() + "\n");
		}

		for (Face f : model.faces) {
			fw.write(f.toString() + "\n");
		}

		fw.close();
	}

	

}
