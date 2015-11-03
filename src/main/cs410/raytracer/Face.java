package cs410.raytracer;

public class Face {
    
	
	int[] vertexIndicies;
	
	Vector[] verticies;
	Vector N;
	Vector zeroVector = new Vector(0, 0, 0, 0);
	
	//diffuse (Lambertian) reflection values
	RGB Kd = new RGB(0.5, 0.5, 0.5);

	
	//specular reflection values
	double ks = 0.0;
	int alpha = 1;

	public Face(int[] vertexIndicies, Vector[] verticies) throws InvalidFormatException {
        this.vertexIndicies = vertexIndicies;
        this.verticies = verticies;
        Vector a = null;
        Vector b = null;
        Vector c = null;
        
        for(int i = 0; i < verticies.length-2; i++){
            //System.out.println("Checking..." + i);
            a = verticies[i];
            b = verticies[i+1];
            c = verticies[i+2];
            
            N = getN(a, b, c);
            if(N != null) break;
            
            //System.out.print("a = " + a.toString() + ", ");
            //System.out.print("b = " + b.toString() + ", ");
            //System.out.println("c = " + c.toString());
            
        }
        
        if(N == null){
            throw new InvalidFormatException("All Vertices for face are collinear");
        }
    }
		
	private Vector getN(Vector a, Vector b, Vector c){
	    Vector ab = b.subtract(a);
	    Vector bc = c.subtract(b);
	    Vector ac = c.subtract(a);
	    
	    Vector abCrossAc = ab.crossProduct(ac);
	    
	    if(abCrossAc.equalTo(zeroVector)){
	        //System.out.println("abCrossAc = " + abCrossAc.toString());
	        return null;
	    }
	    
	    Vector n = ab.crossProduct(bc).getNormalized();
	    
	    return n;
	}
	
	public void setDiffuseReflectance(double kr, double kg, double kb){
	    this.Kd = new RGB(kr, kg, kb);
	}
	
	public void setSpecularReflectance(double ks, int alpha){
        this.ks = ks;
        this.alpha = alpha;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(vertexIndicies.length + " ");
		
		for(int i = 0; i < vertexIndicies.length; i++){
			sb.append(vertexIndicies[i] + " ");
		}
		return sb.toString();
	}

}
