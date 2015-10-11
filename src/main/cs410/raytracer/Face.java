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
        for(int i = 0; i < verticies.length-4; i++){
            Vector a = verticies[i];
            Vector b = verticies[i+1];
            Vector c = verticies[i+2];
            N = getN(a, b, c);
            if(N != null) break;
            
        }
        
        if(N == null){
            throw new InvalidFormatException("All Vertices for face are collinear");
        }
        
    }
		
	private Vector getN(Vector a, Vector b, Vector c){
	    Vector ab = a.subtract(b);
	    Vector bc = b.subtract(c);
	    Vector ac = a.subtract(c);
	    
	    Vector abPlusBc = ab.add(bc);
	    
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
