package cs410.raytracer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RayTracerIO {

    private RayTracerIO() {
    }

    public static Model readModelFile(String filename)
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

        float[][] verticies = readVerticies(br, numVerticies, lineIndex);

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

    private static String readFileHeader(BufferedReader br, Integer lineIndex)
            throws IOException, InvalidFormatException {
        StringBuilder headerBuilder = new StringBuilder();
        String line = null;

        // read the header lines
        while ((line = br.readLine()) != null) {
            if ((line.startsWith("property float") || line.startsWith("property double"))
                    && !(line.endsWith(" x") || line.endsWith(" X"))
                    && !(line.endsWith(" y") || line.endsWith(" Y"))
                    && !(line.endsWith(" z") || line.endsWith(" Z"))) {

                continue;
            }
            // Save with the line
            headerBuilder.append(line + "\n");

            lineIndex++;

            if (line.equals("end_header")) {
                break;
            }

        }

        return headerBuilder.toString();
    }

    private static Face[] readFaces(BufferedReader br, int numFaces, int numVerticies,
            Integer lineIndex) throws IOException, InvalidFormatException {
        Face[] faces = new Face[numFaces];
        int idx = 0;

        String line = null;
        // read the Face lines
        while ((line = br.readLine()) != null) {

            // Deal with the line
            String[] parts = line.trim().split("\\s+");

            if (parts.length == 0) {
                throw new InvalidFormatException("Empty line at line: " + lineIndex);
            }

            int[] indicies = new int[parts.length];

            for (int i = 0; i < indicies.length; i++) {
                try {
                    indicies[i] = Integer.parseInt(parts[i].trim());
                } catch (NumberFormatException nfe) {
                    throw new InvalidFormatException(
                            "Bad index at line: " + lineIndex + ": " + line);
                }

                if (indicies[i] > numVerticies) {
                    throw new InvalidFormatException("Index out of range (" + indicies[i]
                            + ") at line: " + lineIndex + ": " + line);
                }
            }

            lineIndex++;

            faces[idx++] = new Face(indicies);

            if (idx == numFaces)
                break;
        }

        return faces;
    }

    private static float[][] readVerticies(BufferedReader br, int numVerticies, Integer lineIndex)
            throws IOException, InvalidFormatException {

        // Vector[] verticies = new Vector[numVerticies];
        float[][] verticies = new float[4][numVerticies];
        int col = 0;

        String line = null;

        // read the Vertex lines
        while ((line = br.readLine()) != null) {

            // Deal with the line
            String[] parts = line.trim().split("\\s+");

            if (parts.length == 0) {
                throw new InvalidFormatException("Empty line at line: " + lineIndex + ": " + line);
            }
            // read the values from the line
            try {
                verticies[Model.x][col] = Float.parseFloat(parts[Model.x]);
            } catch (NumberFormatException nfe) {
                throw new InvalidFormatException(
                        "Bad x value (" + parts[Model.x] + ") at line: " + lineIndex + ": " + line);
            }

            try {
                verticies[Model.y][col] = Float.parseFloat(parts[Model.y]);
            } catch (NumberFormatException nfe) {
                throw new InvalidFormatException(
                        "Bad y value (" + parts[Model.y] + ") at line: " + lineIndex + ": " + line);
            }

            try {
                verticies[Model.z][col] = Float.parseFloat(parts[Model.z]);
            } catch (NumberFormatException nfe) {
                throw new InvalidFormatException(
                        "Bad z value (" + parts[Model.z] + ")at line: " + lineIndex + ": " + line);
            }

            verticies[Model.w][col] = 1;

            col++;

            // we were able to get the values, so increment out line counter
            lineIndex++;

            // we read all the verticies
            if (col == numVerticies) {
                break;
            }
        }

        return verticies;
    }

    public static synchronized CameraModel readCameraModel(String camerafile)
            throws InvalidFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(camerafile));

        // read the fp line
        String line = br.readLine();
        if (line == null){
            br.close();
            throw new InvalidFormatException("Bad first line in camera model file.");
        }
        String[] split = line.trim().split("\\s+");
        if (split.length < 3){
            br.close();
            throw new InvalidFormatException("Focal point line doesn't have three values.");
        }
        Vector fp = new Vector(Float.parseFloat(split[0]), Float.parseFloat(split[1]),
                Float.parseFloat(split[2]), 1);

        //read the look at point line
        line = br.readLine();
        if (line == null){
            br.close();
        
            throw new InvalidFormatException("Bad second line in camera model file.");
        }
        split = line.trim().split("\\s+");
        if (split.length < 3){
            br.close();
            throw new InvalidFormatException("Look-at point line doesn't have three values.");
        }
        Vector lap = new Vector(Float.parseFloat(split[0]), Float.parseFloat(split[1]),
                Float.parseFloat(split[2]), 1);

        // read the VUP line
        line = br.readLine();
        if (line == null){
            br.close();
            throw new InvalidFormatException("Bad third line in camera model file.");
        }
        split = line.trim().split("\\s+");
        if (split.length < 3){
            br.close();
            throw new InvalidFormatException("VUP line doesn't have three values.");
        }
        Vector vup = new Vector(Float.parseFloat(split[0]), Float.parseFloat(split[1]),
                Float.parseFloat(split[2]), 1);

        //read focal length
        line = br.readLine();
        if (line == null){
            br.close();
            throw new InvalidFormatException("Bad fourth line in camera model file.");
        }
        float d = Float.parseFloat(line.trim());

        // read min/max values for u/v
        line = br.readLine();
        if (line == null){
            br.close();
            throw new InvalidFormatException("Bad fifth line in camera model file.");
        }
        split = line.trim().split("\\s+");
        if (split.length < 4){
            br.close();
            throw new InvalidFormatException("u/v coordinates line doesn't have four values.");
        }
        int minu = Integer.parseInt(split[0]);
        int minv = Integer.parseInt(split[1]);
        int maxu = Integer.parseInt(split[2]);
        int maxv = Integer.parseInt(split[3]);
        
        br.close();
        
        
        //focal length 0?
        if(d == 0){
            throw new InvalidFormatException("Focal length cannot be zero.");
        }
        if(minu >= maxu){
            throw new InvalidFormatException("Min u coordinate must be less than max u coordinate.");
        }
        
        if(minv >= maxv){
            throw new InvalidFormatException("Min v coordinate must be less than max v coordinate.");
        }
        
        //TODO check for other bad camera values
        
        return new CameraModel(fp, lap, vup, d, minu, minv, maxu, maxv);

    }

    public static synchronized void writeModelFile(Model model, String filename)
            throws IOException {
        FileWriter fw = new FileWriter(filename);

        fw.write(model.fileHeader);

        for (int j = 0; j < model.verticies[1].length; j++) {
            fw.write(Utils.prettyPrint(model.verticies[Model.x][j]) + " "
                    + Utils.prettyPrint(model.verticies[Model.y][j]) + " "
                    + Utils.prettyPrint(model.verticies[Model.z][j]) + "\n");
        }

        for (Face f : model.faces) {
            fw.write(f.toString() + "\n");
        }

        fw.close();
    }

}
