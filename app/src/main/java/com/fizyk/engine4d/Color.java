package com.fizyk.engine4d;

import com.fizyk.math4d.Vector4;

public class Color {

	public double r, g, b, a;
	
	public static Color current = new Color();
	
	public Color()
	{
		this(0., 0., 0., 1.);
	}
	
	public Color(Vector4 v)
	{
		this(v.getCoord(0), v.getCoord(1), v.getCoord(2), v.getCoord(3));
	}
	
	public Color(double r, double g, double b)
	{
		this(r, g, b, 1.);
	}
	
	public Color(double r, double g, double b, double a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Vector4 toVector()
	{
		return new Vector4(r, g, b, a);
	}
}
