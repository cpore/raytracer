package cs410.raytracer;

public class Face {
	
	public int[] vertexIndicies;

	public Face(int[] vertexIndicies) {
		this.vertexIndicies = vertexIndicies;
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
