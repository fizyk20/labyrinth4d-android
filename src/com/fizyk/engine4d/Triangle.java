package com.fizyk.engine4d;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Triangle implements Primitive {

	private Vertex[] v;
	
	public Triangle(Vector4 pos1, Color c1, Vector4 pos2, Color c2, Vector4 pos3, Color c3)
	{
		v = new Vertex[3];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
		v[2] = new Vertex(pos3, c3);
	}
	
	public Triangle(Vector4 pos1, Vector4 pos2, Vector4 pos3)
	{
		v = new Vertex[3];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
		v[2] = new Vertex(pos3);
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public Vertex vertex(int i) {
		if(i < 0 || i > 2)
			return null;
		return v[i];
	}

	@Override
	public int nVertices() {
		return 3;
	}

	@Override
	public Primitive intersect(Hyperplane h) {
		// TODO Auto-generated method stub
		return null;
	}

}
