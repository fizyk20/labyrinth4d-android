package com.fizyk.engine4d;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Point implements Primitive {
	
	private Vertex v;
	
	public Point(Vector4 pos, Color c)
	{
		v = new Vertex(pos, c);
	}
	
	public Point(Vector4 pos)
	{
		v = new Vertex(pos);
	}

	@Override
	public void draw() {
		
	}

	@Override
	public Vertex vertex(int i) {
		return v;
	}

	@Override
	public int nVertices() {
		return 1;
	}

	@Override
	public Primitive intersect(Hyperplane h) {
		if(Math.abs(h.dotPlane(v.pos)) < 1e-15)
			return this;
		else
			return null;
	}

}
