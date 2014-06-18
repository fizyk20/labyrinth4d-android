package com.fizyk.engine4d;

public class Color {

	public double r, g, b, a;
	
	public static Color current = new Color();
	
	public Color()
	{
		this(0., 0., 0., 1.);
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
}
