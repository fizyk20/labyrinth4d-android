package com.fizyk.engine4d;

import com.fizyk.math4d.Hyperplane;

public interface Primitive
{
	public void draw();
	
	public Vertex vertex(int i);
	public int nVertices();
	
	public Primitive intersect(Hyperplane h);
}
