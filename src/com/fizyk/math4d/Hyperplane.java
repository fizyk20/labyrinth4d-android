package com.fizyk.math4d;

public class Hyperplane {

	public Vector4 normal;
	public double param; // C in equation n_x*x + n_y*y + n_z*z + n_w*w + C = 0
	
	public enum Position { ABOVE, BELOW, IN };
	
	public Hyperplane()
	{
		normal = new Vector4();
		param = 0.;
	}
	
	public Position inPlane(Vector4 point)
	{
		double dot = normal.dot(point) + param;
		return (dot < -1e-15)  					? Position.BELOW :
			   (dot > -1e-15 && dot < 1e-15) 	?    Position.IN : Position.ABOVE;
	}
}
