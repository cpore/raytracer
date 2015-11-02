package cs410.raytracer;

public class Transform {

	public static final int x = 0;
	public static final int y = 1;
	public static final int z = 2;
	public static final int w = 3;

	public double[][] matrix;

	public Transform(double[][] transformMatrix) {
		this.matrix = transformMatrix;
	}

	/**
	 * multiplies this matrix by that matrix such that the result of the
	 * operation new = this * that
	 * 
	 * @param that
	 */
	public Transform multiply(Transform that) {
		double[][] result = new double[4][4];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				Vector row = new Vector(this.matrix[i][0], this.matrix[i][1], this.matrix[i][2], this.matrix[i][3]);

				Vector col = new Vector(that.matrix[0][j], that.matrix[1][j], that.matrix[2][j], this.matrix[3][j]);

				result[i][j] = row.dotProductTransform(col);

			}

		}
		return new Transform(result);
	}

	public void apply(Model m) {
		double[][] result = new double[4][m.verticies[1].length];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < m.verticies[1].length; j++) {
				Vector row = new Vector(this.matrix[i][0], this.matrix[i][1], this.matrix[i][2], this.matrix[i][3]);

				Vector col = new Vector(m.verticies[0][j], m.verticies[1][j], m.verticies[2][j], m.verticies[3][j]);

				result[i][j] = row.dotProductTransform(col);

			}
		}

		m.verticies = result;
	}

	/**
	 * 
	 * @return A transform that is the transpose of this
	 */
	public Transform getTranspose() {

		double[][] transpose = new double[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				transpose[j][i] = matrix[i][j];
		
		return new Transform(transpose);

	}

}
