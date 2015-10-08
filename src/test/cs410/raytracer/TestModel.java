package cs410.raytracer;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestModel {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testScale() {

		try {
			Model model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			model.scale(10f, 10f, 10f);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_scaled10.ply");

			RayTracerIO.readModelFile("src/testoutput/pyramid_translated10.ply");

			model.scale(-10f, -10f, -10f);
			RayTracerIO.writeModelFile(model, "src/testoutput/dodecahedron_unscaled10.ply");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testTranslate() {
		try {
			Model model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			model.translate(10f, 10f, 10f);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_translated10.ply");

			RayTracerIO.readModelFile("src/testoutput/pyramid_translated10.ply");

			model.translate(-10f, -10f, -10f);
			RayTracerIO.writeModelFile(model, "src/testoutput/dodecahedron_untranslated10.ply");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

		/*Runtime r = Runtime.getRuntime();
		try {
			r.exec("meshlab src/testoutput/dodecahedron_translated10.ply");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@Test
	public void testRotate() {
		try {
			Model model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			model.rotate(0f, 1f, 0f, 45);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_rotated_y_45.ply");

			model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			model.rotate(0f, 1f, 0f, 90);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_rotated_y_90.ply");

			model.rotate(0f, 1f, 0f, -45);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_unrotated_y_45.ply");

			model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			model.rotate(0f, 1f, 0f, -360);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_rotated_y_360.ply");

			model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			model.rotate(1f, 0f, 0f, 90);
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid_rotated_x_90.ply");

			model = RayTracerIO.readModelFile("src/models/medium/beethoven.ply");
			model.rotate(0f, 1f, 0f, 90);
			RayTracerIO.writeModelFile(model, "src/testoutput/beethoven_rotated_y_90.ply");

			model = RayTracerIO.readModelFile("src/models/medium/beethoven.ply");
			model.rotate(1f, 0f, 0f, 90);
			RayTracerIO.writeModelFile(model, "src/testoutput/beethoven_rotated_x_90.ply");

			model = RayTracerIO.readModelFile("src/models/large/bunny.ply");
			model.rotate(0f, 1f, 0f, 90);
			RayTracerIO.writeModelFile(model, "src/testoutput/bunny_rotated_y_90.ply");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

		/*Runtime r = Runtime.getRuntime();
		try {
			r.exec("meshlab src/testoutput/pyramid_rotated_y_45.ply");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@Test
	public void testWriteFile() {
		try {
			Model model = RayTracerIO.readModelFile("src/models/small/dodecahedron.txt");
			RayTracerIO.writeModelFile(model, "src/testoutput/dodecahedron.ply");

			model = RayTracerIO.readModelFile("src/models/large/bunny.txt");
			RayTracerIO.writeModelFile(model, "src/testoutput/bunny.ply");

			model = RayTracerIO.readModelFile("src/models/small/sphere.txt");
			RayTracerIO.writeModelFile(model, "src/testoutput/shpere.ply");

			model = RayTracerIO.readModelFile("src/models/small/pyramid.ply");
			RayTracerIO.writeModelFile(model, "src/testoutput/pyramid.ply");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void testWriteAll() {
		for(File file: new File("src/testoutput/").listFiles()) file.delete();
		
		File folder = new File("src/models/all");
		File[] listOfFiles = folder.listFiles();

		Model model = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isFile()) {
				continue;
			}
			String filename = listOfFiles[i].getName();
			try {
				model = RayTracerIO.readModelFile(listOfFiles[i].getPath());
				
				RayTracerIO.writeModelFile(model, "src/testoutput/" + filename);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Bad File: " + filename);
				e.printStackTrace();
			}
			
			
			
		}
	}
	
	@Test
	public void testRotateAll() {
		File rotateDir = new File("src/testoutput/rotateall");
		rotateDir.mkdir();
		for(File file: rotateDir.listFiles()) file.delete();
		
		File folder = new File("src/models/all");
		File[] listOfFiles = folder.listFiles();

		Model model = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isFile()) {
				continue;
			}
			String filename = listOfFiles[i].getName();
			try {
				model = RayTracerIO.readModelFile(listOfFiles[i].getPath());
				
				model.rotate(0, 0, 1, 90);
				
				RayTracerIO.writeModelFile(model, rotateDir.getPath() + File.separator + filename);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Bad File: " + filename);
				e.printStackTrace();
			}
			
			
			
		}
	}

}
