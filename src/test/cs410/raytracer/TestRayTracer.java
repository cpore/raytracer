package cs410.raytracer;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRayTracer {
    long start;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Pattern pattern = Pattern.compile(".*\\.ppm");
        File dir = new File("src/testoutput/");
        for(File file: dir.listFiles()){
            if(pattern.matcher(file.getName()).matches()) file.delete();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        start = System.nanoTime();
    }

    @After
    public void tearDown() throws Exception {
        long finish = System.nanoTime();
        
        double elapsedSecs = ((double)finish - (double)start) * 0.000000001;
        System.out.println("Elapsed time (seconds) = " + elapsedSecs);
    }
    
    @Test
    public void testOctahedron() {
        String[] args = new String[]{"src/cameramodels/octahedron_bigcam1","src/materialprops/props1", "src/models/octahedron_big.ply", "src/testoutput/octahedron.ppm"};
    
        
        RayTracer.main(args);
       
    }
    
    @Test
    public void testPyramid() {
        String[] args = new String[]{"src/cameramodels/pyramidcam1","src/materialprops/props1", "src/models/pyramid_big.ply", "src/testoutput/pyramid.ppm"};
    
        RayTracer.main(args);
    }
    
    @Test
    public void testSphere() {
        String[] args = new String[]{"src/cameramodels/spherecam1", "src/materialprops/props1", "src/models/sphere.ply", "src/testoutput/sphere1.ppm"};
    
        RayTracer.main(args);
    }

    @Test
    public void testBeethoven() {
        String[] args = new String[]{"src/cameramodels/beethoven_bigcam1", "src/materialprops/props1", "src/models/beethoven_big.ply", "src/testoutput/beethoven.ppm"};
    
        RayTracer.main(args);
    }
    
    @Test
    public void testBeethovenCow() {
        String[] args = new String[]{"src/cameramodels/beethoven_cow_cam1", "src/materialprops/props1", "src/models/beethoven_big.ply", "src/models/cow_big.ply", "src/testoutput/beethoven_cow.ppm"};
    
        RayTracer.main(args);
    }
    
    @Test
    public void testBunny() {
        String[] args = new String[]{"src/cameramodels/bunny_bigcam1", "src/materialprops/props1", "src/models/bunny_big.ply", "src/testoutput/bunny.ppm"};
    
        RayTracer.main(args);
    }
    
    @Test
    public void testCow() {
        String[] args = new String[]{"src/cameramodels/cow_bigcam1", "src/materialprops/props1", "src/models/cow_big.ply", "src/testoutput/cow.ppm"};
    
        RayTracer.main(args);
    }
    
    @Test
    public void testSphere2() {
        String[] args = new String[]{"src/cameramodels/spherecam2", "src/materialprops/props1", "src/models/sphere.ply", "src/testoutput/sphere2.ppm"};
    
        RayTracer.main(args);
    }

}
