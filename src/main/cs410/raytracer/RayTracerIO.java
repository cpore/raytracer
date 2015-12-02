package cs410.raytracer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RayTracerIO {

    private RayTracerIO() {
    }

    public static Model readModelFile(String filename)
            throws NumberFormatException, IOException, InvalidFormatException {
        System.out.println("Reading model file: " + filename);
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

        double[][] verticies = readVerticies(br, numVerticies, lineIndex);

        Face[] faces = readFaces(br, numFaces, numVerticies, lineIndex, verticies);

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
            if ((line.startsWith("property double") || line.startsWith("property double"))
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
            Integer lineIndex, double[][] verticies) throws IOException, InvalidFormatException {
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

            int length = Integer.parseInt(parts[0]);
            int[] indicies = new int[length];
            Vector[] vertexPoints = new Vector[length];

            for (int i = 0; i < indicies.length; i++) {
                try {
                    indicies[i] = Integer.parseInt(parts[i + 1].trim());
                    vertexPoints[i] = new Vector(verticies[Vector.x][indicies[i]],
                            verticies[Vector.y][indicies[i]], verticies[Vector.z][indicies[i]], 1);
                    // System.out.print(indicies[i] + " ");
                } catch (NumberFormatException nfe) {
                    throw new InvalidFormatException(
                            "Bad index at line: " + lineIndex + ": " + line);
                }

                if (indicies[i] > numVerticies) {
                    throw new InvalidFormatException("Index out of range (" + indicies[i]
                            + ") at line: " + lineIndex + ": " + line);
                }

            }
            // System.out.println();

            lineIndex++;

            faces[idx++] = new Face(indicies, vertexPoints);

            if (idx == numFaces)
                break;
        }

        return faces;
    }

    private static double[][] readVerticies(BufferedReader br, int numVerticies, Integer lineIndex)
            throws IOException, InvalidFormatException {

        // Vector[] verticies = new Vector[numVerticies];
        double[][] verticies = new double[4][numVerticies];
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
                verticies[Model.x][col] = Double.parseDouble(parts[Model.x]);
            } catch (NumberFormatException nfe) {
                throw new InvalidFormatException(
                        "Bad x value (" + parts[Model.x] + ") at line: " + lineIndex + ": " + line);
            }

            try {
                verticies[Model.y][col] = Double.parseDouble(parts[Model.y]);
            } catch (NumberFormatException nfe) {
                throw new InvalidFormatException(
                        "Bad y value (" + parts[Model.y] + ") at line: " + lineIndex + ": " + line);
            }

            try {
                verticies[Model.z][col] = Double.parseDouble(parts[Model.z]);
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
        if (line == null) {
            br.close();
            throw new InvalidFormatException("Bad first line in camera model file.");
        }
        String[] split = line.trim().split("\\s+");
        if (split.length < 3) {
            br.close();
            throw new InvalidFormatException("Focal point line doesn't have three values.");
        }
        Vector fp = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                Double.parseDouble(split[2]), 1);

        // read the look at point line
        line = br.readLine();
        if (line == null) {
            br.close();

            throw new InvalidFormatException("Bad second line in camera model file.");
        }
        split = line.trim().split("\\s+");
        if (split.length < 3) {
            br.close();
            throw new InvalidFormatException("Look-at point line doesn't have three values.");
        }
        Vector lap = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                Double.parseDouble(split[2]), 1);

        // read the VUP line
        line = br.readLine();
        if (line == null) {
            br.close();
            throw new InvalidFormatException("Bad third line in camera model file.");
        }
        split = line.trim().split("\\s+");
        if (split.length < 3) {
            br.close();
            throw new InvalidFormatException("VUP line doesn't have three values.");
        }
        Vector vup = new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                Double.parseDouble(split[2]), 1);

        // read focal length
        line = br.readLine();
        if (line == null) {
            br.close();
            throw new InvalidFormatException("Bad fourth line in camera model file.");
        }
        double d = Double.parseDouble(line.trim());

        // read min/max values for u/v
        line = br.readLine();
        if (line == null) {
            br.close();
            throw new InvalidFormatException("Bad fifth line in camera model file.");
        }
        split = line.trim().split("\\s+");
        if (split.length < 4) {
            br.close();
            throw new InvalidFormatException("u/v coordinates line doesn't have four values.");
        }
        int minu = Integer.parseInt(split[0]);
        int minv = Integer.parseInt(split[1]);
        int maxu = Integer.parseInt(split[2]);
        int maxv = Integer.parseInt(split[3]);

        br.close();

        // focal length 0?
        if (d == 0) {
            throw new InvalidFormatException("Focal length cannot be zero.");
        }
        if (minu >= maxu) {
            throw new InvalidFormatException(
                    "Min u coordinate must be less than max u coordinate.");
        }

        if (minv >= maxv) {
            throw new InvalidFormatException(
                    "Min v coordinate must be less than max v coordinate.");
        }

        if (fp.equals(lap)) {
            throw new InvalidFormatException(
                    "The focal point is equal to the look-at-point (or within an epsilon of 0.00001).");
        }

        if (vup.getMagnitude() == 0.0f) {
            throw new InvalidFormatException("VUP has 0 magnitude.");
        }

        // TODO check for other bad camera values

        return new CameraModel(fp, lap, vup, d, minu, minv, maxu, maxv);

    }

    public static synchronized ArrayList<LightSource> readMaterialFile(String materialfile, ArrayList<Model> models)
            throws InvalidFormatException, IOException {

        ArrayList<LightSource> lightRays = new ArrayList<LightSource>();
        BufferedReader br = new BufferedReader(new FileReader(materialfile));

        // read the Face lines
        int linenum = 1;
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");

            if(line.isEmpty()) continue;
            
            switch (parts[0].trim().charAt(0)) {
            case 'L':
                System.out.println(line);
                double r = Double.parseDouble(parts[1].trim());
                double g = Double.parseDouble(parts[2].trim());
                double b = Double.parseDouble(parts[3].trim());
                
                if(parts[4].trim().equals("A") || parts[5].trim().equals("A") || parts[6].trim().equals("A")){
                    lightRays.add(new LightSource(null, new RGB(r,g,b)));
                    break;
                }
                
                double x = Double.parseDouble(parts[4].trim());
                double y = Double.parseDouble(parts[5].trim());
                double z = Double.parseDouble(parts[6].trim());
                
                lightRays.add(new LightSource(new Vector(x, y, z, 1f), new RGB(r, g, b)));

                break;
            case 'M':
                int modelIdx = Integer.parseInt(parts[1].trim());
                if(modelIdx > models.size()-1){
                    break;
                    //throw new InvalidFormatException("Model index("+modelIdx+") out of range");
                }
                int firstPolygon = Integer.parseInt(parts[2].trim());
                int lastPolygon = Integer.parseInt(parts[3].trim());
                if (lastPolygon < firstPolygon) {
                    throw new InvalidFormatException(
                            "Last Polygon(" + lastPolygon + ") greater than first polygon("
                                    + firstPolygon + ") at line: " + linenum);
                }
                double kr = Double.parseDouble(parts[4].trim());
                double kg = Double.parseDouble(parts[5].trim());
                double kb = Double.parseDouble(parts[6].trim());
                double ks = Double.parseDouble(parts[7].trim());
                int alpha = Integer.parseInt(parts[8].trim());
                double kt = Double.parseDouble(parts[9].trim());

                Model model = models.get(modelIdx);
                for (int i = firstPolygon; i <= lastPolygon; i++) {
                    if(i == model.faces.length) break;
                    Face face = model.faces[i];
                    face.setDiffuseReflectance(kr, kg, kb);
                    face.setSpecularReflectance(ks, alpha);
                    face.setTranslucency(kt);
                }
                break;
            default:
                break;//throw new InvalidFormatException("Invalid starting character at line: " + linenum);
            }

            linenum++;
        }

        br.close();
        return lightRays;
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
