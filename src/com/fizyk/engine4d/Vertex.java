package com.fizyk.engine4d;

import com.fizyk.math4d.*;

public class Vertex {

	public Vector4 pt;
	public double r, g, b, a;
	
	public Vertex()
	{
		pt = new Vector4();
		a = 1.;
	}
	
	public boolean equals(Vertex arg)
	{
		return pt.equals(arg);
	}
}
