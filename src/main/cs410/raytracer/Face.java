package cs410.raytracer;

public class Face {
	
	public int[] vertexIndicies;
	
	Vector[] verticies;
	Vector N;

	public Face(int[] vertexIndicies) {
		this.vertexIndicies = vertexIndicies;
	}
	
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
            
        }
        
        if(N == null){
            
            throw new InvalidFormatException("All Vertices for face are collinear");
        }
        
    }
		
	private Vector getN(Vector a, Vector b, Vector c){
	    Vector ab = b.subtract(a);
	    Vector bc = c.subtract(b);
	    Vector ac = c.subtract(a);
	    
	    Vector abPlusBc = ab.add(bc);
	    
	    //System.out.println("abPlusBc = " + abPlusBc.toString());
	    //System.out.println("ac = " + ac.toString());
	    
	    if(abPlusBc.equalTo(ac)) return null;
	    
	    Vector n = ab.crossProduct(bc);
	    
	    n.normalize();
	    return n;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < vertexIndicies.length; i++){
			sb.append(vertexIndicies[i] + " ");
		}
		return sb.toString();
	}

}
