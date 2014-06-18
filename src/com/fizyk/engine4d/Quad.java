package com.fizyk.engine4d;

import com.fizyk.math4d.Hyperplane;
import com.fizyk.math4d.Vector4;

public class Quad implements Primitive {

	private Vertex[] v;
	
	public Quad(Vector4 pos1, Color c1, Vector4 pos2, Color c2, Vector4 pos3, Color c3, Vector4 pos4, Color c4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1, c1);
		v[1] = new Vertex(pos2, c2);
		v[2] = new Vertex(pos3, c3);
		v[3] = new Vertex(pos4, c4);
	}
	
	public Quad(Vector4 pos1, Vector4 pos2, Vector4 pos3, Vector4 pos4)
	{
		v = new Vertex[4];
		v[0] = new Vertex(pos1);
		v[1] = new Vertex(pos2);
		v[2] = new Vertex(pos3);
		v[3] = new Vertex(pos4);
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public Vertex vertex(int i) {
		if(i < 0 || i > 3)
			return null;
		return v[i];
	}

	@Override
	public int nVertices() {
		return 4;
	}

	@Override
	public Primitive intersect(Hyperplane h) {
		// TODO Auto-generated method stub
		return null;
	}

}
