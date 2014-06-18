package com.fizyk.math4d;

public class Hyperplane {

	public Vector4 normal;
	public double param; // C in equation n_x*x + n_y*y + n_z*z + n_w*w + C = 0
	
	public Hyperplane()
	{
		normal = new Vector4();
		param = 0.;
	}
	
	public double dotPlane(Vector4 point)
	{
		return normal.dot(point) + param;
	}
}
