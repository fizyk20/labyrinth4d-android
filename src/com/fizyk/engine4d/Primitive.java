package com.fizyk.engine4d;

public class Primitive {

	public Vertex[] vert;
	
	public Primitive()
	{
		vert = new Vertex[4];
		for(int i=0; i<4; i++)
			vert[i] = new Vertex();
	}
}
