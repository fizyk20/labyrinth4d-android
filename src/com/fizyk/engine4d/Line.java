package com.fizyk.engine4d;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Line implements Primitive {

	private Vertex[] v;
	
	public Line(Vector4 pos1, Color c1, Vector4 pos2, Color c2)
	{
		v = new Vertex[2];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
	}
	
	public Line(Vector4 pos1, Vector4 pos2)
	{
		v = new Vertex[2];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public Vertex vertex(int i) {
		if(i < 0 || i > 1)
			return null;
		return v[i];
	}

	@Override
	public int nVertices() {
		return 2;
	}

	@Override
	public Primitive intersect(Hyperplane h) {
		// TODO Auto-generated method stub
		return null;
	}

}
