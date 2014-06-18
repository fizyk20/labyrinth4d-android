package com.fizyk.engine4d;

import com.fizyk.math4d.*;

public class Vertex {

	public Vector4 pos;
	public Color c;
	
	public Vertex(Vector4 pos, Color c)
	{
		this.pos = pos;
		this.c = c;
	}
	
	public Vertex()
	{
		this(new Vector4(), Color.current);
	}
	
	public Vertex(Vector4 pos)
	{
		this(pos, Color.current);
	}
	
	public boolean equals(Vertex arg)
	{
		return pos.equals(arg);
	}
}
