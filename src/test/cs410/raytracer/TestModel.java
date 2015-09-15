package cs410.raytracer;

import static org.junit.Assert.fail;

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
			Model model = ModelIO.readFile("src/models/small/dodecahedron.txt");
			model.scale(10f, 10f, 10f);
			ModelIO.writeFile(model, "src/testoutput/dodecahedron_scaled10.ply");
			
			model.scale(1f/10f, 1f/10f, 1f/10f);
			ModelIO.writeFile(model, "src/testoutput/dodecahedron_unscaled10.ply");
			
			model = ModelIO.readFile("src/models/small/sphere.txt");
			model.scale(10f, 10f, 10f);
			ModelIO.writeFile(model, "src/testoutput/sphere_scaled10.ply");

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
		
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("meshlab src/testoutput/dodecahedron_scaled10.ply");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTranslate() {
		try {
			Model model = ModelIO.readFile("src/models/small/dodecahedron.txt");
			model.translate(10f, 10f, 10f);
			ModelIO.writeFile(model, "src/testoutput/dodecahedron_translated10.ply");
			
			ModelIO.readFile("src/testoutput/dodecahedron_translated10.ply");
			
			model.translate(-10f, -10f, -10f);
			ModelIO.writeFile(model, "src/testoutput/dodecahedron_untranslated10.ply");
			
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
		
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("meshlab src/testoutput/dodecahedron_translated10.ply");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testRotate() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteFile() {
		try {
			Model model = ModelIO.readFile("src/models/small/dodecahedron.txt");
			ModelIO.writeFile(model, "src/testoutput/dodecahedron.ply");

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

		try {

			Model model = ModelIO.readFile("src/models/large/bunny.txt");
			ModelIO.writeFile(model, "src/testoutput/bunny.ply");
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

		try {

			Model model = ModelIO.readFile("src/models/small/sphere.txt");
			ModelIO.writeFile(model, "src/testoutput/shpere.ply");
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

}
