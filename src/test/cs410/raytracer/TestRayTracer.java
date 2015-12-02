package cs410.raytracer;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRayTracer {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        /*Pattern pattern = Pattern.compile(".*\\.ppm");
        File dir = new File("src/testoutput/");
        for(File file: dir.listFiles()){
            if(pattern.matcher(file.getName()).matches()) file.delete();
        }*/
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    private long getStart(){
        return System.nanoTime();
    }

    private void printElapsed(long start){
        long finish = System.nanoTime();

        double elapsedSecs = ((double)finish - (double)start) * 0.000000001;
        System.out.println("Elapsed time (seconds) = " + elapsedSecs);
    }
    
    private void openViewer(String outputfile){
    	if(outputfile == null) return;
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("win") >= 0) {
			/*try {
    				Process p = r.exec("src/OpenSeeIt/OpenSeeIt.exe " + outputfile);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}*/
		}else {
			try {
				Runtime r = Runtime.getRuntime();
				r.exec("eog " + outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    @After
    public void tearDown() throws Exception {
       
    }
    
    private void runRayTracer(String[] args){
        long start = getStart();
        RayTracer.main(args);
        printElapsed(start);
        openViewer(args[args.length-1]);
    }

    @Test
    public void testOctahedron() {
        String outputfile = "src/testoutput/octahedron.ppm";
        String[] args = new String[]{"src/cameras/octahedron_bigcam1","src/materials/props1", "src/models/octahedron_big.ply", outputfile};
        runRayTracer(args);
    }

    @Test
    public void testPyramid() {
        String[] args = new String[]{"src/cameras/pyramidcam1","src/materials/props1", "src/models/pyramid_big.ply", "src/testoutput/pyramid.ppm"};

        runRayTracer(args);
    }

    @Test
    public void testSphere() {
        String outputfile = "src/testoutput/sphere1.ppm";
        String cameraFile = "src/cameras/spherecam1";
        String materialFile = "src/materials/props1";
        String modelFile1 = "src/models/sphere.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, outputfile};

        runRayTracer(args);
    }

    @Test
    public void testBeethoven() {
        String outputfile = "src/testoutput/beethoven.ppm";
        String[] args = new String[]{"src/cameras/beethoven_bigcam1", "src/materials/props1", "src/models/beethoven_big.ply", outputfile};

        runRayTracer(args);

    }

    @Test
    public void testBeethovenCow() {
        String[] args = new String[]{"src/cameras/beethoven_cow_cam1", "src/materials/props1", "src/models/beethoven_big.ply", "src/models/cow_big.ply", "src/testoutput/beethoven_cow.ppm"};

        RayTracer.main(args);
    }
    
    @Test
    public void testBeethovenSphereShadowed() {
        String[] args = new String[]{"src/cameras/beethoven_sphere_shadowedcam1", "src/materials/props1", "src/models/beethoven_big.ply", "src/models/sphere_shadowed.ply", "src/testoutput/beethoven_sphere_shadowed.ppm"};

        runRayTracer(args);
    }
    
    @Test
    public void testBeethovenSphereShadowedSmall() {
        String[] args = new String[]{"src/cameras/beethoven_sphere_shadowedcam1", "src/materials/props1", "src/models/beethoven_big.ply", "src/models/sphere_shadowed_small.ply", "src/testoutput/beethoven_sphere_shadowed_small.ppm"};

        runRayTracer(args);
    }

    @Test
    public void testBunny() {
        String[] args = new String[]{"src/cameras/bunny_bigcam1", "src/materials/props1", "src/models/bunny_big.ply", "src/testoutput/bunny.ppm"};

        RayTracer.main(args);
    }

    @Test
    public void testCow() {
        String outputfile = "src/testoutput/cow.ppm";
        String[] args = new String[]{"src/cameras/cow_bigcam1", "src/materials/props1", "src/models/cow_big.ply", outputfile};

        runRayTracer(args);
    }

    @Test
    public void testSphere2() {
        String[] args = new String[]{"src/cameras/spherecam2", "src/materials/props1", "src/models/sphere.ply", "src/testoutput/sphere2.ppm"};

        RayTracer.main(args);
    }

    @Test
    public void testAirplane() {
        String outputfile = "src/testoutput/airplane1.ppm";
        String cameraFile = "src/cameras/airplane_camera.txt";
        String materialFile = "src/materials/airplane_materials.txt";
        String modelFile1 = "src/models/airplane.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, outputfile};

        runRayTracer(args);
    }
    
    @Test
    public void testAirplane2() {
        String outputfile = "src/testoutput/airplane2.ppm";
        String cameraFile = "src/cameras/airplane_camera2.txt";
        String materialFile = "src/materials/airplane_materials.txt";
        String modelFile1 = "src/models/airplane.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, outputfile};

        runRayTracer(args);
    }
    
    @Test
    public void testAirplane3() {
        String outputfile = "src/testoutput/airplane3.ppm";
        String cameraFile = "src/cameras/airplane_camera3.txt";
        String materialFile = "src/materials/airplane_materials.txt";
        String modelFile1 = "src/models/airplane.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, outputfile};

        runRayTracer(args);
    }
    
    @Test
    public void testAirplane4() {
        String outputfile = "src/testoutput/airplane4.ppm";
        String cameraFile = "src/cameras/airplane_camera4.txt";
        String materialFile = "src/materials/airplane_materials.txt";
        String modelFile1 = "src/models/airplane.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, outputfile};

        runRayTracer(args);
    }
    
    @Test
    public void testSphereInCubeInRoom() {
        String outputfile = "src/testoutput/sphere_in_cube_in_room.ppm";
        String cameraFile = "src/cameras/sphere_in_cube";
        String materialFile = "src/materials/sphere_in_cube";
        String modelFile1 = "src/models/sphere.ply";
        String modelFile2 = "src/models/cube1.ply";
        String modelFile3 = "src/models/cube_big.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, modelFile2, modelFile3, outputfile};

        runRayTracer(args);
    }
    
    @Test
    public void testSphereInCube() {
        String outputfile = "src/testoutput/sphere_in_cube.ppm";
        String cameraFile = "src/cameras/sphere_in_cube";
        String materialFile = "src/materials/sphere_in_cube";
        String modelFile1 = "src/models/sphere.ply";
        String modelFile2 = "src/models/cube1.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile1, modelFile2, outputfile};

        runRayTracer(args);
    }
    
    @Test
    public void testCube() {
        String outputfile = "src/testoutput/cube1.ppm";
        String cameraFile = "src/cameras/sphere_in_cube";
        String materialFile = "src/materials/props1";
        String modelFile2 = "src/models/cube1.ply";
        String[] args = new String[]{cameraFile, materialFile, modelFile2, outputfile};

        runRayTracer(args);
    }

}
